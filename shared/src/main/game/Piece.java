package game;

import chess.*;

import java.util.Collection;

public abstract class Piece implements ChessPiece {
   ChessGame.TeamColor color;
   PieceType piece;
   boolean hasMoved = false;

   public Piece(ChessGame.TeamColor color, PieceType piece) {
      this.color = color;
      this.piece = piece;
   }

   public Piece(Piece otherPiece) {
      this.color = otherPiece.color;
      this.piece = otherPiece.piece;
      this.hasMoved = otherPiece.hasMoved;
   }

   @Override
   public ChessGame.TeamColor getTeamColor() {
      return color;
   }

   @Override
   public PieceType getPieceType() {
      return piece;
   }

   @Override
   public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

   public void setHasMoved(boolean hasMoved) {
      this.hasMoved = hasMoved;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (obj instanceof Piece objPiece) {
         return hasMoved == objPiece.hasMoved;
      } else return false;
   }
}
