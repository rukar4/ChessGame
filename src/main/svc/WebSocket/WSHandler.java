package svc.WebSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;
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
      System.out.println(error.getMessage());

      Error errorMessage = new Error(error.getMessage());
      session.getRemote().sendString(errorMessage.toString());
   }

   private void joinGame(JoinCommand command, Session session) throws IOException {
      String player = command.getUsername();
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
      connections.broadcast(player, gameID, new Notification(message));
   }

   private void leaveGame(LeaveCommand command) throws IOException {
      String player = command.getUsername();
      connections.remove(player);

      String message = String.format("%s has left the game.", player);
      connections.broadcast(player, command.getGameID(), new Notification(message));
   }
}
