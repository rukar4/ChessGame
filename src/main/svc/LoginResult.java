package svc;

/**
 * LoginResult stores the username and auth token from a given request. It returns an error if the request has invalid
 * credentials. If the login is successful, it creates a new authorization token and adds it to the database.
 */
public class LoginResult extends Result {
   protected String username;
   protected String authToken;

   /**
    * Create the result of a given request. If the username and password are valid, the result includes the username
    * and a newly generated authentication token. Otherwise, the result is an unauthorized message.
    *
    * @param request The client request
    */
   public LoginResult(LoginRequest request) {
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
