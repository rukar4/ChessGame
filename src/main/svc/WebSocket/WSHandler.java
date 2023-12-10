package svc.WebSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

public class WSHandler {
   private final ConnectionManager connections = new ConnectionManager();

   @OnWebSocketMessage
   public void userMessage(Session session, String message) throws IOException {
      UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
      switch (command.getCommandType()) {
         case JOIN_PLAYER -> joinPlayer((JoinPlayerCommand) command, session);
      }
   }

   @OnWebSocketMessage
   public void serverMessage(Session session, String message) throws IOException {
      ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
   }

   private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
      String player = command.getUsername();
      TeamColor color = command.getColor();

      connections.add(player, session);

      String message;
      switch (color) {
         case WHITE -> message = String.format("%s joined as white!", player);
         case BLACK -> message = String.format("%s joined as black!", player);
         default -> message = String.format("%s joined as an observer!", player);
      }
      Notification notification = new Notification(message);

      connections.broadcast(player, notification);
   }
}
