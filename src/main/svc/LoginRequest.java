package svc;

public class LoginRequest {
   private String username;
   private String password;

   /**
    * Create new LoginRequest that initializes the variables
    *
    * @param username The username the client is trying to log into
    * @param password The password the client is using to log into the given username
    */
   public LoginRequest(String username, String password) {
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
