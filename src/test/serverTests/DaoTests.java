package serverTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import svc.Handler;
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

   @Test
   @Order(1)
   @DisplayName("getUser")
   public void testGetUser() throws DataAccessException {

   }

}
