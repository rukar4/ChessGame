package svc.account;

/**
 * LoginRequest object contains a username and password to try to verify credentials
 */
public class LoginRequest {
   /**
    * Username given in the request
    */
   protected String username;

   /**
    * Password given in the request
    */
   protected String password;

   /**
    * Create new LoginRequest
    */
   public LoginRequest(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
