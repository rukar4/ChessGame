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

@WebSocket
public class WSHandler {
   private final ConnectionManager connections = new ConnectionManager();

   @OnWebSocketMessage
   public void onMessage(Session session, String stream) throws IOException {
      Gson gson = new Gson();

      UserGameCommand command = gson.fromJson(stream, UserGameCommand.class);
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
   }

   @OnWebSocketError
   public void onError(Session session, Throwable error) throws IOException {
      String message = String.format("[500] Error: %s", error.getMessage());
      System.out.println();

      ServerMessage errorMessage = generateMessage(ServerMessageType.ERROR, message, null);

      session.getRemote().sendString(errorMessage.toString());
   }

   private void joinGame(JoinCommand command, Session session) throws IOException {
      String player = getUsername(command.getAuthString());

      int gameID = command.getGameID();
      TeamColor color = command.getColor();

      connections.add(player, gameID, session);

      String message;
      if (color == null) message = String.format("%s joined as an observer!", player);
      else {
         switch (color) {
            case WHITE -> message = String.format("%s joined as white!", player);
            case BLACK -> message = String.format("%s joined as black!", player);
            default -> message = String.format("%s joined as an observer!", player);
         }
      }
      // Send game to the new player
      Game game = getGame(command.getGameID());
      ServerMessage loadGameMessage = generateMessage(ServerMessageType.LOAD_GAME, null, game);
      session.getRemote().sendString(loadGameMessage.toString());

      // Broadcast new player's arrival
      connections.broadcast(player, gameID, generateMessage(ServerMessageType.NOTIFICATION, message, null));
   }

   private void leaveGame(LeaveCommand command) throws IOException {
      String player = getUsername(command.getAuthString());
      connections.remove(player);

      String message = String.format("%s has left the game.", player);
      connections.broadcast(player, command.getGameID(), generateMessage(ServerMessageType.NOTIFICATION, message, null));
   }

   private String getUsername(String authToken) {
      AuthDAO authDAO = AuthDAO.getInstance();
      String username = "";
      try {
         username = authDAO.getToken(authToken).getUsername();
      } catch (DataAccessException e) {
         System.out.printf("[%d] Error: %s\n", e.getResponseCode().getCode(), e.getMessage());
      }
      return username;
   }

   private Game getGame(int gameID) {
      GameDAO gameDAO = GameDAO.getInstance();
      Game game = null;
      try {
         game = gameDAO.getGame(gameID);
      } catch (DataAccessException e) {
         System.out.printf("[%d] Error: %s\n", e.getResponseCode().getCode(), e.getMessage());
      }
      return game;
   }

   private ServerMessage generateMessage(ServerMessageType serverMessageType, String message, Game game) {
      ServerMessage serverMessage = new ServerMessage(serverMessageType);
      switch (serverMessageType) {
         case NOTIFICATION, ERROR -> serverMessage.setMessage(message);
         case LOAD_GAME -> serverMessage.setGame(game);
      }
      return serverMessage;
   }
}
