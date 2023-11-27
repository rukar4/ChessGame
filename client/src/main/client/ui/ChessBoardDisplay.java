package client.ui;

import chess.ChessGame;
import chess.ChessPiece;
import game.Board;
import game.ChsGame;
import game.Position;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static client.ui.EscapeSequences.*;

public class ChessBoardDisplay {
   private static final int BOARD_SIZE_IN_SQUARES = 8;
   private static final int SQUARE_SIZE_IN_CHARS = 3;

   public void displayBoard(ChsGame game, ChessGame.TeamColor color) {
      var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

      Board chessBoard = (Board) game.getBoard();

      drawLetterRow(out, color);

      drawChessBoard(out, chessBoard, color);

      drawLetterRow(out, color);

      out.print(SET_TEXT_COLOR_WHITE);
   }

   private void drawLetterRow(PrintStream out, ChessGame.TeamColor color) {
      setGray(out);
      out.print(SET_TEXT_BOLD);

      char[] letters = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', ' '};

      if (color == ChessGame.TeamColor.WHITE) {
         for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES + 2; ++boardCol) {
            drawBorderChar(out, letters[boardCol], true);
         }
      } else {
         for (int boardCol = letters.length - 1; boardCol >= 0; --boardCol) {
            drawBorderChar(out, letters[boardCol], true);
         }
      }
      setBlack(out);
      out.println();
   }

   private void drawBorderChar(PrintStream out, char letter, boolean notEndCol) {
      setGray(out);
      out.print(SET_TEXT_BOLD);

      int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
      int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

      // Move the long space to the end if it is an end column
      if (notEndCol) {
         // Use the long space to format better
         out.print("\u2003");

         prefixLength -= 1;
         out.print(" ".repeat(prefixLength));

         printBorderText(out, letter);

         out.print(" ".repeat(suffixLength));
      } else {
         out.print(" ".repeat(prefixLength));

         printBorderText(out, letter);

         // Use the long space to format better
         out.print("\u2003");
         suffixLength -= 1;
         out.print(" ".repeat(suffixLength));
      }
   }

   private void printBorderText(PrintStream out, char letter) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_DARK_GREY);

      out.print(letter);

      setGray(out);
   }

   private void drawChessBoard(PrintStream out, Board chessBoard, ChessGame.TeamColor color) {
      switch (color) {
         case WHITE:
            for (int row = 8; row > 0; --row) {
               char rowLabel = (char) (row + '0');
               drawBorderChar(out, rowLabel, true);

               for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; ++col) {
                  boolean isLight = (row + col) % 2 == 0;

                  ChessPiece piece = chessBoard.getPiece(new Position(row, col));
                  drawSquare(out, isLight, piece);
               }

               drawBorderChar(out, rowLabel, false);
               setBlack(out);
               out.println();
            }
            break;
         case BLACK:
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; ++row) {
               char rowLabel = (char) (row + '0');
               drawBorderChar(out, rowLabel, true);

               for (int col = 8; col > 0; --col) {
                  boolean isLight = (row + col) % 2 == 0;

                  ChessPiece piece = chessBoard.getPiece(new Position(row, col));
                  drawSquare(out, isLight, piece);
               }

               drawBorderChar(out, rowLabel, false);
               setBlack(out);
               out.println();
            }
      }
   }


   private void drawSquare(PrintStream out, boolean isLight, ChessPiece piece) {
      if (isLight) out.print(SET_BG_COLOR_CREAM);
      else out.print(SET_BG_COLOR_DARK_RED);
      out.print(RESET_TEXT_BOLD_FAINT);

      if (piece == null) out.print(EMPTY);
      else {
         printPiece(out, piece);
      }
   }

   private void printPiece(PrintStream out, ChessPiece piece) {
      switch (piece.getTeamColor()) {
         case WHITE:
            out.print(SET_TEXT_COLOR_MAGENTA);
            switch (piece.getPieceType()) {
               case ROOK:
                  out.print(WHITE_ROOK);
                  break;
               case KNIGHT:
                  out.print(WHITE_KNIGHT);
                  break;
               case BISHOP:
                  out.print(WHITE_BISHOP);
                  break;
               case KING:
                  out.print(WHITE_KING);
                  break;
               case QUEEN:
                  out.print(WHITE_QUEEN);
                  break;
               case PAWN:
                  out.print(WHITE_PAWN);
                  break;
            }
            break;
         case BLACK:
            out.print(SET_TEXT_COLOR_BLACK);
            switch (piece.getPieceType()) {
               case ROOK:
                  out.print(BLACK_ROOK);
                  break;
               case KNIGHT:
                  out.print(BLACK_KNIGHT);
                  break;
               case BISHOP:
                  out.print(BLACK_BISHOP);
                  break;
               case KING:
                  out.print(BLACK_KING);
                  break;
               case QUEEN:
                  out.print(BLACK_QUEEN);
                  break;
               case PAWN:
                  out.print(BLACK_PAWN);
                  break;
            }
            break;
      }
      out.print(SET_TEXT_COLOR_WHITE);
   }

   private void setGray(PrintStream out) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_LIGHT_GREY);
   }

   private void setBlack(PrintStream out) {
      out.print(SET_BG_COLOR_BLACK);
      out.print(SET_TEXT_COLOR_BLACK);
   }
}
