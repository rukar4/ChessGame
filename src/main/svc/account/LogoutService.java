package svc.account;

import dao.AuthDAO;
import models.AuthToken;
import svc.ErrorLogger;
import svc.Result;

/**
 * LogoutService manages logging out of the application
 */
public class LogoutService {
   AuthDAO authDAO = AuthDAO.getInstance();

   /**
    * Logout the user from the system by removing the auth token from the database.
    *
    * @param token The client request
    * @return the result of the request
    */
   public Result logout(String token) {
      Result result = new Result();

      try {
         authDAO.removeToken(token);
         result.setApiRes(Result.ApiRes.SUCCESS);
         return result;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "LogoutService", result);

         return result;
      }
   }
}
