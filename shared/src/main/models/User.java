package models;

/**
 * User objects store the variables for users credentials as well as their id and email.
 */
public class User {

   /**
    * The user's name
    */
   private String username;

   /**
    * The user's password
    */
   private String password;

   /**
    * The user's email
    */
   private String email;

   /**
    * Create a new user with the given username and password
    *
    * @param username The user's username
    * @param password The user's password
    */
   public User(String username, String password) {
      this.username = username;
      this.password = password;
   }

   public User(String username, String password, String email) {
      this.username = username;
      this.password = password;
      this.email = email;
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

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }
}
