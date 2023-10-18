package svc.game;

/**
 * JoinGameRequest stores the game id for the game the caller wants to join and the color that they want to join as.
 * The color can be WHITE, BLACK, or null. A null color means the caller will join as an observer.
 */
public class JoinGameRequest {
   String playerColor = null;
   int gameId;

   /**
    * Create JoinGameRequest from given playerColor and the gameId.
    *
    * @param playerColor The color to join game as. If it is empty, the user is added as an observer
    * @param gameId      The game to join
    */
   public JoinGameRequest(String username, String playerColor, int gameId) {
   }
}
