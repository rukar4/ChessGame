package client.ui;

import chess.ChessGame;
import client.ChessClient;
import client.webSocket.NotificationHandler;
import game.ChsGame;
import models.Game;
import webSocketMessages.serverMessages.Notification;

import java.util.Scanner;

import static client.ui.EscapeSequences.*;

public class GameRepl implements NotificationHandler {
   private final ChessClient client;
   private final ChessBoardDisplay display = new ChessBoardDisplay();

   public GameRepl(ChessClient client) {
      client.setNotificationHandler(this);
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

   @Override
   public void notify(Notification notification) {
      System.out.println(SET_TEXT_COLOR_GREEN + notification.getMessage());
      System.out.print(SET_TEXT_COLOR_WHITE);
   }
}
