package srcCode;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMoves extends Piece {
   public BishopMoves(ChessGame.TeamColor color) {
      super(color, PieceType.BISHOP);
   }

   @Override
   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
      int row = start.getRow();
      int col = start.getColumn();
      for (int i = 1; row + i <= 8 && col + i <= 8; ++i) {
         Position next = new Position(row + i, col + i);
         if (board.getPiece(next) != null) {
            // If the piece is the opposite color, include space in valid moves
            if (board.getPiece(next).getTeamColor() != color) {
               moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      for (int i = 1; row + i <= 8 && col - i >= 1; ++i) {
         Position next = new Position(row + i, col - i);
         if (board.getPiece(next) != null) {
            // If the piece is the opposite color, include space in valid moves
            if (board.getPiece(next).getTeamColor() != color) {
               moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      for (int i = 1; row - i >= 1 && col + i <= 8; ++i) {
         Position next = new Position(row - i, col + i);
         if (board.getPiece(next) != null) {
            // If the piece is the opposite color, include space in valid moves
            if (board.getPiece(next).getTeamColor() != color) {
                  moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      for (int i = 1; row - i >= 1 && col - i >= 1; ++i) {
         Position next = new Position(row - i, col - i);
         if (board.getPiece(next) != null) {
            // If the piece is the opposite color, include space in valid moves
            if (board.getPiece(next).getTeamColor() != color) {
                  moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      return moves;
   }
}
