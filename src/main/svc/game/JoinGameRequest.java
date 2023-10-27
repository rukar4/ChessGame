package svc.game;

/**
 * JoinGameRequest stores the game id for the game the caller wants to join and the color that they want to join as.
 * The color can be WHITE, BLACK, or null. A null color means the caller will join as an observer.
 */
public class JoinGameRequest {
   /**
    * Color given in the request
    */
   private String playerColor = null;

   /**
    * Game ID number given in the request
    */
   private int gameID;

   /**
    * Create JoinGameRequest from given playerColor and the gameId.
    */
   public JoinGameRequest() {
   }

   public String getPlayerColor() {
      return playerColor;
   }

   public void setPlayerColor(String playerColor) {
      this.playerColor = playerColor;
   }

   public int getGameID() {
      return gameID;
   }

   public void setGameID(int gameID) {
      this.gameID = gameID;
   }
}
