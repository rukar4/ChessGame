package svc.ServiceClasses;

import dao.GameDAO;
import models.Game;
import svc.Result;
import svc.game.GetGameResult;

public class GetGameService {
   GameDAO gameDAO = GameDAO.getInstance();

   public GetGameResult getGame(int gameID) {
      GetGameResult result = new GetGameResult();

      try {
         Game game = gameDAO.getGame(gameID);
         result.setGame(game);

         result.setApiRes(Result.ApiRes.SUCCESS);

         return result;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "LogoutService", result);

         return result;
      }
   }
}
