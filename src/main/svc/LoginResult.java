package svc;

import models.AuthToken;

public class LoginResult {
   private String message;
   private String username;
   private String authToken;

   /**
    * Create the result of a given request. If the username and password are valid,
    * the result includes the username and authentication token. Otherwise, the result
    * is an unauthorized message.
    *
    * @param request The request to evaluate
    */
   public LoginResult(LoginRequest request) {
      // TODO: Get AuthToken
      // TODO: if (AuthToken) set username and authToken else: set message to unauthorized
   }

   /**
    * Verify that the given user and password are valid
    *
    * @return new AuthToken if authenticated, null if not
    */
   private AuthToken authenticateUser(String username, String password) {
      return null;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getAuthToken() {
      return authToken;
   }

   public void setAuthToken(String authToken) {
      this.authToken = authToken;
   }
}
