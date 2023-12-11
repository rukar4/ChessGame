package webSocketMessages.serverMessages;

import com.google.gson.Gson;

public class Error extends ServerMessage {
   private final String errorMessage;

   public Error(String error) {
      super(ServerMessageType.ERROR);
      this.errorMessage = String.format("[500] WebSocket Error: %s", error);
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   public String toString() {
      return new Gson().toJson(this);
   }
}
