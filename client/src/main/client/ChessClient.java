package client;

import models.Game;
import server.ServerFacade;
import svc.Result;
import svc.account.LoginRequest;
import svc.account.LoginResult;
import svc.account.RegisterRequest;
import svc.game.CreateGameRequest;
import svc.game.CreateGameResult;
import svc.game.JoinGameRequest;
import svc.game.ListGamesResult;

import java.util.List;

import static client.ui.EscapeSequences.*;

public class ChessClient {
   private String username = null;
   private String authToken = null;
   private boolean signedIn = false;
   private final ServerFacade server;

   public ChessClient(String serverUrl) {
      server = new ServerFacade(serverUrl);
   }

   public String login(String username, String password) {
      LoginRequest loginRequest = new LoginRequest(username, password);

      try {
         LoginResult res = server.login(loginRequest);
         this.username = res.getUsername();
         this.authToken = res.getAuthToken();
         signedIn = true;

         return String.format(SET_TEXT_COLOR_GREEN + "Welcome %s.\n", res.getUsername());

      } catch (Exception e) {
         return String.format(SET_TEXT_COLOR_RED + "Login failed:\n %s\n", e.getMessage());
      }
   }

   public String register(String username, String password, String email) {
      RegisterRequest registerRequest = new RegisterRequest(username, password, email);

      try {
         LoginResult res = server.register(registerRequest);

         this.username = res.getUsername();
         this.authToken = res.getAuthToken();

         signedIn = true;

         return String.format(SET_TEXT_COLOR_GREEN + "Register successful! Welcome %s!\n", res.getUsername());

      } catch (Exception e) {
         return String.format(SET_TEXT_COLOR_RED + "Register failed:\n %s\n", e.getMessage());
      }
   }

   public String logout(String authToken) {
      try {
         server.logout(authToken);

         username = null;
         this.authToken = null;
         signedIn = false;

         return String.format(SET_TEXT_COLOR_GREEN + "Logout successful.");
      } catch (Exception e) {
         return String.format(SET_TEXT_COLOR_RED + "Logout failed:\n %s\n", e.getMessage());
      }
   }

   public String createGame(String gameName) {
      CreateGameRequest req = new CreateGameRequest(gameName);

      try {
         CreateGameResult res = server.createGame(req, authToken);

         return String.format(
                 SET_TEXT_COLOR_GREEN + "\nNew game %s created:\n\t" +
                         SET_TEXT_COLOR_YELLOW + "Game ID: %d\n" +
                         SET_TEXT_COLOR_WHITE + "\nUse the \"Join Game\" command to join the game as a player"
                 , gameName, res.getGameID());
      } catch (Exception e) {
         return String.format(SET_TEXT_COLOR_RED + "Create game failed:\n %s\n", e.getMessage());
      }
   }

   public String joinGame(String teamColor, int gameID) {
      JoinGameRequest req = new JoinGameRequest(teamColor, gameID);
      try {
         server.joinGame(req, authToken);

         if (!teamColor.isEmpty()) return String.format(SET_TEXT_COLOR_GREEN + "Joined %d as %s!", gameID, teamColor);
         else return String.format(SET_TEXT_COLOR_GREEN + "Joined %d as an observer!", gameID);
      } catch (Exception e) {
         return String.format(SET_TEXT_COLOR_RED + "Join game failed:\n %s\n", e.getMessage());
      }
   }

   public String listGames() {
      try {
         ListGamesResult res = server.listGames(authToken);
         return createGameList(res);
      } catch (Exception e) {
         return String.format(SET_TEXT_COLOR_RED + "List games failed:\n %s\n", e.getMessage());
      }
   }

   private String createGameList(ListGamesResult res) {
      StringBuilder sb = new StringBuilder(String.format(SET_TEXT_BOLD + "\n%-10s | %-15s | %-15s | %-15s | \n", "Game ID", "Game Name", "White", "Black"));
      List<Game> gameList = res.getGames();
      sb.append(SET_TEXT_COLOR_LIGHT_GREY);

      for (Game game : gameList) {
         String white = game.getWhiteUsername() == null ? "" : game.getWhiteUsername();
         String black = game.getBlackUsername() == null ? "" : game.getBlackUsername();

         sb.append(String.format("%-10d | %-15s | %-15s | %-15s | \n", game.getGameID(), game.getGameName(), white, black));
      }

      return sb.toString();
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getAuthToken() {
      return authToken;
   }

   public void setAuthToken(String authToken) {
      this.authToken = authToken;
   }

   public boolean isSignedIn() {
      return signedIn;
   }

   public void setSignedIn(boolean signedIn) {
      this.signedIn = signedIn;
   }
}
