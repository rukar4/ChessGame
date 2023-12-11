package client.webSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

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

         this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
               Notification notification = new Gson().fromJson(message, Notification.class);
               notificationHandler.notify(notification);
            }
         });

      } catch (DeploymentException | IOException | URISyntaxException e) {
         System.out.println(Arrays.toString(e.getStackTrace()));
         throw new ResponseException(500, e.getMessage());
      }
   }

   @Override
   public void onOpen(Session session, EndpointConfig endpointConfig) {
   }

   public void joinPlayer(String authToken, int gameID, String username, TeamColor color) throws ResponseException {
      try {
         var command = new JoinPlayerCommand(authToken, gameID, username, color);

         Gson gson = new GsonBuilder().serializeNulls().create();

         this.session.getBasicRemote().sendText(gson.toJson(command));
      } catch (IOException e) {
         throw new ResponseException(500, e.getMessage());
      }
   }
}
