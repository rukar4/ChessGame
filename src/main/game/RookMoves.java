package game;

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
      int direction = -1;
      // Iterate over rows and columns until the end of the row or until a piece is found
      while (direction != 0) {
         for (int i = col + direction; i <= 8 && i >= 1; i += direction) {
            Position next = new Position(row, i);
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
         for (int i = row + direction; i <= 8 && i >= 1; i += direction) {
            Position next = new Position(i, col);
            if (board.getPiece(next) != null) {
               if (board.getPiece(next).getTeamColor() != color) {
                  moves.add(new Move(start, next));
               }
               break;
            } else {
               moves.add(new Move(start, next));
            }
         }
         switch (direction) {
            // Switch the direction to forward after first loop and 0 after second
            case -1 -> direction = 1;
            case 1 -> direction = 0;
         }
      }
      return moves;
   }
}
