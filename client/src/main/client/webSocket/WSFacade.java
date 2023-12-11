package client.webSocket;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.LeaveCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class WSFacade extends Endpoint {
   Session session;
   ServerMessageHandler serverMessageHandler;
   private final Gson gson = new GsonBuilder().serializeNulls().create();
   private final int errCode = 500;

   public WSFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
      try {
         url = url.replace("http", "ws");
         URI socketURI = new URI(url + "/connect");
         this.serverMessageHandler = serverMessageHandler;

         WebSocketContainer container = ContainerProvider.getWebSocketContainer();
         this.session = container.connectToServer(this, socketURI);

         this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
               Notification notification = new Gson().fromJson(message, Notification.class);
               serverMessageHandler.displayMessage(notification);
            }
         });

      } catch (DeploymentException | IOException | URISyntaxException e) {
         System.out.println(Arrays.toString(e.getStackTrace()));
         throw new ResponseException(errCode, String.format("[%d] %s", errCode, e.getMessage()));
      }
   }

   @Override
   public void onOpen(Session session, EndpointConfig endpointConfig) {
   }

   public void joinGame(String authToken, int gameID, String username, TeamColor color) throws ResponseException {
      try {
         var command = new JoinCommand(authToken, gameID, username, color);
         this.session.getBasicRemote().sendText(gson.toJson(command));
      } catch (IOException e) {
         throw new ResponseException(errCode, String.format("[%d] %s", errCode, e.getMessage()));
      }
   }

   public void leaveGame(String authToken, int gameID, String username) throws ResponseException {
      try {
         var command = new LeaveCommand(authToken, gameID, username);
         this.session.getBasicRemote().sendText(gson.toJson(command));
      } catch (IOException e) {
         throw new ResponseException(errCode, String.format("[%d] %s", errCode, e.getMessage()));
      }
   }
}
