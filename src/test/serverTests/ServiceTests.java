package serverTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import org.junit.jupiter.api.*;
import svc.ErrorLogger;
import svc.Result;
import svc.account.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
   private final GameDAO gameDAO = GameDAO.getInstance();
   private final UserDAO userDAO = UserDAO.getInstance();
   private final AuthDAO authDAO = AuthDAO.getInstance();
   private final String username = "rukar42";
   private final String password = "password";
   private final String email = "test@test.com";
   private final User user = new User(username, password);

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
   public void registerSuccess() {
      RegisterService registerService = new RegisterService();
      RegisterRequest registerRequest = new RegisterRequest("New User", password, email);
      LoginResult result = registerService.register(registerRequest);

      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(2)
   @DisplayName("Register username already taken")
   public void registerAlreadyTaken() {
      RegisterService registerService = new RegisterService();
      RegisterRequest registerRequest = new RegisterRequest(username, password, email);
      LoginResult result = registerService.register(registerRequest);

      Assertions.assertEquals(403, result.getApiRes().getCode());
   }

   @Test
   @Order(3)
   @DisplayName("Login Success")
   public void loginSuccess() {
      LoginService loginService = new LoginService();
      LoginRequest loginRequest = new LoginRequest(username, password);

      LoginResult result = loginService.login(loginRequest);

      Assertions.assertEquals(200, result.getApiRes().getCode());
   }

   @Test
   @Order(4)
   @DisplayName("Login Invalid Password")
   public void loginInvalidPass() {
      LoginService loginService = new LoginService();
      LoginRequest loginRequest = new LoginRequest(username, password + "invalid");

      LoginResult result = loginService.login(loginRequest);

      Assertions.assertEquals(401, result.getApiRes().getCode());
   }

   @Test
   @Order(5)
   @DisplayName("Login Invalid username")
   public void loginInvalidUser() {
      LoginService loginService = new LoginService();
      LoginRequest loginRequest = new LoginRequest(username + "invalid", password);

      LoginResult result = loginService.login(loginRequest);

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

      Assertions.assertEquals(200, result.getApiRes().getCode());
   }
}
