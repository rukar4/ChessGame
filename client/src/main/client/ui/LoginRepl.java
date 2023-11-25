package client.ui;

import client.ChessClient;

import java.util.Scanner;

import static client.ui.EscapeSequences.*;

public class LoginRepl {
   private final ChessClient client;

   public LoginRepl(String serverUrl) {
      client = new ChessClient(serverUrl);
   }

   public void run() {
      String input = "";

      System.out.print("""
              Chess Project UI: 1.0.0
              \t[L] Login
              \t[R] Register
              \t[Q] Quit
              \t[H] Help
              """);

      String username;
      String password;
      String email;

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();
         System.out.print(SET_TEXT_COLOR_WHITE);

         switch (input.toLowerCase()) {
            case "q", "quit":
               break;
            case "l", "login":
               System.out.print("Username:\n\t");
               username = scanner.nextLine();

               System.out.print("Password:\n\t");
               password = scanner.nextLine();

               System.out.print(client.login(username, password));

               //TODO: Start next REPL loop

               break;
            case "r", "register":
               System.out.print("Username:\n\t");
               username = scanner.nextLine();

               System.out.print("Password:\n\t");
               password = scanner.nextLine();

               System.out.print("Email:\n\t");
               email = scanner.nextLine();

               System.out.print(client.register(username, password, email));

               if (client.isSignedIn()) {
                  //TODO: Start next REPL loop
               }
               break;
            case "h", "help":
               System.out.print("""
                       You can use each of the following commands in the start menu.
                       You can also execute a command by typing its first letter.
                       Once you are logged in, you can begin a chess game!
                       \tLogin >>> log in as an existing user
                       \tRegister >>> register a new user
                       \tQuit >>> exit the program
                       """);
               break;
            default:
               System.out.print("""
                       The command you gave was unrecognized. Please type one of the following:
                       \tLogin >>> log in as an existing user
                       \tRegister >>> register a new user
                       \tQuit >>> exit the program
                       \tHelp >>> open help menu
                       """);
         }
      }
   }
}
