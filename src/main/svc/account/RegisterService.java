package svc.account;

import models.User;

/**
 * The RegisterService extends LoginService to inherit variables. This service manages registering a new user.
 */
public class RegisterService extends LoginService {
   /**
    * Register the user from the given request and return the corresponding login result.
    * A new user is created from the request information.
    * The request is invalid if the username or email is already taken (403).
    * The request is invalid if the email is not in a valid format
    *
    * @param request The client request
    * @return the result from the request
    */
   public LoginResult register(RegisterRequest request) {
      return null;
   }

   /**
    * Create a new user with the information from the login request
    *
    * @param username The username for the new user
    * @param password The password for the new user
    * @return the newly created user
    */
   private User createUser(String username, String password) {
      return null;
   }
}
