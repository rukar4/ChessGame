package svc.account;

import models.AuthToken;
import svc.Result;

/**
 * LogoutService manages logging out of the application
 */
public class LogoutService {
   /**
    * Logout the user from the system by removing the auth token from the database.
    *
    * @param token The client request
    * @return the result of the request
    */
   public Result logout(AuthToken token) {
      return null;
   }
}
