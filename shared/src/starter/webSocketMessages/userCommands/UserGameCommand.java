package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

   private final String authToken;
   protected CommandType commandType;

   public UserGameCommand(String authToken) {
      this.authToken = authToken;
   }

   public String getAuthString() {
      return authToken;
   }

   public CommandType getCommandType() {
      return this.commandType;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!(o instanceof UserGameCommand that))
         return false;
      return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
   }

   @Override
   public int hashCode() {
      return Objects.hash(getCommandType(), getAuthString());
   }

   public enum CommandType {
      JOIN_PLAYER,
      JOIN_OBSERVER,
      MAKE_MOVE,
      LEAVE,
      RESIGN
   }
}
