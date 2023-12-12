package svc.WebSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WSHandler {
   private final ConnectionManager connections = new ConnectionManager();
   private final GameDAO gameDAO = GameDAO.getInstance();

   @OnWebSocketMessage
   public void onMessage(Session session, String stream) throws IOException {
      Gson gson = new Gson();

      UserGameCommand command = gson.fromJson(stream, UserGameCommand.class);
      try {
         switch (command.getCommandType()) {
            case JOIN_PLAYER, JOIN_OBSERVER:
               JoinCommand joinCommand = gson.fromJson(stream, JoinCommand.class);
               joinGame(joinCommand, session);
               break;
            case MAKE_MOVE:
               System.out.println("Show me your moves!");
               break;
            case RESIGN:
               System.out.println("I never learned how to read!!!");
               break;
            case LEAVE:
               LeaveCommand leaveCommand = gson.fromJson(stream, LeaveCommand.class);
               leaveGame(leaveCommand);
               break;
         }
      } catch (DataAccessException e) {
         ServerMessage errorMessage = generateMessage(ServerMessageType.ERROR, e.getMessage(), null);
         session.getRemote().sendString(errorMessage.toString());
      }
   }

   @OnWebSocketError
   public void onError(Session session, Throwable error) throws IOException {
      String message = String.format("[500] Error: %s", error.getMessage());
      System.out.println(message);

      ServerMessage errorMessage = generateMessage(ServerMessageType.ERROR, message, null);
      session.getRemote().sendString(errorMessage.toString());
   }

   private void joinGame(JoinCommand command, Session session) throws IOException, DataAccessException {
      String player = getUsername(command.getAuthString());
      int gameID = command.getGameID();
      TeamColor color = command.getPlayerColor();
      Game game = gameDAO.getGame(gameID);

      connections.add(player, gameID, session);

      String message;
      if (color == null) message = String.format("%s joined as an observer!", player);
      else {
         // Check if player is rejoining their color
         switch (color) {
            case WHITE -> {
               if (!Objects.equals(game.getWhiteUsername(), player))
                  throw new DataAccessException("Error: already taken");
            }
            case BLACK -> {
               if (!Objects.equals(game.getBlackUsername(), player))
                  throw new DataAccessException("Error: already taken");
            }
            default -> throw new DataAccessException("Error: bad request");
         }
         message = String.format("%s joined as an %s!", player, color);
      }
      // Send game to the new player
      ServerMessage loadGameMessage = generateMessage(ServerMessageType.LOAD_GAME, null, game);
      session.getRemote().sendString(loadGameMessage.toString());

      // Broadcast new player's arrival
      connections.broadcast(player, gameID, generateMessage(ServerMessageType.NOTIFICATION, message, null));
   }

   private void leaveGame(LeaveCommand command) throws IOException, DataAccessException {
      String player = getUsername(command.getAuthString());
      connections.remove(player);

      String message = String.format("%s has left the game.", player);
      connections.broadcast(player, command.getGameID(), generateMessage(ServerMessageType.NOTIFICATION, message, null));
   }

   private String getUsername(String authToken) throws DataAccessException {
      AuthDAO authDAO = AuthDAO.getInstance();
      return authDAO.getToken(authToken).getUsername();
   }

   private ServerMessage generateMessage(ServerMessageType serverMessageType, String message, Game game) {
      ServerMessage serverMessage = new ServerMessage(serverMessageType);
      switch (serverMessageType) {
         case NOTIFICATION -> serverMessage.setMessage(message);
         case LOAD_GAME -> serverMessage.setGame(game);
         case ERROR -> serverMessage.setErrorMessage(message);
      }
      return serverMessage;
   }
}
