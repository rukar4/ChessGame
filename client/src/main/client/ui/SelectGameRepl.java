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
              \t[L] List Games
              \t[W] Watch Game
              \t[Q] Quit (Logout)
              \t[H] Help
              """;

      System.out.print(start);

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();
         System.out.print(SET_TEXT_COLOR_WHITE);

         switch (input.toLowerCase()) {
            case "q", "quit", "logout":
               System.out.println(client.logout(client.getAuthToken()));
               break;
            case "c", "create", "create game":
               System.out.print("Enter a name for your game:\n\t");
               String gameName = scanner.nextLine();

               System.out.println(client.createGame(gameName));
               break;
            case "r", "register":

               break;
            case "h", "help":
               System.out.print("""
                       You can use each of the following commands in the start menu.
                       You can also execute a command by typing its first letter.
                                              
                       \tList Games >>> List all existing games
                       \tCreate Game >>> Create a new game
                       \tJoin Game >>> Join a game as a player
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
                       \tWatch Game >>> Watch a game as an observer
                       \tQuit (Logout) >>> Logout and return to the login menu
                       """);
         }
      }
   }
}
