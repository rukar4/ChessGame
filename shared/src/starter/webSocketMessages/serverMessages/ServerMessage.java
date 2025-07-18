package webSocketMessages.serverMessages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Game;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
   ServerMessageType serverMessageType;
   private String message;
   private String errorMessage;
   private Game game;

   public ServerMessage(ServerMessageType type) {
      this.serverMessageType = type;
   }

   public ServerMessageType getServerMessageType() {
      return this.serverMessageType;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   public Game getGame() {
      return game;
   }

   public void setGame(Game game) {
      this.game = game;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!(o instanceof ServerMessage that))
         return false;
      return getServerMessageType() == that.getServerMessageType();
   }

   @Override
   public int hashCode() {
      return Objects.hash(getServerMessageType());
   }

   public String toString() {
      Gson gson = new GsonBuilder()
              .excludeFieldsWithModifiers(Modifier.STATIC)
              .create();
      return gson.toJson(this);
   }

   public enum ServerMessageType {
      LOAD_GAME,
      ERROR,
      NOTIFICATION
   }
}
