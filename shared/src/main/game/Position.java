package game;

import chess.ChessPosition;

public class Position implements ChessPosition {
   int row;
   int col;

   public Position(int row, int col) {
      this.row = row;
      this.col = col;
   }

   public boolean isInBounds() {
      return row > 0 && row <= 8 && col > 0 && col <= 8;
   }

   @Override
   public int getRow() {
      return row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   @Override
   public int getColumn() {
      return col;
   }

   public void setColumn(int column) {
      this.col = column;
   }

   @Override
   public int hashCode() {
      return row * col % 13;
   }

   @Override
   public String toString() {
      char letter = (char) ('a' + col - 1);
      return letter + String.valueOf(row);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) return true;
      if (obj instanceof Position objPos) {
         return objPos.col == col && objPos.row == row;
      } else {
         return false;
      }
   }
}
