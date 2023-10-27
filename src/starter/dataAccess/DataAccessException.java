package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
   int responseCode = 500;

   public DataAccessException(String message) {
      super(message);
   }

   public DataAccessException(String message, int responseCode) {
      super(message);
      this.responseCode = responseCode;
   }
}
