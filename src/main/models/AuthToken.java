package models;

import java.util.UUID;

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
   private final String username;

   /**
    * Create new authentication token for given user
    *
    * @param username The user with the AuthToken
    */
   public AuthToken(String username) {
      this.username = username;
      generateToken();
   }

   /**
    * Method to manage token generation
    */
   private void generateToken() {
      UUID token = UUID.randomUUID();
      this.authToken = token.toString();
   }

   public String getAuthToken() {
      return authToken;
   }

   public String getUsername() {
      return username;
   }

   @Override
   public int hashCode() {
      return (int) authToken.charAt(3) * (int) username.charAt(0) / 13;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != getClass()) return false;
      AuthToken objToken = (AuthToken) obj;
      return objToken.username.equals(username) && objToken.authToken.equals(authToken);
   }
}
