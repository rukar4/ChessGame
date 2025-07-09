package svc.ServiceClasses;

import dao.AuthDAO;
import dao.GameDAO;
import models.Game;
import svc.Result;
import svc.game.CreateGameRequest;
import svc.game.CreateGameResult;

/**
 * Service to create a new game. A game name is required in the request and then a success result returns the
 * game id for the newly created game.
 */
public class CreateGameService {
   private final AuthDAO authDAO = AuthDAO.getInstance();
   private final GameDAO gameDAO = GameDAO.getInstance();

   /**
    * Create a game from the request.
    * Return 200 and gameId in result on a success.
    * Return error code and message on failure.
    *
    * @param req The client request with the game name
    * @return the result of attempting to create a game
    */
   public CreateGameResult createGame(CreateGameRequest req) {
      CreateGameResult result = new CreateGameResult();

      if (req == null || req.getGameName() == null || req.getGameName().isEmpty()) {
         result.setGameID(null);
         result.setApiRes(Result.ApiRes.BAD_REQUEST);
         return result;
      }

      try {
         Game game = new Game(req.getGameName());
         gameDAO.insertGame(game);

         result.setGameID(game.getGameID());
         result.setApiRes(Result.ApiRes.SUCCESS);
         return result;
      } catch (Exception e) {
         ErrorLogger err = new ErrorLogger();
         err.errMessage(e, "CreateGameService", result);

         return result;
      }
   }
}
