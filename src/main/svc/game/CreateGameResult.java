package svc.game;

import models.Game;
import svc.Result;

/**
 * CreateGameResult generates a new game and stores the corresponding game id. A 200 response code is given when the
 * game is successfully created.
 */
public class CreateGameResult extends Result {
   String gameId;

   /**
    * Construct CreateGameResult from the request. If the request is valid, create a new game and set the status code
    * to 200. Otherwise, return a message detailing the error.
    *
    * @param request The client request
    * @return the result from the request
    */
   public CreateGameResult result(CreateGameRequest request) {
      return null;
   }
}
