package svc.game;

import dao.GameDAO;
import models.Game;
import svc.ErrorLogger;
import svc.Result;

import java.util.List;

/**
 * Service to list all the games in the database. The request only requires a valid auth token to retrieve the list of
 * games.
 */
public class ListGamesService {
   GameDAO gameDAO = GameDAO.getInstance();

   /**
    * Retrieve a list of all the games in the database.
    * Return 200 with a list of all the games on success.
    * Return error code and error message on failure.
    *
    * @return the result of the request
    */
   public ListGamesResult getGames() {
      ListGamesResult result = new ListGamesResult();

      try {
         List<Game> games = gameDAO.getAllGames();
         result.setGames(games);
         result.setApiRes(Result.ApiRes.SUCCESS);
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "ListGamesService", result);
      }
      return result;
   }
}
