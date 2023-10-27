package svc.account;

/**
 * RegisterRequest contains the information for a prospective user with their username, password (inherited from
 * LoginRequest) and email.
 */
public class RegisterRequest extends LoginRequest {
   /**
    * Email given in the request
    */
   String email;

   /**
    * RegisterRequest includes all the variables from LoginRequest and a new email
    */
   public RegisterRequest(String username, String password, String email) {
      super(username, password);
      this.email = email;
   }
}
