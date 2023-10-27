package svc.clearApp;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import svc.ErrorLogger;
import svc.Result;

/**
 * Service to clear the entire database. Removes all users, games, and authTokens
 */
public class ClearAppService {
   GameDAO gameDAO = GameDAO.getInstance();
   AuthDAO authDAO = AuthDAO.getInstance();
   UserDAO userDAO = UserDAO.getInstance();

   /**
    * Clear the application of all users, games and authTokens. Return 200 on success, or 500 with error message on
    * failure.
    *
    * @return the result of attempting to clear the application
    */
   public Result clearApp() {
      Result result = new Result();

      try {
         gameDAO.clearGames();
         authDAO.clearTokens();
         userDAO.clearUsers();

         result.setApiRes(Result.ApiRes.SUCCESS);
      } catch (Exception e){
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "ClearAppService", result);
      }
      return result;
   }
}
