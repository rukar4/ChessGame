package srcCode;

import chess.ChessPosition;

public class Castle extends Move {
   Position rookStart;
   Position rookEnd;

   public Castle(ChessPosition position, ChessPosition endPosition, ChessPosition rookStart, ChessPosition rookEnd) {
      super(position, endPosition);
      this.rookStart = (Position) rookStart;
      this.rookEnd = (Position) rookEnd;
   }

   public Position getRookStart() {
      return rookStart;
   }

   public Position getRookEnd() {
      return rookEnd;
   }
}
