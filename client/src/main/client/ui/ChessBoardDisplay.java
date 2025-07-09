package client.ui;

import chess.ChessGame.TeamColor;
import chess.ChessMove;
import chess.ChessPiece;
import client.ChessClient;
import game.Board;
import game.ChsGame;
import game.Position;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static client.ui.EscapeSequences.*;

public class ChessBoardDisplay {
   private static final int BOARD_SIZE_IN_SQUARES = 8;
   private static final int SQUARE_SIZE_IN_CHARS = 3;
   private final ChessClient client;

   public ChessBoardDisplay(ChessClient client) {
      this.client = client;
   }

   public void displayBoard(ChsGame game, Position position) {
      PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
      Board chessBoard = (Board) game.getBoard();

      TeamColor color = client.getPlayerColor();
      if (color == null) color = TeamColor.WHITE;

      drawLetterRow(out, color);
      drawChessBoard(out, chessBoard, color, position, getMoves(game, position));
      drawLetterRow(out, color);

      out.print(SET_TEXT_COLOR_WHITE);
   }

   private void drawLetterRow(PrintStream out, TeamColor color) {
      setGray(out);
      out.print(SET_TEXT_BOLD);

      char[] letters = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', ' '};

      if (color == TeamColor.WHITE) {
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

   private void drawChessBoard(PrintStream out, Board chessBoard, TeamColor color, Position startPosition, Collection<ChessMove> moves) {
      int startRow, endRow, rowIncrement;

      switch (color) {
         case WHITE:
            startRow = 8;
            endRow = 1;
            rowIncrement = -1;
            break;
         case BLACK:
            startRow = 1;
            endRow = 8;
            rowIncrement = 1;
            break;
         default:
            throw new IllegalArgumentException("Invalid color");
      }

      for (int row = startRow; row != endRow + rowIncrement; row += rowIncrement) {
         char rowLabel = (char) (row + '0');
         drawBorderChar(out, rowLabel, true);

         for (int col = (color == TeamColor.WHITE) ? 1 : 8;
              (color == TeamColor.WHITE) ? col <= BOARD_SIZE_IN_SQUARES : col > 0;
              col += (color == TeamColor.WHITE) ? 1 : -1) {

            Position currentPosition = new Position(row, col);
            ChessPiece piece = chessBoard.getPiece(currentPosition);

            boolean highlight = false;
            boolean isStart = false;

            if (currentPosition.equals(startPosition)) isStart = true;
            else if (moves != null && !moves.isEmpty()) {
               for (ChessMove move : moves) {
                  if (currentPosition.equals(move.getEndPosition())) {
                     moves.remove(move);
                     highlight = true;
                     break;
                  }
               }
            }
            boolean isDark = (row + col) % 2 == 0;
            drawSquare(out, piece, isDark, highlight, isStart);
         }
         drawBorderChar(out, rowLabel, false);
         setBlack(out);
         out.println();
      }
   }


   private void drawSquare(PrintStream out, ChessPiece piece, boolean isDark, boolean highlight, boolean isStart) {
      // Choose background color based on booleans
      if (isStart) out.print(SET_BG_COLOR_YELLOW);
      else if (isDark) {
         if (highlight) out.print(SET_BG_COLOR_DARK_GREEN);
         else out.print(SET_BG_COLOR_DARK_RED);
      } else {
         if (highlight) out.print(SET_BG_COLOR_GREEN);
         else out.print(SET_BG_COLOR_CREAM);
      }
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
            break;
         case BLACK:
            out.print(SET_TEXT_COLOR_BLACK);
            break;
      }

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

      out.print(SET_TEXT_COLOR_WHITE);
   }

   private Collection<ChessMove> getMoves(ChsGame game, Position position) {
      if (position != null) {
         if (!position.isInBounds()) throw new IndexOutOfBoundsException("Position is out of bounds");
         else return game.validMoves(position);
      }
      return null;
   }

   private void setGray(PrintStream out) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_LIGHT_GREY);
   }

   private void setBlack(PrintStream out) {
      out.print(RESET_BG_COLOR);
      out.print(SET_TEXT_COLOR_BLACK);
   }

   public void displayStart(String gameName) {
      String start = String.format(SET_TEXT_COLOR_GREEN + "%s %s\n\n", gameName, SET_TEXT_COLOR_WHITE);
      System.out.print(ERASE_SCREEN + start);
   }

   public void displayGameUI(int gameID, ChsGame chess) {
      if (client.getPlayerColor() == null)
         System.out.printf(
                 SET_TEXT_COLOR_GREEN + "Watching %d\n" +
                         SET_TEXT_COLOR_WHITE + "Team turn: %s\n",
                 gameID, chess.getTeamTurn().toString()
         );
      else if (chess.getTeamTurn() == client.getPlayerColor()) {
         System.out.println("What's your next move?");
      } else {
         System.out.printf("Waiting for %s to move...\n", chess.getTeamTurn().toString().toLowerCase());
      }
      System.out.println("Type [h]elp for command details");
   }
}
