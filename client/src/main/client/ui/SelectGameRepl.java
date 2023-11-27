package client.ui;

import client.ChessClient;

import java.util.Scanner;

import static client.ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class SelectGameRepl {
   private final ChessClient client;

   public SelectGameRepl(ChessClient client) {
      this.client = client;
   }

   public void run() {
      String input = "";
      String start = SET_TEXT_COLOR_WHITE + """
              Execute one of the commands below to get started:
              \t[C] Create Game
              \t[J] Join Game
              \t[R] Return to Game
              \t[L] List Games
              \t[W] Watch Game
              \t[Q] Quit (Logout)
              \t[H] Help
              """;

      System.out.print(start);

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);

         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();

         int gameID;

         switch (input.toLowerCase()) {
            case "q", "quit", "logout":
               System.out.println(client.logout(client.getAuthToken()));
               break;
            case "c", "create", "create game":
               System.out.print("Enter a name for your game:\n\t");
               String gameName = scanner.nextLine();

               System.out.println(client.createGame(gameName));
               break;
            case "j", "join", "join game":
               System.out.print("Enter the game ID:\n\t");
               gameID = Integer.parseInt(scanner.nextLine());

               System.out.print("Enter the color to join as (white or black):\n\t");
               String color = scanner.nextLine();

               client.joinGame(color, gameID);

               System.out.print(start);
               break;
            case "l", "list", "list games":
               System.out.println(client.listGames());
               break;
            case "w", "watch", "o", "observe", "watch game":
               System.out.print("Enter the game ID:\n\t");
               gameID = Integer.parseInt(scanner.nextLine());

               client.joinGame("", gameID);

               System.out.print(start);
               break;
            case "r", "return":
               System.out.print("Enter the game ID:\n\t");
               gameID = Integer.parseInt(scanner.nextLine());

               client.rejoinGame(gameID);

               System.out.print(start);
               break;
            case "h", "help":
               System.out.print("""
                       You can use each of the following commands in the start menu.
                       You can also execute a command by typing its first letter.
                                              
                       \tList Games >>> List all existing games
                       \tCreate Game >>> Create a new game
                       \tJoin Game >>> Join a game as a player
                       \tReturn to Game >>> Rejoin a game you have started
                       \tWatch Game >>> Watch a game as an observer
                       \tQuit (Logout) >>> Logout and return to the login menu
                       """);
               break;
            default:
               System.out.print("""
                       The command you gave was unrecognized. Please type one of the following:
                                              
                       \tList Games >>> List all existing games
                       \tCreate Game >>> Create a new game
                       \tJoin Game >>> Join a game as a player
                       \tReturn to Game >>> Rejoin a game you have started
                       \tWatch Game >>> Watch a game as an observer
                       \tQuit (Logout) >>> Logout and return to the login menu
                       """);
         }
      }
   }
}
