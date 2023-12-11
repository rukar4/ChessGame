package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
   private final int gameID;
   private final String username;

   public LeaveCommand(String authToken, int gameID, String username) {
      super(authToken);
      this.commandType = CommandType.LEAVE;
      this.gameID = gameID;
      this.username = username;
   }

   public int getGameID() {
      return gameID;
   }

   public String getUsername() {
      return username;
   }
}
