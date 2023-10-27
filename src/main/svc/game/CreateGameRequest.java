package svc.game;

/**
 * Request structure for creating a new game. CreateGameRequest stores the gameName.
 */
public class CreateGameRequest {
   /**
    * Name given in the request
    */
   private String gameName;

   /**
    * Generate CreateGameRequest from the given game name
    */
   public CreateGameRequest() {
   }

   public String getGameName() {
      return gameName;
   }

   public void setGameName(String gameName) {
      this.gameName = gameName;
   }
}
