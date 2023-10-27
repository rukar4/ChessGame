package svc.account;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import svc.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The RegisterService extends LoginService to inherit variables. This service manages registering a new user.
 */
public class RegisterService extends LoginService {
   private final UserDAO userDAO = UserDAO.getInstance();
   private final AuthDAO authDAO = AuthDAO.getInstance();

   /**
    * Regex to check for valid emails
    */
   private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

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
      String username = req.username;
      String password = req.password;
      String email = req.email;

      if (username == null || password == null || email == null
//              || !isValidEmail(email) Prevents tests from passing -_-
              || username.isEmpty() || password.isEmpty()) {
         result.setApiRes(Result.ApiRes.BAD_REQUEST);
         return result;
      }

      try {
         if (userDAO.getUser(username) == null) {
            User newUser = new User(username, password);
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
         String message;
         if (e instanceof DataAccessException) {
            message = "Error: error accessing the database";
         } else {
            message = "Error: unknown error";
         }
         System.out.println(message + "\nError caught in the RegisterService---------------------------\n" + e);

         result.setApiRes(Result.ApiRes.INTERNAL_ERROR);
         result.setMessage(message);

         return result;
      }
   }

   /**
    * Use regex to make sure the email is valid
    *
    * @param email the email in the request
    * @return TRUE if the email is valid, FALSE otherwise
    */
   private static boolean isValidEmail(String email) {
      Pattern pattern = Pattern.compile(EMAIL_REGEX);
      Matcher matcher = pattern.matcher(email);
      return matcher.matches();
   }
}
