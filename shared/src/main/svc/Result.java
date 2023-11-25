package svc;

/**
 * Result class is a super class for all the result classes. This includes the variables message and code which will be
 * utilized by all the subclasses. It manages the default messages for each status code as well.
 */
public class Result {
   /**
    * Result message for the given request
    */
   private String message;

   /**
    * Response code for the given request
    */
   private transient ApiRes apiRes;

   /**
    * Generic constructor for results without a special response body
    */
   public Result() {
   }

   /**
    * Generate the default message for each result code
    *
    * @param apiCode The code that the API generated
    */
   private void generateMessage(ApiRes apiCode) {
      switch (apiCode) {
         case SUCCESS -> message = null;
         case BAD_REQUEST -> message = "Error: bad request";
         case UNAUTHORIZED -> message = "Error: unauthorized";
         case ALREADY_TAKEN -> message = "Error: already taken";
         case INTERNAL_ERROR -> message = "Error: internal server error";
         case NOT_FOUND -> message = "Error: not found";
      }
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public ApiRes getApiRes() {
      return apiRes;
   }

   public void setApiRes(ApiRes apiRes) {
      this.apiRes = apiRes;
      generateMessage(apiRes);
   }

   /**
    * List of result codes
    */
   public enum ApiRes {
      /**
       * If the result is successful, return 200
       */
      SUCCESS(200),

      /**
       * If the request is incorrectly formatted, return 400
       */
      BAD_REQUEST(400),

      /**
       * If the user is not authorized, return 401
       */
      UNAUTHORIZED(401),

      /**
       * If the requested name (ie username, game name, etc.) is already in use, return 403
       */
      ALREADY_TAKEN(403),

      /**
       * If the requested endpoint does not exist, return 404
       */
      NOT_FOUND(404),

      /**
       * If there is an error in the code or the request was unable to be fulfilled, or an unknown error occurred,
       * return 500
       */
      INTERNAL_ERROR(500);

      /**
       * The result code
       */
      private final int code;

      /**
       * Initializes the ResultCode to the given code and generates the default message
       *
       * @param code The code that the API returns
       */
      ApiRes(int code) {
         this.code = code;
      }

      public int getCode() {
         return code;
      }
   }
}
