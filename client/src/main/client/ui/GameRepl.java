package client.ui;

import client.ChessClient;
import game.ChsGame;
import models.Game;

import java.util.Scanner;

import static client.ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GameRepl {
   private final ChessClient client;

   public GameRepl(ChessClient client) {
      this.client = client;
   }

   public void run(Game game) {
      ChsGame chess = (ChsGame) game.getGameData();
      String input = "";
      String start = String.format(SET_TEXT_COLOR_WHITE + """
              Welcome to %s, %s
              Enter [q]uit to return
              """, game.getGameName(), client.getUsername());

      System.out.print(start);
      System.out.println(chess.toString());

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);

         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();
      }
   }
}
