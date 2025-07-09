package svc.game;

import svc.Result;

/**
 * CreateGameResult generates a new game and stores the corresponding game id. A 200 response code is given when the
 * game is successfully created.
 */
public class CreateGameResult extends Result {
   /**
    * Generated game ID number if the request is valid
    */
   private Integer gameID;

   /**
    * Construct CreateGameResult.
    *
    * @return the result from the request
    */
   public CreateGameResult result() {
      return null;
   }

   public int getGameID() {
      return gameID;
   }

   public void setGameID(Integer gameID) {
      this.gameID = gameID;
   }
}
