package svc.account;

/**
 * RegisterRequest contains the information for a prospective user with their username, password (inherited from
 * LoginRequest) and email.
 */
public class RegisterRequest extends LoginRequest {
   String email;

   /**
    * RegisterRequest includes all the variables from LoginRequest and a new email
    */
   public RegisterRequest() {

   }
}
