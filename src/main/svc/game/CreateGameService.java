package svc.game;

import models.Game;

/**
 * Service to create a new game. A game name is required in the request and then a success result returns the
 * game id for the newly created game.
 */
public class CreateGameService {
   /**
    * Create a game from the request.
    * Return 200 and gameId in result on a success.
    * Return error code and message on failure.
    *
    * @param request The client request with the game name
    * @return the result of attempting to create a game
    */
   public CreateGameResult createGame(CreateGameRequest request) {
      return null;
   }

   /**
    * Method to create a new game with the given game name. The game id will be determined by the number of games in the
    * database.
    *
    * @param gameName The given name from the request
    * @return the newly created Game object
    */
   private Game createGame(String gameName) {
      return null;
   }
}
