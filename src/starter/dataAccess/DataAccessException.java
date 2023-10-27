package dataAccess;

import svc.Result;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
   private Result.ApiRes responseCode = Result.ApiRes.INTERNAL_ERROR;

   public DataAccessException(String message) {
      super(message);
   }

   public DataAccessException(String message, Result.ApiRes responseCode) {
      super(message);
      this.responseCode = responseCode;
   }

   public Result.ApiRes getResponseCode() {
      return responseCode;
   }
}
