package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinCommand extends UserGameCommand {
   private final int gameID;
   private final ChessGame.TeamColor color;

   public JoinCommand(String authToken, int gameID, ChessGame.TeamColor color) {
      super(authToken);
      this.gameID = gameID;
      this.color = color;

      if (color == null) this.commandType = CommandType.JOIN_PLAYER;
      else this.commandType = CommandType.JOIN_OBSERVER;

   }

   public int getGameID() {
      return gameID;
   }

   public ChessGame.TeamColor getColor() {
      return color;
   }
}
