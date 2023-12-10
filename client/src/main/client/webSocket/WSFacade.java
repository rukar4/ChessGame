package client.webSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSFacade extends Endpoint {
   Session session;
   NotificationHandler notificationHandler;

   public WSFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
      try {
         url = url.replace("http", "ws");
         URI socketURI = new URI(url + "/connect");
         this.notificationHandler = notificationHandler;

         WebSocketContainer container = ContainerProvider.getWebSocketContainer();
         this.session = container.connectToServer(this, socketURI);

         this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            Notification notification = new Gson().fromJson(message, Notification.class);
            notificationHandler.notify(notification);
         });

      } catch (DeploymentException | IOException | URISyntaxException e) {
         throw new ResponseException(500, e.getMessage());
      }
   }

   @Override
   public void onOpen(Session session, EndpointConfig endpointConfig) {
   }

   public void joinPlayer(String authToken, int gameID, String username, TeamColor color) throws ResponseException {
      try {
         var command = new JoinPlayerCommand(authToken, gameID, username, color);
         this.session.getBasicRemote().sendText(new Gson().toJson(command));
      } catch (IOException e) {
         throw new ResponseException(500, e.getMessage());
      }
   }
}
