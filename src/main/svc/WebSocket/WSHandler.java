package svc.WebSocket;

import chess.ChessGame.TeamColor;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import game.ChsGame;
import game.Move;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import svc.game.ChessMoveAdapter;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

import static game.ChsGame.GameStatus.*;
import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WSHandler {
   private final ConnectionManager connections = new ConnectionManager();
   private final GameDAO gameDAO = GameDAO.getInstance();

   @OnWebSocketMessage
   public void onMessage(Session session, String stream) throws IOException {
      Gson gson = new GsonBuilder().registerTypeAdapter(ChessMove.class, new ChessMoveAdapter()).create();

      UserGameCommand command = gson.fromJson(stream, UserGameCommand.class);
      try {
         switch (command.getCommandType()) {
            case JOIN_PLAYER, JOIN_OBSERVER:
               JoinCommand joinCommand = gson.fromJson(stream, JoinCommand.class);
               joinGame(joinCommand, session);
               break;
            case MAKE_MOVE:
               MakeMoveCommand makeMoveCommand = gson.fromJson(stream, MakeMoveCommand.class);
               makeMove(makeMoveCommand);
               break;
            case RESIGN:
               ResignCommand resignCommand = gson.fromJson(stream, ResignCommand.class);
               resign(resignCommand);
               break;
            case LEAVE:
               LeaveCommand leaveCommand = gson.fromJson(stream, LeaveCommand.class);
               leaveGame(leaveCommand);
               break;
         }
      } catch (DataAccessException | InvalidMoveException e) {
         ServerMessage errorMessage = constructServerMessage(ERROR, e.getMessage(), null);
         session.getRemote().sendString(errorMessage.toString());
      }
   }

   @OnWebSocketError
   public void onError(Session session, Throwable error) throws IOException {
      String message = String.format("[500] Error: %s", error.getMessage());
      System.out.println(message);

      ServerMessage errorMessage = constructServerMessage(ERROR, message, null);
      session.getRemote().sendString(errorMessage.toString());
   }

   private void joinGame(JoinCommand command, Session session) throws IOException, DataAccessException {
      String player = getUsername(command.getAuthString());
      int gameID = command.getGameID();
      TeamColor color = command.getPlayerColor();
      Game game = gameDAO.getGame(gameID);

      connections.add(player, gameID, session);

      String message = getJoinMessage(color, player, game);

      // Send game to the new player
      ServerMessage loadGameMessage = constructServerMessage(LOAD_GAME, null, game);
      session.getRemote().sendString(loadGameMessage.toString());

      // Broadcast new player's arrival
      connections.broadcast(player, gameID, constructServerMessage(NOTIFICATION, message, null));
   }

   private void resign(ResignCommand command) throws IOException, DataAccessException {
      String player = getUsername(command.getAuthString());
      int gameID = command.getGameID();
      Game game = gameDAO.getGame(gameID);

      if (game.getGameData().getStatus() != ONGOING) throw new DataAccessException("Error: game has already concluded");

      TeamColor victorColor = game.getOpponentColor(player);

      if (victorColor == null) throw new DataAccessException("Error: resigning user is not a player");
      else {
         game.getGameData().setVictor(victorColor);
         game.getGameData().setStatus(RESIGN);
         gameDAO.updateGame(gameID, game);

         String message = String.format("%s has resigned. %s WINS!", player, game.getOpponentName(player));
         ServerMessage resignationNotice = constructServerMessage(NOTIFICATION, message, null);
         connections.broadcast("", gameID, resignationNotice);
      }
   }

   private void leaveGame(LeaveCommand command) throws IOException, DataAccessException {
      String player = getUsername(command.getAuthString());
      connections.remove(player);

      String message = String.format("%s has left the game.", player);
      connections.broadcast(player, command.getGameID(), constructServerMessage(NOTIFICATION, message, null));
   }

   private void makeMove(MakeMoveCommand command) throws DataAccessException, InvalidMoveException, IOException {
      String playerName = getUsername(command.getAuthString());
      int gameID = command.getGameID();
      Move move = (Move) command.getMove();

      Game game = gameDAO.getGame(gameID);
      ChsGame chsGame = game.getGameData();
      String opponentName = game.getOpponentName(playerName);
      TeamColor playerColor = game.getPlayerColor(playerName);
      TeamColor opponentColor = game.getOpponentColor(playerName);

      checkPlayerTurn(game, playerName, opponentName, playerColor);

      chsGame.makeMove(move);

      ServerMessage moveMessage = constructServerMessage(NOTIFICATION, String.format("%s: %s", playerName, move), null);
      connections.broadcast(playerName, gameID, moveMessage);

      boolean isInCheck = chsGame.isInCheck(opponentColor);
      updateGameStatus(chsGame, isInCheck, playerColor, opponentColor);

      game.setGameData(chsGame);
      gameDAO.updateGame(gameID, game);

      connections.broadcast("", gameID, constructServerMessage(LOAD_GAME, null, game));

      broadcastGameStatus(chsGame, gameID, playerName, opponentName, isInCheck);
   }

   private static void checkPlayerTurn(Game game, String player, String opponent, TeamColor playerColor) throws InvalidMoveException {
      ChsGame chsGame = game.getGameData();

      if (game.getGameData().getStatus() != ONGOING)
         throw new InvalidMoveException("Error: game has already concluded");

      if (!(player.equalsIgnoreCase(game.getWhiteUsername()) || player.equalsIgnoreCase(game.getBlackUsername())))
         throw new InvalidMoveException("Error: you are not a player in this game");
      else // If-statement checks if the player is moving at the right time and if they are not playing themselves
         if (!(playerColor == chsGame.getTeamTurn() || player.equalsIgnoreCase(opponent)))
            throw new InvalidMoveException("Error: it is not your turn");
   }

   private static void updateGameStatus(ChsGame chsGame, boolean isInCheck, TeamColor player, TeamColor opponent) {
      boolean isInStalemate = chsGame.isInStalemate(opponent);
      boolean isInCheckmate = isInStalemate && isInCheck;

      if (isInCheckmate) {
         chsGame.setStatus(CHECKMATE);
         chsGame.setVictor(player);
      } else if (isInStalemate) {
         chsGame.setStatus(STALEMATE);
      }
   }

   private void broadcastGameStatus(ChsGame chsGame, int gameID, String player, String opponentName, boolean isInCheck) throws IOException {
      String message;
      ServerMessage notification = new ServerMessage(NOTIFICATION);
      switch (chsGame.getStatus()) {
         case CHECKMATE:
            message = String.format("%s is in checkmate! %s WINS!", opponentName, player);
            notification.setMessage(message);
            connections.broadcast("", gameID, notification);
            break;
         case STALEMATE:
            message = String.format("%s caused a stalemate. It's a draw!", player);
            notification.setMessage(message);
            connections.broadcast("", gameID, notification);
            break;
         default:
            if (isInCheck) {
               message = String.format("%s is in check!", opponentName);
               notification.setMessage(message);
               connections.broadcast("", gameID, notification);
            }
      }
   }

   private String getUsername(String authToken) throws DataAccessException {
      AuthDAO authDAO = AuthDAO.getInstance();
      return authDAO.getToken(authToken).getUsername();
   }

   private ServerMessage constructServerMessage(ServerMessageType serverMessageType, String message, Game game) {
      ServerMessage serverMessage = new ServerMessage(serverMessageType);
      switch (serverMessageType) {
         case NOTIFICATION -> serverMessage.setMessage(message);
         case LOAD_GAME -> serverMessage.setGame(game);
         case ERROR -> serverMessage.setErrorMessage(message);
      }
      return serverMessage;
   }

   private static String getJoinMessage(TeamColor color, String player, Game game) throws DataAccessException {
      String message;
      if (color == null) message = String.format("%s joined as an observer!", player);
      else {
         // Check if player is joining as their color
         String playerInGame = game.getPlayerFromColor(color);
         if (Objects.equals(playerInGame, player)) {
            message = String.format("%s joined as %s!", player, color.toString().toLowerCase());
         } else {
            throw new DataAccessException("Error: bad request");
         }
      }
      return message;
   }
}
