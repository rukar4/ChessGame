package srcCode;

import chess.ChessMove;
import chess.ChessPosition;

public class Castle extends Move {
   int QUEEN_CASTLE_COL = 3;
   int KING_CASTLE_COL = 7;
   Position rookStart;
   Position rookEnd;
   int row;

   CastleType type;

   enum CastleType {
      QUEEN_SIDE,
      KING_SIDE
   }

   public Castle(ChessPosition position, ChessPosition endPosition) {
      super(position, endPosition);
      row = endPosition.getRow();
      // Check for side
      if (endPosition.getColumn() == QUEEN_CASTLE_COL) {
         type = CastleType.QUEEN_SIDE;
         rookStart = new Position(row, 1);
         rookEnd = new Position(row, QUEEN_CASTLE_COL + 1);
      } else {
         type = CastleType.KING_SIDE;
         rookStart = new Position(row, 8);
         rookEnd = new Position(row, KING_CASTLE_COL - 1);
      }
   }

   public Position getRookStart() {
      return rookStart;
   }

   public Position getRookEnd() {
      return rookEnd;
   }
}
