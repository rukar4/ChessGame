package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinCommand extends UserGameCommand {
   private final int gameID;
   private final String username;
   private final ChessGame.TeamColor color;

   public JoinCommand(String authToken, int gameID, String username, ChessGame.TeamColor color) {
      super(authToken);
      this.gameID = gameID;
      this.username = username;
      this.color = color;

      if (color == null) this.commandType = CommandType.JOIN_PLAYER;
      else this.commandType = CommandType.JOIN_OBSERVER;

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
