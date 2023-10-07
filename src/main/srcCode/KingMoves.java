package srcCode;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMoves extends Piece {

   public KingMoves(ChessGame.TeamColor color) {
      super(color, PieceType.KING);
   }

   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
      Collection<Position> possiblePositions = getPositions(start);

      for (Position position : possiblePositions) {
         if (!position.isInBounds()) continue;
         if (board.getPiece(position) != null) {
            if (board.getPiece(position).getTeamColor() == color) continue;
         }
         moves.add(new Move(start, position));
      }

      return moves;
   }

   private static Collection<Position> getPositions(ChessPosition start) {
      int row = start.getRow();
      int col = start.getColumn();
      Collection<Position> possibleMoves = new HashSet<>();

      Position up = new Position(row + 1, col);
      Position down = new Position(row - 1, col);

      possibleMoves.add(up);
      possibleMoves.add(down);

      for (int i = row - 1; i <= row + 1; ++i) {
         Position right = new Position(i, col + 1);
         Position left = new Position(i, col - 1);
         possibleMoves.add(right);
         possibleMoves.add(left);
      }
      return possibleMoves;
   }
}
