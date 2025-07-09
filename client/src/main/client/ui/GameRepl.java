package client.ui;

import client.ChessClient;
import client.webSocket.ServerMessageHandler;
import game.ChsGame;
import game.Move;
import game.Position;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static client.ui.EscapeSequences.*;

public class GameRepl implements ServerMessageHandler {
   private final ChessClient client;
   private final ChessBoardDisplay display;
   private Game game;
   private ChsGame chess;
   private boolean justEntered = true;

   public GameRepl(ChessClient client) {
      client.setNotificationHandler(this);
      this.client = client;
      this.display = new ChessBoardDisplay(client);
   }

   public void run() {
      String input = "";

      while (!(input.equalsIgnoreCase("l") || input.equalsIgnoreCase("Leave"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);
         System.out.print(RESET_BG_COLOR + "\n");

         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();

         switch (input.toLowerCase()) {
            case "r", "redraw":
               display.displayBoard(chess, null);
               break;
            case "s", "show", "show moves", "moves":
               System.out.printf("Enter the position in this format: %sd2%s\n\t", SET_TEXT_COLOR_GREEN, SET_TEXT_COLOR_WHITE);
               input = scanner.nextLine();

               System.out.printf("Highlighting: %s\n", input);

               try {
                  Position position = parsePosition(input);
                  display.displayBoard(chess, position);
               } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                  System.out.println(SET_TEXT_COLOR_RED + e.getMessage());
                  System.out.print(SET_TEXT_COLOR_WHITE);
               }
               break;
            case "m", "move", "make move":
               System.out.printf("Enter the move in this format: %sd2 d4%s\n\t", SET_TEXT_COLOR_GREEN, SET_TEXT_COLOR_WHITE);
               String startString = scanner.next();
               String endString = scanner.next();

               Position start = parsePosition(startString);
               Position end = parsePosition(endString);

               client.makeMove(game.getGameID(), new Move(start, end));
               break;
            case "x", "resign":
               System.out.print(SET_TEXT_COLOR_RED + "Are you sure you want to resign? Type yes to confirm:\n\t");
               input = scanner.nextLine();
               if (input.equalsIgnoreCase("yes")) {
                  client.resign(game.getGameID());
                  break;
               } else {
                  System.out.print(ERASE_SCREEN);
                  display.displayBoard(chess, null);
               }
            case "h", "help":
               System.out.printf("""
                       Use the following commands to play chess!
                                              
                       \tRedraw >>> Redraws the chess board.
                       \tShow Moves >>> Highlights the valid moves for a piece.
                       \t\t\tSelect the piece by entering its position on the board. Ex: d2
                       \tMove >>> Move a piece by entering the starting and ending positions.
                       \t\t\tFormat your moves like this: %sd2 d4%s
                       \t\t\tThe first is your piece's starting position, and the second is
                       \t\t\twhere you want to move to.
                       \tResign [X] >>> Forfeits the game to your opponent.
                       \tLeave >>> Return to the previous menu.
                                              
                       You can also use the first letter to select a command, except for resign
                       where X is the shortcut.
                       """, SET_TEXT_COLOR_GREEN, SET_TEXT_COLOR_WHITE);
               break;
            case "l":
               System.out.print(ERASE_SCREEN);
               break;
            default:
               System.out.print("""
                       The command you gave was unrecognized. Please type one of the following:
                                              
                       \tRedraw >>> Redraws the chess board.
                       \tShow Moves >>> Highlights the valid moves for a piece.
                       \t\t\tSelect the piece by entering its position on the board. Ex: d2
                       \tMove >>> Move a piece by entering the starting and ending positions.
                       \t\t\tFormat your moves like this: %sd2 d4%s
                       \t\t\tThe first is your piece's starting position, and the second is
                       \t\t\twhere you want to move to.
                       \tResign [X] >>> Forfeits the game to your opponent.
                       \tLeave >>> Return to the previous menu.
                                              
                       You can also use the first letter to select a command, except for resign
                       where X is the shortcut.
                       """);
               break;
         }
      }
      client.setPlayerColor(null);
      client.leaveGame(game.getGameID());
   }

   private void updateGameState(Game game) {
      this.game = game;
      chess = game.getGameData();
   }

   private Position parsePosition(String input) {
      if (input.length() != 2) throw new IllegalArgumentException("Invalid input length");
      char fileChar = input.charAt(0);
      char rankChar = input.charAt(1);

      int file = fileChar - 'a' + 1;
      int rank = Character.getNumericValue(rankChar);

      return new Position(rank, file);
   }

   @Override
   public void displayMessage(ServerMessage serverMessage) {
      switch (serverMessage.getServerMessageType()) {
         case NOTIFICATION:
            System.out.println(SET_TEXT_COLOR_GREEN + serverMessage.getMessage());
            System.out.print(SET_TEXT_COLOR_WHITE);
            break;
         case ERROR:
            System.out.println(SET_TEXT_COLOR_RED + serverMessage.getErrorMessage());
            System.out.print(SET_TEXT_COLOR_WHITE);
            break;
         case LOAD_GAME:
            updateGameState(serverMessage.getGame());

            if (justEntered) {
               display.displayStart(game.getGameName());
               justEntered = false;
            }

            display.displayBoard(chess, null);
            switch (chess.getStatus()) {
               case ONGOING:
                  display.displayGameUI(game.getGameID(), chess);
                  break;
               case CHECKMATE:
                  System.out.printf("%s won by checkmate!\n", chess.getVictor());
                  break;
               case STALEMATE:
                  System.out.println("The game ended in stalemate.");
                  break;
               case RESIGN:
                  System.out.printf("%s resigned. %s WINS!\n", game.getPlayerFromColor(chess.getVictor()), chess.getVictor());
                  break;
            }

      }
   }
}
