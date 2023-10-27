package svc;

import dataAccess.DataAccessException;

public class ErrorLogger {
   public void errMessage(Exception e, String service, Result result) {
      String message;
      if (e instanceof DataAccessException) {
         message = e.getMessage();
      } else {
         message = "Error: unknown error";
      }
      String errLog = message + "\nError caught in " + service + "\n----------------------------------------\n" + e;
      System.out.println(errLog);

      result.setApiRes(Result.ApiRes.INTERNAL_ERROR);
      result.setMessage(message);
   }
}
