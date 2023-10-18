package svc;

import models.User;

/**
 * The RegisterService extends LoginService to inherit variables. This service manages registering a new user.
 */
public class RegisterService extends LoginService {
   /**
    * Generate the register result from a given request.
    *
    * @param request The client request
    * @return the register result from the request
    */
   public RegisterResult register(RegisterRequest request) {
      return null;
   }

   /**
    * Create a new user with the information from the login request if response code is 200
    *
    * @param username The username for the newly registered user
    * @param password The password for the newly registered user
    * @return the newly created user
    */
   private User createUser(String username, String password) {
      return null;
   }
}
