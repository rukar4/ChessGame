package svc.game;

import svc.Result;

/**
 * CreateGameResult generates a new game and stores the corresponding game id. A 200 response code is given when the
 * game is successfully created.
 */
public class CreateGameResult extends Result {
   String gameId;

   /**
    * Construct CreateGameResult.
    *
    * @return the result from the request
    */
   public CreateGameResult result() {
      return null;
   }

   public String getGameId() {
      return gameId;
   }

   public void setGameId(String gameId) {
      this.gameId = gameId;
   }
}
