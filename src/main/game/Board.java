package game;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class Board implements ChessBoard {
   int BOARD_LENGTH = 8;
   ChessPiece[][] chessBoard = new ChessPiece[BOARD_LENGTH][BOARD_LENGTH];

   public Board(ChessBoard board) {
      for (int i = 1; i <= BOARD_LENGTH; ++i) {
         for (int j = 1; j <= BOARD_LENGTH; ++j) {
            Position position = new Position(i, j);
            Piece piece = (Piece) board.getPiece(position);
            if (piece != null) {
               Piece copyPiece = createNewPiece(piece.getPieceType(), piece.getTeamColor());
               assert copyPiece != null;
               copyPiece.setHasMoved(piece.hasMoved);
               addPiece(position, copyPiece);
            }
         }
      }
   }

   public Board() {
   }

   @Override
   public void addPiece(ChessPosition position, ChessPiece piece) {
      int[] pieceIndex = getPieceIndex(position);

      chessBoard[pieceIndex[0]][pieceIndex[1]] = piece;
   }

   public Piece createNewPiece(ChessPiece.PieceType pieceType, ChessGame.TeamColor color) {
      switch (pieceType) {
         case PAWN -> {
            return new PawnMoves(color);
         }
         case ROOK -> {
            return new RookMoves(color);
         }
         case BISHOP -> {
            return new BishopMoves(color);
         }
         case KNIGHT -> {
            return new KnightMoves(color);
         }
         case KING -> {
            return new KingMoves(color);
         }
         case QUEEN -> {
            return new QueenMoves(color);
         }
      }
      return null;
   }

   @Override
   public ChessPiece getPiece(ChessPosition position) {
      int[] pieceIndex = getPieceIndex(position);

      return chessBoard[pieceIndex[0]][pieceIndex[1]];
   }

   private int[] getPieceIndex(ChessPosition position) {
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

   private void resetHelper(ChessGame.TeamColor color) {
      Position position = new Position(1, 1);
      int row;
      if (color == ChessGame.TeamColor.WHITE) {
         row = 1;
      } else {
         // If black, set row to 7 to begin loop
         row = 7;
      }
      for (int i = row; i <= row + 1; ++i) {
         position.setRow(i);
         for (int column = 1; column <= BOARD_LENGTH; ++column) {
            position.setColumn(column);
            if (position.getRow() == 2 || position.getRow() == 7) {
               addPiece(position, new PawnMoves(color));
            } else {
               switch (position.getColumn()) {
                  case 1, 8:
                     addPiece(position, new RookMoves(color));
                     break;
                  case 2, 7:
                     addPiece(position, new KnightMoves(color));
                     break;
                  case 3, 6:
                     addPiece(position, new BishopMoves(color));
                     break;
                  case 4:
                     addPiece(position, new QueenMoves(color));
                     break;
                  case 5:
                     addPiece(position, new KingMoves(color));
                     break;
               }
            }
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int row = 0; row < BOARD_LENGTH; ++row) {
         sb.append('|');
         for (int col = 0; col < BOARD_LENGTH; ++col) {
            StringBuilder pieceString = new StringBuilder();
            if (chessBoard[row][col] == null) {
               sb.append("   |");
            } else {
               switch (chessBoard[row][col].getPieceType()) {
                  case ROOK:
                     pieceString.append(" r |");
                     break;
                  case KNIGHT:
                     pieceString.append(" n |");
                     break;
                  case BISHOP:
                     pieceString.append(" b |");
                     break;
                  case KING:
                     pieceString.append(" k |");
                     break;
                  case QUEEN:
                     pieceString.append(" q |");
                     break;
                  case PAWN:
                     pieceString.append(" p |");
                     break;
               }
               if (chessBoard[row][col].getTeamColor() == ChessGame.TeamColor.WHITE) {
                  sb.append(pieceString.toString().toUpperCase());
               } else {
                  sb.append(pieceString);
               }
            }
         }
         sb.append("\n");
      }
      return sb.toString();
   }

   public int getLength() {
      return BOARD_LENGTH;
   }
}
