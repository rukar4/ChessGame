package srcCode;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class RookMoves extends Piece {
   public RookMoves(ChessGame.TeamColor color) {
      super(color, PieceType.ROOK);
   }

   @Override
   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
      int row = start.getRow();
      int col = start.getColumn();




      // Iterate over rows and columns until the end of the row or until a piece is found
      for (int i = col + 1; i <= 8; ++i){
         Position next = new Position(row, i);
         if (board.getPiece(next) != null) {
            // If the piece is the opposite color, include space in valid moves
            if (board.getPiece(next).getTeamColor() != color){
               moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      for (int i = col - 1; i >= 1; --i){
         Position next = new Position(row, i);
         if (board.getPiece(next) != null) {
            if (board.getPiece(next).getTeamColor() != color){
               moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      for (int i = row + 1; i <= 8; ++i){
         Position next = new Position(i, col);
         if (board.getPiece(next) != null) {
            if (board.getPiece(next).getTeamColor() != color){
               moves.add(new Move(start, next));
            }
            break;
         } else {
            moves.add(new Move(start, next));
         }
      }
      for (int i = row - 1; i >= 1; --i){
         Position next = new Position(i, col);
         if (board.getPiece(next) != null) {
            if (board.getPiece(next).getTeamColor() != color){
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
