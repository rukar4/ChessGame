package client.ui;

import chess.ChessGame.TeamColor;
import client.ChessClient;
import client.webSocket.ServerMessageHandler;
import game.ChsGame;
import models.Game;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static client.ui.EscapeSequences.*;

public class GameRepl implements ServerMessageHandler {
   private final ChessClient client;
   private final ChessBoardDisplay display = new ChessBoardDisplay();
   private Game game;
   private ChsGame chess;

   @Override
   public void displayMessage(ServerMessage message) {
      switch (message.getServerMessageType()) {
         case NOTIFICATION:
            Notification notification = (Notification) message;
            System.out.println(SET_TEXT_COLOR_GREEN + notification.getMessage());
            System.out.print(SET_TEXT_COLOR_WHITE);
            break;
         case ERROR:
            Error error = (Error) message;
            System.out.println(SET_TEXT_COLOR_RED + error.getErrorMessage());
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

   public GameRepl(ChessClient client) {
      client.setNotificationHandler(this);
      this.client = client;
   }

   public void run(Game game) {
      this.game = game;
      this.chess = (ChsGame) game.getGameData();

      String input = "";
      String start = String.format(SET_TEXT_COLOR_GREEN + """
              Welcome to %s %s %s
              """, game.getGameName(), client.getUsername(), SET_TEXT_COLOR_WHITE);

      System.out.print(start);

      while (!(input.equalsIgnoreCase("l") || input.equalsIgnoreCase("Leave"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);
         System.out.print(RESET_BG_COLOR + "\n");

         display.displayBoard(chess, client.getPlayerColor());
         if (client.getPlayerColor() == null)
            System.out.printf(
                    SET_TEXT_COLOR_GREEN + "Watching %d\n" +
                    SET_TEXT_COLOR_WHITE + "Team turn: %s\n",
                    game.getGameID(), game.getGameData().getTeamTurn().toString()
            );
         else if (chess.getTeamTurn() == client.getPlayerColor()) {
            System.out.println("What's your next move?");
         } else {
            System.out.printf("Waiting for %s to move...\n", game.getGameData().getTeamTurn().toString().toLowerCase());
         }
         System.out.println("Type [h]elp for command details");


         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();

         System.out.print(ERASE_SCREEN);
      }

      client.setPlayerColor(null);
      client.leaveGame(game.getGameID());
   }

   private void updateGameState() {
      game = client.getCurrentGameState(game.getGameID());
      chess = (ChsGame) game.getGameData();
   }
}
