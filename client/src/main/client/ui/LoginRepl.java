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
      String start = RESET_BG_COLOR + SET_TEXT_COLOR_WHITE + """
              Chess Project UI: 1.0.0
              \t[L] Login
              \t[R] Register
              \t[Q] Quit
              \t[H] Help
                            
              """;

      System.out.print(ERASE_SCREEN);

      System.out.print(start);

      String username;
      String password;
      String email;

      while (!(input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("Quit"))) {
         System.out.print(SET_TEXT_COLOR_WHITE);

         Scanner scanner = new Scanner(System.in);
         input = scanner.nextLine();


         switch (input.toLowerCase()) {
            case "q", "quit":
               System.out.print(ERASE_SCREEN);
               break;
            case "l", "login":
               System.out.print("Username:\n\t");
               username = scanner.nextLine();

               System.out.print("Password:\n\t");
               password = scanner.nextLine();

               System.out.print(client.login(username, password));

               if (client.isSignedIn()) {
                  SelectGameRepl selectRepl = new SelectGameRepl(client);
                  selectRepl.run();
               }

               System.out.print(start);

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
                  SelectGameRepl selectRepl = new SelectGameRepl(client);
                  selectRepl.run();
               }

               System.out.print(start);

               break;
            case "h", "help":
               System.out.print("""
                       You can use each of the following commands in the login menu.
                       You can also execute a command by typing its first letter.
                       Once you are logged in, you can begin a chess game!
                                              
                       \tLogin >>> Log in as an existing user
                       \tRegister >>> Register a new user
                       \tQuit >>> Exit the program
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
