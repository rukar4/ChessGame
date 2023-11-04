package serverTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.User;
import org.junit.jupiter.api.*;
import svc.ErrorLogger;
import svc.Result;

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
   @DisplayName("Insert user")
   public void insertUser() {
      try {
         userDAO.insertUser(user);
         User insertedUser = userDAO.getUser(user.getUsername());

         Assertions.assertEquals(user.getUsername(), insertedUser.getUsername());
         Assertions.assertEquals(user.getPassword(), insertedUser.getPassword());
         Assertions.assertEquals(user.getEmail(), insertedUser.getEmail());

         Assertions.assertDoesNotThrow(() -> userDAO.insertUser(new User("newUser", password)));

      } catch (Exception e) {
         throw new AssertionError();
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
}
