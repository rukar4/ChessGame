package svc;

/**
 * RegisterRequest contains the information for a prospective user with their username, password (inherited from
 * LoginRequest) and email.
 */
public class RegisterRequest extends LoginRequest {
   String email;

   /**
    * Create new RegisterRequest that initializes the variables
    *
    * @param username The username the client is trying to register as
    * @param password The password the client is using to register the username
    */
   public RegisterRequest(String username, String password, String email) {
      super(username, password);
      this.email = email;
   }
}
