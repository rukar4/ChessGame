package svc;

/**
 * Result class is a super class for all the result classes. This includes the variables message and code which will be
 * utilized by all the subclasses. It manages the default messages for each status code as well.
 */
public class Result {
   private String message;

   /**
    * List of result codes
    */
   public enum ResultCode {
      SUCCESS(200),
      BAD_REQUEST(400),
      UNAUTHORIZED(401),
      ALREADY_TAKEN(403),
      INTERNAL_ERROR(500);

      private final int code;

      /**
       * Initializes the ResultCode to the given code and generates the default message
       *
       * @param code The code that the API returns
       */
      ResultCode(int code) {
         this.code = code;
         generateMessage(code);
      }

      public int getCode() {
         return code;
      }
   }

   /**
    * Generate the default message for each result code
    *
    * @param code The code that the API generated
    */
   private static void generateMessage(int code) {
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
