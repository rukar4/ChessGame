package svc;

/**
 * LoginRequest object contains a username and password that can be accessed by outside classes to verify credentials
 */
public class LoginRequest {
   protected String username;
   protected String password;

   /**
    * Create new LoginRequest that initializes the variables
    *
    * @param username The username the client is trying to log into
    * @param password The password the client is using to log into the given username
    */
   public LoginRequest(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }
}
