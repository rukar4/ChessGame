package svc.account;

import dao.AuthDAO;
import dao.UserDAO;
import models.AuthToken;
import models.User;
import svc.ErrorLogger;
import svc.Result;

import java.util.Objects;

/**
 * LoginService object manages LoginRequests and creates LoginResults.
 */
public class LoginService {
   private final UserDAO userDAO = UserDAO.getInstance();
   private final AuthDAO authDAO = AuthDAO.getInstance();

   /**
    * Generate the login result from a given request. If the request is valid, generate a new auth token for the caller.
    *
    * @param req The received request from the client
    * @return the login result from the request
    */
   public LoginResult login(LoginRequest req) {
      LoginResult result = new LoginResult();
      String username = req.username;
      String password = req.password;

      if (username == null || password == null) {
         result.setApiRes(Result.ApiRes.BAD_REQUEST);
         return result;
      }

      try {
         User user = userDAO.getUser(username);
         // If the user is found and the credentials are correct, return 200
         if (user != null) {
            if (Objects.equals(user.getPassword(), password)) {
               AuthToken authToken = new AuthToken(username);
               authDAO.insertToken(authToken);

               result.setUsername(username);
               result.setAuthToken(authToken.getAuthToken());
               result.setApiRes(Result.ApiRes.SUCCESS);

               return result;
            }
         }
         // If the user is not found or the credentials do not match, return 401
         result.setApiRes(Result.ApiRes.UNAUTHORIZED);
         return result;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "LoginService", result);

         return result;
      }
   }
}
