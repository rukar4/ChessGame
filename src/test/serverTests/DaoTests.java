package serverTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.*;
import svc.ServiceClasses.ErrorLogger;
import svc.Result;
import java.util.List;

import static svc.Result.ApiRes.BAD_REQUEST;

public class DaoTests {
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

   private void populateUserDB(int numUsers) throws DataAccessException {
      // Insert users into db
      for (int i = 0; i < numUsers; ++i) {
         User user1 = new User("user" + i, password, email);
         userDAO.insertUser(user1);
      }
   }

   private void populateAuthDB(int numTokens) throws DataAccessException {
      populateUserDB(numTokens);
      List<User> users = userDAO.getAllUsers();

      for (User user : users) {
         authDAO.insertToken(new AuthToken(user.getUsername()));
      }
   }

   private void populateDB(int numGames) throws DataAccessException {
      // Populate the DB with all data
      for (int i = 0; i < numGames; ++i) {
         User user1 = new User(white + i, password, email);
         User user2 = new User(black + i, password, email);

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
   }

   @BeforeEach
   public void setup() {
      try {
         authDAO.clearTokens();
         gameDAO.clearGames();
         userDAO.clearUsers();

         user.setEmail(email);
      } catch (Exception e) {
         ErrorLogger errorLogger = new ErrorLogger();
         errorLogger.errMessage(e, "Test Setup", new Result());
      }
   }

   @Test
   @Order(1)
   @DisplayName("Insert/get user")
   public void insertAndGetUser() {
      try {
         userDAO.insertUser(user);
         User insertedUser = userDAO.getUser(user.getUsername());

         Assertions.assertEquals(user.getUsername(), insertedUser.getUsername());
         Assertions.assertEquals(user.getPassword(), insertedUser.getPassword());
         Assertions.assertEquals(user.getEmail(), insertedUser.getEmail());

         Assertions.assertDoesNotThrow(() -> userDAO.insertUser(new User("newUser", password)));

         Assertions.assertDoesNotThrow(() -> userDAO.getUser(user.getUsername()));
         Assertions.assertDoesNotThrow(() -> userDAO.getUser("newUser"));

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(2)
   @DisplayName("Insert same user twice")
   public void insertSameUser() {
      Assertions.assertThrows(DataAccessException.class, () -> {
         userDAO.insertUser(user);
         userDAO.insertUser(user);
      });
   }

   @Test
   @Order(3)
   @DisplayName("Get nonexistent user")
   public void getNullUser() {
      try {
         Assertions.assertNull(userDAO.getUser(username));
         Assertions.assertNull(userDAO.getUser("user1"));
         Assertions.assertNull(userDAO.getUser("user2"));
      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(4)
   @DisplayName("Get all users")
   public void getAllUsers() {
      try {
         int numUsers = 20;

         populateUserDB(numUsers);

         Assertions.assertEquals(numUsers, userDAO.getAllUsers().size());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(4)
   @DisplayName("Clear users")
   public void clearUsers() {
      try {
         int numUsers = 20;
         populateUserDB(numUsers);

         Assertions.assertEquals(numUsers, userDAO.getAllUsers().size());

         userDAO.clearUsers();

         Assertions.assertEquals(0, userDAO.getAllUsers().size());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(5)
   @DisplayName("Insert/get token")
   public void insertAndGetToken() {
      try {
         // Insert user to satisfy key constraints
         userDAO.insertUser(new User(username, password, email));

         AuthToken token = new AuthToken(username);
         authDAO.insertToken(token);

         AuthToken insertedToken = authDAO.getToken(token.getAuthToken());

         Assertions.assertEquals(token.getAuthToken(), insertedToken.getAuthToken());
         Assertions.assertEquals(token.getUsername(), insertedToken.getUsername());

         userDAO.insertUser(new User("newUser", password, email));
         AuthToken newToken = new AuthToken("newUser");

         Assertions.assertDoesNotThrow(() -> authDAO.insertToken(newToken));

         Assertions.assertDoesNotThrow(() -> authDAO.getToken(token.getAuthToken()));
         Assertions.assertDoesNotThrow(() -> authDAO.getToken(newToken.getAuthToken()));
      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(6)
   @DisplayName("Insert same token twice")
   public void insertSameToken() {
      Assertions.assertThrows(DataAccessException.class, () -> {
         // Insert user to satisfy key constraints
         userDAO.insertUser(new User(username, password, email));

         AuthToken token = new AuthToken(username);
         authDAO.insertToken(token);
         authDAO.insertToken(token);
      });
   }

   @Test
   @Order(7)
   @DisplayName("Insert token for nonexistent user")
   public void insertTokenMissingUser() {
      Assertions.assertThrows(DataAccessException.class, () -> {
         // Insert token without first making a user
         AuthToken token = new AuthToken(username);
         authDAO.insertToken(token);
      });
   }

   @Test
   @Order(8)
   @DisplayName("Get nonexistent token")
   public void getNonexistentToken() {
      try {
         Assertions.assertNull(userDAO.getUser(username));
         Assertions.assertNull(userDAO.getUser(username));
         Assertions.assertNull(userDAO.getUser(username));
      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(9)
   @DisplayName("Get all tokens")
   public void getAllTokens() {
      try {
         int numTokens = 20;

         populateAuthDB(numTokens);

         Assertions.assertEquals(numTokens, authDAO.getAllTokens().size());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(10)
   @DisplayName("Clear tokens")
   public void clearTokens() {
      try {
         int numTokens = 20;
         populateAuthDB(numTokens);

         Assertions.assertEquals(numTokens, authDAO.getAllTokens().size());

         authDAO.clearTokens();

         Assertions.assertEquals(0, authDAO.getAllTokens().size());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(11)
   @DisplayName("Remove a token")
   public void removeToken() {
      try {
         userDAO.insertUser(new User(username, password, email));
         AuthToken token = new AuthToken(username);
         authDAO.insertToken(token);

         // Make sure token was inserted
         Assertions.assertNotNull(authDAO.getToken(token.getAuthToken()));

         authDAO.removeToken(token.getAuthToken());

         // getToken should return null if it doesn't exist
         Assertions.assertNull(authDAO.getToken(token.getAuthToken()));

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(11)
   @DisplayName("Remove a nonexistent token")
   public void removeNullToken() {
      try {
         // Insert the user, but not the token, into the DB
         userDAO.insertUser(new User(username, password, email));
         AuthToken token = new AuthToken(username);

         Assertions.assertThrows(DataAccessException.class, () -> authDAO.removeToken(token.getAuthToken()));

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(12)
   @DisplayName("Insert/get game")
   public void insertAndGetGame() {
      try {
         Game game = new Game(gameName);

         gameDAO.insertGame(game);
         int gameID = game.getGameID();

         Game insertedGame = gameDAO.getGame(gameID);

         // Check all initialized variables
         Assertions.assertNotNull(insertedGame);
         Assertions.assertEquals(gameID, insertedGame.getGameID());
         Assertions.assertEquals(gameName, insertedGame.getGameName());

         // All usernames should be null
         Assertions.assertNull(game.getWhiteUsername());
         Assertions.assertNull(game.getBlackUsername());
         Assertions.assertEquals(game.getWhiteUsername(), insertedGame.getWhiteUsername());
         Assertions.assertEquals(game.getBlackUsername(), insertedGame.getBlackUsername());
      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(13)
   @DisplayName("Get nonexistent game")
   public void getNullGame() {
      try {
         Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(-1));
      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(14)
   @DisplayName("Claim color")
   public void claimColor() {
      try {
         Game game = new Game(gameName);
         gameDAO.insertGame(game);
         int gameID = game.getGameID();

         // Create and add players to the game
         userDAO.insertUser(new User(white, password));
         userDAO.insertUser(new User(black, password));
         gameDAO.claimColor(white, white, gameID);
         gameDAO.claimColor(black, black, gameID);

         // Update game
         game = gameDAO.getGame(gameID);

         // Ensure getGame returned all the correct data
         Assertions.assertNotNull(game);
         Assertions.assertEquals(gameID, game.getGameID());
         Assertions.assertEquals(gameName, game.getGameName());
         Assertions.assertEquals(white, game.getWhiteUsername());
         Assertions.assertEquals(black, game.getBlackUsername());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(14)
   @DisplayName("Claim invalid color")
   public void claimInvalidColor() {
      try {
         Game game = new Game(gameName);
         gameDAO.insertGame(game);
         int gameID = game.getGameID();

         // Create player
         userDAO.insertUser(new User(white, password));

         Assertions.assertThrows(DataAccessException.class, () -> {
            try {
               gameDAO.claimColor(white, "INVALID", gameID);
            } catch (Exception e){
               if (e instanceof DataAccessException dataAccessException) {
                  Assertions.assertEquals(BAD_REQUEST, dataAccessException.getResponseCode());
                  throw e;
               } else {
                  throw new AssertionError(e);
               }
            }
         });

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(15)
   @DisplayName("Claim color invalid game ID")
   public void claimColorBadID() {
      try {
         // Create player
         userDAO.insertUser(new User(white, password));

         Assertions.assertThrows(DataAccessException.class, () -> {
            try {
               gameDAO.claimColor(white, white, -1);
            } catch (Exception e){
               if (e instanceof DataAccessException dataAccessException) {
                  Assertions.assertEquals(BAD_REQUEST, dataAccessException.getResponseCode());
                  throw e;
               } else {
                  throw new AssertionError(e);
               }
            }
         });

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(16)
   @DisplayName("Get all games")
   public void getAllGames() {
      try {
         int numGames = 20;

         populateDB(numGames);

         Assertions.assertEquals(numGames, gameDAO.getAllGames().size());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }

   @Test
   @Order(17)
   @DisplayName("Clear games")
   public void clearGames() {
      try {
         int numGames = 20;

         populateDB(numGames);

         Assertions.assertEquals(numGames, gameDAO.getAllGames().size());

         gameDAO.clearGames();

         Assertions.assertEquals(0, gameDAO.getAllGames().size());

      } catch (Exception e) {
         throw new AssertionError(e);
      }
   }
}
