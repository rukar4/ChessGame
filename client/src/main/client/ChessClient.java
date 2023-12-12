package client;

import chess.ChessGame.TeamColor;
import client.ui.GameRepl;
import client.webSocket.ServerMessageHandler;
import client.webSocket.WSFacade;
import exception.ResponseException;
import models.Game;
import server.ServerFacade;
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
   private final String serverURL;
   private final ServerFacade server;
   private String username = null;
   private String authToken = null;
   private boolean signedIn = false;
   private ServerMessageHandler serverMessageHandler;
   private WSFacade ws;

   private TeamColor playerColor = null;

   public ChessClient(String serverUrl) {
      server = new ServerFacade(serverUrl);
      this.serverURL = serverUrl;
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

   public void joinGame(String teamColor, int gameID) {
      try {
         JoinGameRequest req = new JoinGameRequest(teamColor, gameID);
         server.joinGame(req, authToken);

         GameRepl gameRepl = new GameRepl(this);

         ws = new WSFacade(serverURL, serverMessageHandler);
         if (teamColor.isEmpty()) {
            ws.joinGame(authToken, gameID, playerColor);
         } else {
            playerColor = TeamColor.valueOf(teamColor.toUpperCase());
            ws.joinGame(authToken, gameID, playerColor);
         }
         // Join the game!
         gameRepl.run();

      } catch (Exception e) {
         System.out.printf(SET_TEXT_COLOR_RED + "Join game failed:\n %s\n", e.getMessage());
      }
   }

   public void rejoinGame(int gameID) {
      try {
         Game game = getCurrentGameState(gameID);

         if (username.equalsIgnoreCase(game.getWhiteUsername())) {
            playerColor = TeamColor.WHITE;
         } else if (username.equalsIgnoreCase(game.getBlackUsername())) {
            playerColor = TeamColor.BLACK;
         } else {
            int errCode = 400;
            throw new ResponseException(errCode, String.format("[%d] You are not a player in %d", errCode, gameID));
         }

         this.joinGame("", gameID);

      } catch (ResponseException e) {
         System.out.printf(SET_TEXT_COLOR_RED + "Join game failed:\n %s\n", e.getMessage());
      }
   }

   public void leaveGame(int gameID) {
      try {
         ws = new WSFacade(serverURL, serverMessageHandler);
         ws.leaveGame(authToken, gameID);
      } catch (ResponseException e) {
         System.out.printf(SET_TEXT_COLOR_RED + "Leave game failed:\n %s\n", e.getMessage());
      }
   }

   public void resign(int gameID) {
      try {
         ws = new WSFacade(serverURL, serverMessageHandler);
         ws.resign(authToken, gameID);
      } catch (ResponseException e) {
         System.out.printf(SET_TEXT_COLOR_RED + "Resign game failed:\n %s\n", e.getMessage());
      }
   }

   public String listGames() {
      try {
         ListGamesResult res = server.listGames(authToken);
         return createGameList(res);
      } catch (ResponseException e) {
         return String.format(SET_TEXT_COLOR_RED + "List games failed:\n %s\n", e.getMessage());
      }
   }

   private String createGameList(ListGamesResult res) {
      // Table headers
      StringBuilder sb = new StringBuilder(String.format(SET_TEXT_BOLD + "\n%-10s | %-15s | %-15s | %-15s | \n", "Game ID", "Game Name", "White", "Black"));
      List<Game> gameList = res.getGames();
      sb.append(SET_TEXT_COLOR_LIGHT_GREY);

      // Table body
      for (Game game : gameList) {
         String white = game.getWhiteUsername() == null ? "" : game.getWhiteUsername();
         String black = game.getBlackUsername() == null ? "" : game.getBlackUsername();

         sb.append(String.format("%-10d | %-15s | %-15s | %-15s | \n", game.getGameID(), game.getGameName(), white, black));
      }
      return sb.toString();
   }

   public Game getCurrentGameState(int gameID) throws ResponseException {
      return server.getGame(gameID, authToken).getGame();
   }

   public String getUsername() {
      return username;
   }

   public String getAuthToken() {
      return authToken;
   }

   public boolean isSignedIn() {
      return signedIn;
   }

   public void setNotificationHandler(ServerMessageHandler serverMessageHandler) {
      this.serverMessageHandler = serverMessageHandler;
   }

   public TeamColor getPlayerColor() {
      return playerColor;
   }

   public void setPlayerColor(TeamColor playerColor) {
      this.playerColor = playerColor;
   }
}
