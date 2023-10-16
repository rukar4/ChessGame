package game;

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

      addCastles(board, start, 1);
      addCastles(board, start, 8);

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

   private void addCastles(ChessBoard board, ChessPosition start, int rookCol){
      if (hasMoved) return;

      // Check for valid rook piece
      int row = start.getRow();
      Position rookHome = new Position(row, rookCol);
      Piece rook = (Piece) board.getPiece(rookHome);
      if (rook == null || rook.hasMoved || rook.getPieceType() != PieceType.ROOK || rook.getTeamColor() != color) return;

      // Initialize the rest of variables
      int kingCol = start.getColumn();
      int direction = 1;
      if (rookCol == 1) direction = -1;

      for (int i = 1; i <= 2; ++i){
         if (board.getPiece(new Position(row, kingCol + direction * i)) != null) return;
      }
      moves.add(new Castle(start, new Position(row, kingCol + direction * 2)));
   }
}
