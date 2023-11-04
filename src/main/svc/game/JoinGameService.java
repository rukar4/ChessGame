package svc.game;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import svc.ErrorLogger;
import svc.Result;

import java.util.Objects;

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
            if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
               result.setApiRes(Result.ApiRes.BAD_REQUEST);
               result.setMessage("Error: invalid color");
               return result;
            }
            gameDAO.claimColor(username, color, gameID);
         } else {
            gameDAO.getGame(gameID);
         }
         result.setApiRes(Result.ApiRes.SUCCESS);
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "JoinGameService", result);
      }
      return result;
   }
}
