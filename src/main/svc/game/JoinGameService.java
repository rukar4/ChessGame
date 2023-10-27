package svc.game;

import dao.AuthDAO;
import dao.GameDAO;
import svc.ErrorLogger;
import svc.Result;

/**
 * JoinGameService manages joining a chess game from a given result. If the client is authorized and the request
 * is valid, the service adds them to the game requested.
 */
public class JoinGameService {
   GameDAO gameDAO = GameDAO.getInstance();
   AuthDAO authDAO = AuthDAO.getInstance();

   /**
    * Add user to the game. Takes in the client auth token and client request to add them to the game.
    *
    * @param request The client request with desired color and game id
    * @return the result from the request
    */
   public Result joinGame(String authToken, JoinGameRequest request) {
      Result result = new Result();
      int gameID = request.getGameID();
      String color = request.getPlayerColor();

      try {
         String username = authDAO.getToken(authToken).getUsername();

         if (color != null && !color.isEmpty()) {
            gameDAO.claimColor(username, color, gameID);
         }
         result.setApiRes(Result.ApiRes.SUCCESS);
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "JoinGameService", result);
      }
      return result;
   }
}
