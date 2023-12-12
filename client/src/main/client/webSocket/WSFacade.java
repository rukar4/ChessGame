package client.webSocket;

import chess.ChessGame.TeamColor;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import svc.game.ChessPieceAdapter;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.ResignCommand;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.Modifier;
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
            public void onMessage(String stream) {
               ServerMessage message = new GsonBuilder()
                       .excludeFieldsWithModifiers(Modifier.STATIC)
                       .registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter())
                       .create()
                       .fromJson(stream, ServerMessage.class);
               serverMessageHandler.displayMessage(message);
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

   public void joinGame(String authToken, int gameID, TeamColor color) throws ResponseException {
      try {
         var command = new JoinCommand(authToken, gameID, color);
         this.session.getBasicRemote().sendText(gson.toJson(command));
      } catch (IOException e) {
         throw new ResponseException(errCode, String.format("[%d] %s", errCode, e.getMessage()));
      }
   }

   public void leaveGame(String authToken, int gameID) throws ResponseException {
      try {
         var command = new LeaveCommand(authToken, gameID);
         this.session.getBasicRemote().sendText(gson.toJson(command));
      } catch (IOException e) {
         throw new ResponseException(errCode, String.format("[%d] %s", errCode, e.getMessage()));
      }
   }

   public void resign(String authToken, int gameID) throws ResponseException {
      try {
         var command = new ResignCommand(authToken, gameID);
         this.session.getBasicRemote().sendText(gson.toJson(command));
      } catch (IOException e) {
         throw new ResponseException(errCode, String.format("[%d] %s", errCode, e.getMessage()));
      }
   }
}
