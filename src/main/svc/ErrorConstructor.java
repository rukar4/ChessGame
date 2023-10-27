package svc;

import dataAccess.DataAccessException;

public class ErrorConstructor {
   public void errorConstructor(Exception e, String service, Result result) {
      String message;
      if (e instanceof DataAccessException) {
         message = "Error: error accessing the database";
      } else {
         message = "Error: unknown error";
      }
      System.out.println(message + "\nError caught in the %s---------------------------\n" + e);

      result.setApiRes(Result.ApiRes.INTERNAL_ERROR);
      result.setMessage(message);
   }
}
