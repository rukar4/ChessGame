package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
   private final int gameID;
   private final String username;
   private final ChessGame.TeamColor color;

   public JoinPlayerCommand(String authToken, int gameID, String username, ChessGame.TeamColor color) {
      super(authToken);
      this.commandType = CommandType.JOIN_PLAYER;

      this.gameID = gameID;
      this.username = username;
      this.color = color;
   }

   public int getGameID() {
      return gameID;
   }

   public String getUsername() {
      return username;
   }

   public ChessGame.TeamColor getColor() {
      return color;
   }
}
