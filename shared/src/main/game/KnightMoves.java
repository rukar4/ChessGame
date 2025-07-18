package game;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoves extends Piece {
   public KnightMoves(ChessGame.TeamColor color) {
      super(color, PieceType.KNIGHT);
   }

   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
      Collection<ChessMove> moves = new HashSet<>();

      int row = start.getRow();
      int col = start.getColumn();
      Collection<Position> possiblePositions = new HashSet<>();

      for (int i = -2; i <= 2; ++i) {
         switch (i) {
            case 0:
               break;
            case -1, 1:
               possiblePositions.add(new Position(row + i, col + 2));
               possiblePositions.add(new Position(row + i, col - 2));
               break;
            case -2, 2:
               possiblePositions.add(new Position(row + i, col + 1));
               possiblePositions.add(new Position(row + i, col - 1));
               break;
         }
      }

      for (Position position : possiblePositions) {
         if (!position.isInBounds()) continue;
         if (board.getPiece(position) != null) {
            if (board.getPiece(position).getTeamColor() == color) continue;
         }
         moves.add(new Move(start, position));
      }

      return moves;
   }
}
