package svc.game;

/**
 * Request structure for creating a new game. CreateGameRequest stores the gameName.
 */
public class CreateGameRequest {
   private String gameName;

   /**
    * Generate CreateGameRequest from the given game name
    *
    * @param gameName The name for the game to be created.
    */
   public CreateGameRequest(String gameName) {
      this.gameName = gameName;
   }

   public String getGameName() {
      return gameName;
   }

   public void setGameName(String gameName) {
      this.gameName = gameName;
   }
}
