package client.ui;

import chess.ChessGame;
import client.ChessClient;
import game.ChsGame;
import models.Game;

import java.util.Scanner;

import static client.ui.EscapeSequences.*;

public class GameRepl {
   private final ChessClient client;
   private final ChessBoardDisplay display = new ChessBoardDisplay();

   public GameRepl(ChessClient client) {
      this.client = client;
   }

   public void run(Game game) {
      ChsGame chess = (ChsGame) game.getGameData();
      String input = "";
      String start = String.format(SET_TEXT_COLOR_GREEN + """
              Welcome to %s, %s %s
              Enter [q]uit to return
              """, game.getGameName(), client.getUsername(), SET_TEXT_COLOR_WHITE);

      System.out.print(start);

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);
         System.out.print(RESET_BG_COLOR + "\n");

         display.displayBoard(chess, ChessGame.TeamColor.WHITE);
         System.out.print(RESET_BG_COLOR + "\n");
         display.displayBoard(chess, ChessGame.TeamColor.BLACK);

         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();

         System.out.print(ERASE_SCREEN);
      }
   }
}
