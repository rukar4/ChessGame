package client.ui;

import chess.ChessGame.TeamColor;
import client.ChessClient;
import client.webSocket.ServerMessageHandler;
import game.ChsGame;
import models.Game;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static client.ui.EscapeSequences.*;

public class GameRepl implements ServerMessageHandler {
   private final ChessClient client;
   private final ChessBoardDisplay display = new ChessBoardDisplay();
   private Game game;
   private ChsGame chess;

   public GameRepl(ChessClient client) {
      client.setNotificationHandler(this);
      this.client = client;
   }

   public void run(Game game) {
      this.game = game;
      this.chess = (ChsGame) game.getGameData();
      String input = "";
      String start = String.format(SET_TEXT_COLOR_GREEN + """
              Welcome to %s, %s %s
              Enter [q]uit to return
              """, game.getGameName(), client.getUsername(), SET_TEXT_COLOR_WHITE);

      System.out.print(start);

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);
         System.out.print(RESET_BG_COLOR + "\n");

         display.displayBoard(chess, client.getPlayerColor());

         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();

         System.out.print(ERASE_SCREEN);
      }

      client.setPlayerColor(null);
   }

   @Override
   public void displayMessage(ServerMessage message) {
      switch (message.getServerMessageType()) {
         case NOTIFICATION:
            Notification notification = (Notification) message;
            System.out.println(SET_TEXT_COLOR_GREEN + notification.getMessage());
            System.out.print(SET_TEXT_COLOR_WHITE);
            break;
         case ERROR:

            System.out.println(SET_TEXT_COLOR_RED + "error");
            System.out.print(SET_TEXT_COLOR_WHITE);
            break;
         case LOAD_GAME:
            if (client.getPlayerColor() == TeamColor.BLACK) {
               System.out.print(ERASE_SCREEN);
               updateGameState();
               display.displayBoard(chess, TeamColor.BLACK);
            } else {
               System.out.print(ERASE_SCREEN);
               display.displayBoard(chess, TeamColor.WHITE);
            }
      }
   }

   private void updateGameState() {
      game = client.getCurrentGameState(game.getGameID());
      chess = (ChsGame) game.getGameData();
   }
}
