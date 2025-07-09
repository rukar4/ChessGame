package svc.account;

import svc.Result;

/**
 * LoginResult stores a username and authToken
 */
public class LoginResult extends Result {

   /**
    * Username from the request if the request is valid
    */
   protected String username;

   /**
    * Auth token given to the user if the request is valid
    */
   protected String authToken;

   /**
    * Constructor creates the message with the username and authToken and saves it.
    */
   public LoginResult() {
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
