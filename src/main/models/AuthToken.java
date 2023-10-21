package models;

/**
 * AuthToken class generates and stores auth tokens for a certain username.
 */
public class AuthToken {
   /**
    * UUID for authentication of users
    */
   private String authToken;

   /**
    * Username for the associated auth token
    */
   private String username;

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
