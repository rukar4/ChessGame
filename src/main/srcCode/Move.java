package srcCode;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class Move implements ChessMove {
   Position position;
   Position endPosition;
   ChessPiece.PieceType promotionPiece = null;

   public Move(ChessPosition position, ChessPosition endPosition) {
      this.position = (Position) position;
      this.endPosition = (Position) endPosition;
   }

   public Move(ChessPosition position, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
      this.position = (Position) position;
      this.endPosition = (Position) endPosition;
      this.promotionPiece = promotionPiece;
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
      return promotionPiece;
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
         return objMove.position.getRow() == position.getRow()
                 && objMove.position.getColumn() == position.getColumn()
                 && objMove.endPosition.getRow() == endPosition.getRow()
                 && objMove.endPosition.getColumn() == endPosition.getColumn()
                 && objMove.promotionPiece == promotionPiece;
      } else return false;
   }

   @Override
   public String toString(){
      return position.toString() + " to " + endPosition.toString();
   }
}
