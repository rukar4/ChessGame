package srcCode;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class Move implements ChessMove {
   Position position;
   Position endPosition;
   ChessPiece.PieceType piece = null;

   public Move(ChessPosition position, ChessPosition endPosition) {
      this.position = (Position) position;
      this.endPosition = (Position) endPosition;
   }

   public Move(ChessPosition position, ChessPosition endPosition, ChessPiece.PieceType piece) {
      this.position = (Position) position;
      this.endPosition = (Position) endPosition;
      this.piece = piece;
   }

   @Override
   public ChessPosition getStartPosition() {
      return position;
   }

   @Override
   public ChessPosition getEndPosition() {
      return endPosition;
   }

   @Override
   public ChessPiece.PieceType getPromotionPiece() {
      return piece;
   }

   @Override
   public int hashCode() {
      return position.col * position.row + endPosition.col * endPosition.row;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (obj instanceof ChessMove) {
         Move objMove = (Move) obj;
         return objMove.position == position
                 && objMove.endPosition.getRow() == endPosition.getRow()
                 && objMove.endPosition.getColumn() == endPosition.getColumn()
                 && objMove.piece == piece;
      } else return false;
   }
}
