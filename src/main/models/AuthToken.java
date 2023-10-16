package models;

public class AuthToken {
   String authToken;
   String username;

   /**
    * Create new authentication token for given user
    *
    * @param username The user with the AuthToken
    */
   public AuthToken(String username) {
   }

   /**
    * Method to manage token generation
    */
   private void generateToken() {
   }

   /**
    * Method to update the token
    */
   public void updateToken() {
   }

   public String getAuthToken() {
      return authToken;
   }

   public String getUsername() {
      return username;
   }
}
