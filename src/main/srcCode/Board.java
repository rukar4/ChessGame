package srcCode;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class Board implements ChessBoard {
   int BOARD_LENGTH = 8;
   ChessPiece[][] chessBoard = new ChessPiece[BOARD_LENGTH][BOARD_LENGTH];

   @Override
   public void addPiece(ChessPosition position, ChessPiece piece) {
      int[] pieceIndex = getPieceIndex(position);

      chessBoard[pieceIndex[0]][pieceIndex[1]] = piece;
   }

   @Override
   public ChessPiece getPiece(ChessPosition position) {
      int[] pieceIndex = getPieceIndex(position);

      return chessBoard[pieceIndex[0]][pieceIndex[1]];
   }

   private int[] getPieceIndex(ChessPosition position){
      // Convert row and column number to array index
      int[] index = new int[2];
      index[0] = -(position.getRow() - BOARD_LENGTH);
      index[1] = position.getColumn() - 1;

      return index;
   }

   @Override
   public void resetBoard() {
      chessBoard = new ChessPiece[BOARD_LENGTH][BOARD_LENGTH];
      resetHelper(ChessGame.TeamColor.WHITE);
      resetHelper(ChessGame.TeamColor.BLACK);
   }

   private void resetHelper(ChessGame.TeamColor color){
      Position position = new Position(1, 1);
      int row;
      if (color == ChessGame.TeamColor.WHITE) {
         row = 1;
      } else {
         // If black, set row to 7 to begin loop
         row = 7;
      }
      for (int i = row; i <= row + 1; ++i){
         position.setRow(i);
         for (int column = 1; column <= BOARD_LENGTH; ++column){
            position.setColumn(column);
            if (position.getRow() == 2 || position.getRow() == 7){
               addPiece(position, new Piece(color, ChessPiece.PieceType.PAWN));
            } else {
               switch (position.getColumn()) {
                  case 1, 8:
                     addPiece(position, new Piece(color, ChessPiece.PieceType.ROOK));
                     break;
                  case 2, 7:
                     addPiece(position, new Piece(color, ChessPiece.PieceType.KNIGHT));
                     break;
                  case 3, 6:
                     addPiece(position, new Piece(color, ChessPiece.PieceType.BISHOP));
                     break;
                  case 4:
                     addPiece(position, new Piece(color, ChessPiece.PieceType.QUEEN));
                     break;
                  case 5:
                     addPiece(position, new Piece(color, ChessPiece.PieceType.KING));
                     break;
               }
            }
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int row = 0; row < BOARD_LENGTH; ++row){
         sb.append('|');
         for (int col = 0; col < BOARD_LENGTH; ++col){
            if (chessBoard[row][col] == null){
               sb.append("   |");
            } else {
               switch (chessBoard[row][col].getPieceType()) {
                  case ROOK:
                     sb.append(" r |");
                     break;
                  case KNIGHT:
                     sb.append(" n |");
                     break;
                  case BISHOP:
                     sb.append(" b |");
                     break;
                  case KING:
                     sb.append(" k |");
                     break;
                  case QUEEN:
                     sb.append(" q |");
                     break;
                  case PAWN:
                     sb.append(" p |");
                     break;
               }
            }
         }
         sb.append("\n");
      }
      return sb.toString();
   }
}
