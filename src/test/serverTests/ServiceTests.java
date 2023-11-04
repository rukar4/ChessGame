package serverTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.*;
import svc.ErrorLogger;
import svc.Handler;
import svc.Result;
import svc.account.*;
import svc.clearApp.ClearAppService;
import svc.game.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
   private final GameDAO gameDAO = GameDAO.getInstance();
   private final UserDAO userDAO = UserDAO.getInstance();
   private final AuthDAO authDAO = AuthDAO.getInstance();
   private final String username = "rukar42";
   private final String password = "password";
   private final String email = "test@test.com";
   private final String gameName = "TestGame";
   private final String white = "white";
   private final String black = "black";
   private final User user = new User(username, password);
   private final Database db = new Database();

   @BeforeEach
   public void setup() {
      try {
         authDAO.clearTokens();
         gameDAO.clearGames();
         userDAO.clearUsers();

         user.setEmail(email);
         userDAO.insertUser(user);
      } catch (Exception e) {
         ErrorLogger errorLogger = new ErrorLogger();
         errorLogger.errMessage(e, "Test Setup", new Result());
      }
   }

   @Test
   @Order(1)
   @DisplayName("Register Success")
   public void registerSuccess() throws DataAccessException {
      RegisterService registerService = new RegisterService();
      RegisterRequest registerRequest = new RegisterRequest("New User", password, email);
      LoginResult result = registerService.register(registerRequest);

      User newUser = userDAO.getUser("New User");

      Assertions.assertEquals("New User", result.getUsername());
      Assertions.assertEquals("New User", newUser.getUsername());
      Assertions.assertEquals(password, newUser.getPassword());
      Assertions.assertEquals(email, newUser.getEmail());
      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(2)
   @DisplayName("Register username already taken")
   public void registerAlreadyTaken() {
      RegisterService registerService = new RegisterService();
      RegisterRequest registerRequest = new RegisterRequest(username, password, email);
      LoginResult result = registerService.register(registerRequest);

      Assertions.assertEquals("Error: already taken", result.getMessage());
      Assertions.assertEquals(403, result.getApiRes().getCode());
   }

   @Test
   @Order(3)
   @DisplayName("Login Success")
   public void loginSuccess() {
      LoginService loginService = new LoginService();
      LoginRequest loginRequest = new LoginRequest(username, password);

      LoginResult result = loginService.login(loginRequest);

      Assertions.assertEquals(username, result.getUsername());
      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(4)
   @DisplayName("Login Invalid Password")
   public void loginInvalidPass() {
      LoginService loginService = new LoginService();
      LoginRequest loginRequest = new LoginRequest(username, password + "invalid");

      LoginResult result = loginService.login(loginRequest);

      Assertions.assertEquals("Error: unauthorized", result.getMessage());
      Assertions.assertEquals(401, result.getApiRes().getCode());
   }

   @Test
   @Order(5)
   @DisplayName("Login Invalid username")
   public void loginInvalidUser() {
      LoginService loginService = new LoginService();
      LoginRequest loginRequest = new LoginRequest(username + "invalid", password);

      LoginResult result = loginService.login(loginRequest);

      Assertions.assertEquals("Error: unauthorized", result.getMessage());
      Assertions.assertEquals(401, result.getApiRes().getCode());
   }

   @Test
   @Order(6)
   @DisplayName("Logout Success")
   public void logoutSuccess() throws DataAccessException {
      AuthToken authToken = new AuthToken(username);
      String token = authToken.getAuthToken();

      authDAO.insertToken(authToken);

      LogoutService logoutService = new LogoutService();
      Result result = logoutService.logout(token);

      Assertions.assertEquals(0, authDAO.getAllTokens().size());
      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(7)
   @DisplayName("Clear Application")
   public void clearAppSuccess() throws DataAccessException {
      for (int i = 0; i < 100; ++i) {
         User newUser = new User(String.valueOf(i), password);
         AuthToken token = new AuthToken(newUser.getUsername());
         Game game = new Game(String.valueOf(i));

         userDAO.insertUser(newUser);
         authDAO.insertToken(token);
         gameDAO.insertGame(game);
      }

      ClearAppService clearAppService = new ClearAppService();
      Result result = clearAppService.clearApp();

      Assertions.assertEquals(0, userDAO.getAllUsers().size());
      Assertions.assertEquals(0, authDAO.getAllTokens().size());
      Assertions.assertEquals(0, gameDAO.getAllGames().size());
      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(8)
   @DisplayName("Create Game")
   public void createGameSuccess() throws DataAccessException {
      CreateGameService createGameService = new CreateGameService();
      CreateGameRequest request = new CreateGameRequest(gameName);
      CreateGameResult result = createGameService.createGame(request);

      Assertions.assertTrue(result.getGameID() > 0);

      Game game = gameDAO.getGame(result.getGameID());

      Assertions.assertEquals(gameName, game.getGameName());
      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(9)
   @DisplayName("Create Game Missing Name")
   public void createMissingName() throws DataAccessException {
      CreateGameService createGameService = new CreateGameService();
      CreateGameRequest request = new CreateGameRequest(null);
      CreateGameResult result = createGameService.createGame(request);

      Assertions.assertEquals("Error: bad request", result.getMessage());
      Assertions.assertEquals(0, gameDAO.getAllGames().size());
      Assertions.assertEquals(400, result.getApiRes().getCode());
   }

   @Test
   @Order(10)
   @DisplayName("Create Game Name Empty")
   public void createNameEmpty() throws DataAccessException {
      CreateGameService createGameService = new CreateGameService();
      CreateGameRequest request = new CreateGameRequest("");
      CreateGameResult result = createGameService.createGame(request);

      Assertions.assertEquals("Error: bad request", result.getMessage());
      Assertions.assertEquals(0, gameDAO.getAllGames().size());
      Assertions.assertEquals(400, result.getApiRes().getCode());
   }

   @Test
   @Order(11)
   @DisplayName("Join Game")
   public void joinGame() throws DataAccessException {
      Game game = new Game(gameName);
      AuthToken token = new AuthToken(username);

      authDAO.insertToken(token);
      gameDAO.insertGame(game);

      // Join as white
      JoinGameService joinGameService = new JoinGameService();
      JoinGameRequest joinAsWhite = new JoinGameRequest(white, game.getGameID());

      Result result = joinGameService.joinGame(token.getAuthToken(), joinAsWhite);

      Assertions.assertEquals(200, result.getApiRes().getCode());

      // Join as black
      JoinGameRequest joinAsBlack = new JoinGameRequest(black, game.getGameID());
      result = joinGameService.joinGame(token.getAuthToken(), joinAsBlack);

      Assertions.assertEquals(200, result.getApiRes().getCode());

      // Attempt to join as white again
      result = joinGameService.joinGame(token.getAuthToken(), joinAsWhite);

      Assertions.assertEquals("Error: already taken", result.getMessage());
      Assertions.assertEquals(403, result.getApiRes().getCode());

      // Attempt to join with a new user
      User user1 = new User("newUser", password, email);
      userDAO.insertUser(user1);
      token = new AuthToken("newUser");
      authDAO.insertToken(token);
      result = joinGameService.joinGame(token.getAuthToken(), joinAsWhite);

      Assertions.assertEquals("Error: already taken", result.getMessage());
      Assertions.assertEquals(403, result.getApiRes().getCode());
   }

   @Test
   @Order(12)
   @DisplayName("Join Game Invalid Color")
   public void joinGameInvalidColor() throws DataAccessException {
      Game game = new Game(gameName);
      AuthToken token = new AuthToken(username);

      authDAO.insertToken(token);
      gameDAO.insertGame(game);

      JoinGameService joinGameService = new JoinGameService();
      JoinGameRequest joinGameRequest = new JoinGameRequest(white + "invalid", game.getGameID());

      Result result = joinGameService.joinGame(token.getAuthToken(), joinGameRequest);

      Assertions.assertEquals("Error: invalid color", result.getMessage());
      Assertions.assertEquals(400, result.getApiRes().getCode());
   }

   @Test
   @Order(13)
   @DisplayName("List Games")
   public void listGames() throws DataAccessException {
      int numGames = 21;

      // Insert games into database with players and colors
      for (int i = 1; i <= numGames; ++i) {
         User user1 = new User("user" + i, password);
         User user2 = new User("user100" + i, password);

         AuthToken token1 = new AuthToken(user1.getUsername());
         AuthToken token2 = new AuthToken(user2.getUsername());
         Game game = new Game(String.valueOf(i));

         userDAO.insertUser(user1);
         authDAO.insertToken(token1);
         userDAO.insertUser(user2);
         authDAO.insertToken(token2);
         gameDAO.insertGame(game);

         gameDAO.claimColor(user1.getUsername(), white, game.getGameID());
         gameDAO.claimColor(user2.getUsername(), black, game.getGameID());
      }
      ListGamesService listGamesService = new ListGamesService();
      ListGamesResult result = listGamesService.getGames();

      Assertions.assertEquals(numGames, result.getGames().size());
      Assertions.assertEquals(200, result.getApiRes().getCode());

      authDAO.clearTokens();
      gameDAO.clearGames();
      userDAO.clearUsers();
   }

   @Test
   @Order(14)
   @DisplayName("Authorization Failure")
   public void testAuthorization() throws DataAccessException {
      // Don't insert into database so it's invalid. All unauthorized tokens are handled by this function
      AuthToken token = new AuthToken(username);
      Handler handler = new Handler();
      Result result = new Result();

      Assertions.assertTrue(handler.isNotAuthorized(token.getAuthToken(), result));
      Assertions.assertEquals("Error: unauthorized", result.getMessage());
      Assertions.assertEquals(401, result.getApiRes().getCode());
   }
}
