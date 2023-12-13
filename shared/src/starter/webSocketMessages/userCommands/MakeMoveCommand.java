package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
   private final int gameID;
   private final ChessMove move;

   public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
      super(authToken);
      this.commandType = CommandType.MAKE_MOVE;
      this.gameID = gameID;
      this.move = move;
   }

   public int getGameID() {
      return gameID;
   }

   public ChessMove getMove() {
      return move;
   }
}
