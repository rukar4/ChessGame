package svc.ServiceClasses;

import dao.AuthDAO;
import dao.UserDAO;
import models.AuthToken;
import models.User;
import svc.Result;
import svc.account.LoginResult;
import svc.account.RegisterRequest;

/**
 * The RegisterService extends LoginService to inherit variables. This service manages registering a new user.
 */
public class RegisterService extends LoginService {
   /**
    * Regex to check for valid emails
    */
   private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
   private final UserDAO userDAO = UserDAO.getInstance();
   private final AuthDAO authDAO = AuthDAO.getInstance();

   /**
    * Register the user from the given request and return the corresponding login result.
    * A new user is created from the request information.
    * The request is invalid if the username is already taken (403).
    * The request is invalid if the email is not in a valid format
    *
    * @param req The client request
    * @return the result from the request
    */
   public LoginResult register(RegisterRequest req) {
      LoginResult result = new LoginResult();
      String username = req.getUsername();
      String password = req.getPassword();
      String email = req.getEmail();

      if (username == null || password == null || email == null || username.isEmpty() || password.isEmpty()) {
         result.setApiRes(Result.ApiRes.BAD_REQUEST);
         return result;
      }

      try {
         if (userDAO.getUser(username) == null) {
            User newUser = new User(username.toLowerCase(), password);
            newUser.setEmail(email);
            userDAO.insertUser(newUser);

            AuthToken authToken = new AuthToken(username);
            authDAO.insertToken(authToken);

            result.setUsername(username);
            result.setAuthToken(authToken.getAuthToken());
            result.setApiRes(Result.ApiRes.SUCCESS);

         } else {
            result.setApiRes(Result.ApiRes.ALREADY_TAKEN);
         }
         return result;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "RegisterService", result);

         return result;
      }
   }
}
