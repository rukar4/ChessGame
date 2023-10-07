package srcCode;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoves extends Piece {
   public PawnMoves(ChessGame.TeamColor color) {
      super(color, PieceType.PAWN);
   }

   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
      int row = start.getRow();
      int col = start.getColumn();
      int direction = 1;
      Collection<Position> possiblePositions = new HashSet<>();

      if (color == ChessGame.TeamColor.BLACK) {
         direction = -1;
         if (start.getRow() != 7) hasMoved = true;
      } else {
         if (start.getRow() != 2) hasMoved = true;
      }

      for (int i = -1; i <= 1; ++i) {
         Position next = new Position(row + direction, col + i);
         if (i == 0) {
            if (board.getPiece(next) == null) {
               possiblePositions.add(next);
               if (!hasMoved) {
                  Position twoSpace = new Position(row + 2 * direction, col);
                  if (board.getPiece(twoSpace) == null) possiblePositions.add(twoSpace);
               }
            }
         } else if (next.isInBounds() && board.getPiece(next) != null && board.getPiece(next).getTeamColor() != color) {
            possiblePositions.add(next);
         }
      }

      for (Position position : possiblePositions) {
         if (!position.isInBounds()) continue;
         if ((position.row == 8 && color == ChessGame.TeamColor.WHITE) || (position.row == 1 && color == ChessGame.TeamColor.BLACK)){
            moves.add(new Move(start, position, PieceType.QUEEN));
            moves.add(new Move(start, position, PieceType.KNIGHT));
            moves.add(new Move(start, position, PieceType.ROOK));
            moves.add(new Move(start, position, PieceType.BISHOP));
            continue;
         }
         moves.add(new Move(start, position));
      }

      return moves;
   }
}
