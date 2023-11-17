package game;

import chess.ChessPosition;

public class EnPassant extends Move {
   Position pawnToCapture;

   public EnPassant(ChessPosition position, ChessPosition endPosition, Position pawnToCapture) {
      super(position, endPosition);
      this.pawnToCapture = pawnToCapture;
   }
}
