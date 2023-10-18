package svc.clearApp;

import svc.Result;

/**
 * ClearAppResult clears the database and returns 200. If it is unable to clear, a 500 is returned.
 * This handles removing all users, games and authTokens.
 */
public class ClearAppResult extends Result {
   /**
    * Generate ClearAppResult when the API is called. If the database is cleared, it returns 200. Otherwise, a 500 error
    * is returned with an error message.
    */
   public ClearAppResult() {
   }

   /**
    * Method to clear the application database. This will handle removing all users, games and authTokens.
    */
   private void clearDatabase() {
   }
}
