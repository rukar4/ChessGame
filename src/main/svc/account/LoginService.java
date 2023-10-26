package svc.account;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import svc.Result;

import java.util.Objects;

/**
 * LoginService object manages LoginRequests and creates LoginResults.
 */
public class LoginService {
   private UserDAO userDAO;
   private AuthDAO authDAO;

   /**
    * Generate the login result from a given request. If the request is valid, generate a new auth token for the caller.
    *
    * @param request The received request from the client
    * @return the login result from the request
    */
   public LoginResult login(LoginRequest request) {
      LoginResult loginResult = new LoginResult();
      String username = request.username;
      String password = request.password;

      if (username == null || password == null) {
         loginResult.setApiCode(Result.ResponseCode.BAD_REQUEST);
         return loginResult;
      }

      try {
         User user = userDAO.getUser(username);
         if (user != null) {
            if (Objects.equals(user.getPassword(), password)) {
               AuthToken authToken = new AuthToken(username);

               loginResult.setUsername(username);
               loginResult.setAuthToken(authToken.getAuthToken());

               loginResult.setApiCode(Result.ResponseCode.SUCCESS);
            } else {
               loginResult.setApiCode(Result.ResponseCode.UNAUTHORIZED);
            }
         }
         return loginResult;
      } catch (Exception e) {
         String message;
         if (e instanceof DataAccessException) {
            message = "Error: error accessing the database";
         } else {
            message = "Error: unknown error";
         }
         System.out.println(message + "\nError caught in the LoginService---------------------------\n" + e);

         loginResult.setApiCode(Result.ResponseCode.INTERNAL_ERROR);
         loginResult.setMessage(message);

         return loginResult;
      }
   }
}
