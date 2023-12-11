package svc.WebSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinPlayerCommand;

import java.io.IOException;

@WebSocket
public class WSHandler {
   private final ConnectionManager connections = new ConnectionManager();

   @OnWebSocketMessage
   public void onMessage(Session session, String stream) throws IOException {
      JoinPlayerCommand command = new Gson().fromJson(stream, JoinPlayerCommand.class);
      switch (command.getCommandType()) {
         case JOIN_PLAYER -> joinPlayer(command, session);
      }
   }

   private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
      String player = command.getUsername();
      TeamColor color = command.getColor();

      connections.add(player, session);

      String message;
      if (color == null) message = String.format("%s joined as an observer!", player);
      else {
         switch (color) {
            case WHITE -> message = String.format("%s joined as white!", player);
            case BLACK -> message = String.format("%s joined as black!", player);
            default -> message = String.format("%s joined as an observer!", player);
         }
      }
      Notification notification = new Notification(message);

      connections.broadcast(player, notification);
   }
}
