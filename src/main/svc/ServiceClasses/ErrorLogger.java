package svc.ServiceClasses;

import dataAccess.DataAccessException;
import svc.Result;

public class ErrorLogger {
   public void errMessage(Exception e, String service, Result result) {
      String message;
      if (e instanceof DataAccessException) {
         message = e.getMessage();
         result.setApiRes(((DataAccessException) e).getResponseCode());
      } else {
         message = "Error: unknown error";
         result.setApiRes(Result.ApiRes.INTERNAL_ERROR);
      }
      String errLog = message + "\nError caught in " + service + "\n----------------------------------------\n" + e;
      System.out.println(errLog);

      result.setMessage(message);
   }
}
