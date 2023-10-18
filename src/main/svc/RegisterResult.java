package svc;

/**
 * RegisterResult stores the username and a new auth token if the request is valid.
 */
public class RegisterResult extends LoginResult {
   /**
    * Generate register result from given request. Initialize username and authToken if the request is valid,
    * otherwise generate a message for the error. The request is invalid if the username is already taken.
    *
    * @param request The client request
    */
   public RegisterResult(RegisterRequest request) {
      super(request);
   }
}
