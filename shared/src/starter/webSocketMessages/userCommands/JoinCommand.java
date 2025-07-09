package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinCommand extends UserGameCommand {
   private final int gameID;
   private final ChessGame.TeamColor playerColor;

   public JoinCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
      super(authToken);
      this.gameID = gameID;
      this.playerColor = playerColor;

      if (playerColor == null) this.commandType = CommandType.JOIN_PLAYER;
      else this.commandType = CommandType.JOIN_OBSERVER;

   }

   public int getGameID() {
      return gameID;
   }

   public ChessGame.TeamColor getPlayerColor() {
      return playerColor;
   }
}
