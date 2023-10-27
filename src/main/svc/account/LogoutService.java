package svc.account;

import dao.AuthDAO;
import models.AuthToken;
import svc.ErrorConstructor;
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
   public Result logout(AuthToken token) {
      Result result = new Result();

      if (token == null) {
         result.setApiRes(Result.ApiRes.UNAUTHORIZED);
         return result;
      }

      try {
         AuthToken userToken = authDAO.getTokenByUser(token.getUsername());

         if (userToken == null || !userToken.equals(token)) {
            result.setApiRes(Result.ApiRes.UNAUTHORIZED);
            return result;
         }

         authDAO.removeToken(token);
         result.setApiRes(Result.ApiRes.SUCCESS);
         return result;
      } catch (Exception e) {
         ErrorConstructor err = new ErrorConstructor();
         err.errorConstructor(e, "LogoutService", result);

         return result;
      }
   }
}
