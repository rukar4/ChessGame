import exception.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.ServerFacade;
import svc.account.LoginRequest;
import svc.account.LoginResult;
import svc.account.RegisterRequest;
import svc.game.*;

public class ServerFacadeTests {
   private final ServerFacade server = new ServerFacade("http://localhost:8080");

   private final String username = "username";
   private final String password = "password";
   private final String email = "email@email.com";
   private final String gameName = "newGame";
   private final RegisterRequest registerRequest = new RegisterRequest(username, password, email);
   private final LoginRequest loginRequest = new LoginRequest(username, password);
   private final CreateGameRequest createGameRequest = new CreateGameRequest(gameName);

   private JoinGameRequest createJoinGameRequest(String token) {
      try {
         int gameID = server.createGame(createGameRequest, token).getGameID();

         return new JoinGameRequest("white", gameID);
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
         return null;
      }
   }

   private String didNotFailMessage(String method) {
      return method + "did not fail";
   }

   @BeforeEach
   public void setUp() {
      try {
         server.clearApp();
      } catch (ResponseException e) {
         System.out.println(e.getMessage());
      }
   }

   @Test
   @DisplayName("Clear app")
   public void clearApp() {
      try {
         Assertions.assertDoesNotThrow(server::clearApp);
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Register new user")
   public void registerUser() {
      try {
         LoginResult res = server.register(registerRequest);

         Assertions.assertEquals(username, res.getUsername());
         Assertions.assertNotNull(res.getAuthToken());
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Register same user twice")
   public void registerSameUser() {
      try {
         server.register(registerRequest);
         server.register(registerRequest);
         Assertions.fail(didNotFailMessage("register"));
      } catch (ResponseException e) {
         Assertions.assertEquals(403, e.StatusCode());
         Assertions.assertEquals("[403] Error: already taken", e.getMessage());
      }
   }

   @Test
   @DisplayName("Login as existing user")
   public void login() {
      try {
         // Register the user and log in
         server.register(registerRequest);
         LoginResult res = server.login(loginRequest);

         Assertions.assertEquals(username, res.getUsername());
         Assertions.assertNotNull(res.getAuthToken());
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Login without registering")
   public void loginMissingUser() {
      try {
         server.login(loginRequest);
         Assertions.fail(didNotFailMessage("login"));
      } catch (ResponseException e) {
         Assertions.assertEquals(401, e.StatusCode());
         Assertions.assertEquals("[401] Error: unauthorized", e.getMessage());
      }
   }

   @Test
   @DisplayName("Logout")
   public void logout() {
      try {
         LoginResult res = server.register(registerRequest);

         Assertions.assertDoesNotThrow(() -> server.logout(res.getAuthToken()));
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Logout without auth token")
   public void logoutMissingToken() {
      try {
         server.logout(null);
         Assertions.fail(didNotFailMessage("logout"));
      } catch (ResponseException e) {
         Assertions.assertEquals(401, e.StatusCode());
         Assertions.assertEquals("[401] Error: unauthorized", e.getMessage());
      }
   }

   @Test
   @DisplayName("Create a new game")
   public void createGame() {
      try {
         String token = server.register(registerRequest).getAuthToken();
         CreateGameResult res = server.createGame(createGameRequest, token);

         Assertions.assertTrue(res.getGameID() > 0);
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Create game without a name")
   public void createGameMissingName() {
      try {
         String token = server.register(registerRequest).getAuthToken();
         server.createGame(new CreateGameRequest(null), token);
         Assertions.fail(didNotFailMessage("createGame"));
      } catch (ResponseException e) {
         Assertions.assertEquals(400, e.StatusCode());
         Assertions.assertEquals("[400] Error: bad request", e.getMessage());
      }
   }

   @Test
   @DisplayName("Join game")
   public void joinGame() {
      try {
         String token = server.register(registerRequest).getAuthToken();
         JoinGameRequest req = createJoinGameRequest(token);

         Assertions.assertDoesNotThrow(() -> server.joinGame(req, token));
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Join game as same color twice")
   public void joinGameSameColor() {
      try {
         String token = server.register(registerRequest).getAuthToken();
         JoinGameRequest req = createJoinGameRequest(token);

         server.joinGame(req, token);
         server.joinGame(req, token);
         Assertions.fail(didNotFailMessage("joinGame"));
      } catch (ResponseException e) {
         Assertions.assertEquals(403, e.StatusCode());
         Assertions.assertEquals("[403] Error: already taken", e.getMessage());
      }
   }

   @Test
   @DisplayName("Get a game")
   public void getGame() {
      try {
         String token = server.register(registerRequest).getAuthToken();
         CreateGameResult createGameResult = server.createGame(createGameRequest, token);
         GetGameResult res = server.getGame(createGameResult.getGameID(), token);

         Assertions.assertEquals(createGameResult.getGameID(), res.getGame().getGameID());
         Assertions.assertEquals(gameName, res.getGame().getGameName());
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("Get a nonexistent game")
   public void getGameBadID() {
      int gameID = -1;

      try {
         String token = server.register(registerRequest).getAuthToken();
         server.getGame(gameID, token);
         Assertions.fail(didNotFailMessage("getGame"));
      } catch (ResponseException e) {
         Assertions.assertEquals(400, e.StatusCode());
         Assertions.assertEquals(String.format("[400] Error: game %d not found", gameID), e.getMessage());
      }
   }

   @Test
   @DisplayName("List games")
   public void listGames() {
      int numGames = 10;

      try {
         String token = server.register(registerRequest).getAuthToken();

         for (int i = 0; i < numGames; ++i) {
            server.createGame(createGameRequest, token);
         }

         ListGamesResult res = server.listGames(token);

         Assertions.assertEquals(res.getGames().size(), numGames);
      } catch (Exception e) {
         Assertions.fail(e.getMessage());
      }
   }

   @Test
   @DisplayName("List games unauthorized")
   public void listGamesUnauthorized() {
      try {
         server.listGames(null);
         Assertions.fail(didNotFailMessage("listGames"));
      } catch (ResponseException e) {
         Assertions.assertEquals(401, e.StatusCode());
         Assertions.assertEquals("[401] Error: unauthorized", e.getMessage());
      }
   }
}
