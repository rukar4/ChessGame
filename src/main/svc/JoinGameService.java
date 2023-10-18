package svc;

/**
 * JoinGameService manages joining a chess game from a given result. If the client is authorized and the request
 * is valid, the service adds them to the game requested.
 */
public class JoinGameService {
   /**
    * Create the JoinGameResult from the given request
    *
    * @param request The client request
    * @return the result from the request
    */
   public JoinGameResult joinGame(JoinGameRequest request) {
      return null;
   }

   /**
    * Add user to an existing game in the database.
    *
    * @param gameId      The game to add user to
    * @param username    The user to add
    * @param playerColor The color the user will join as. This can be WHITE, BLACK, or null
    */
   private void addToGame(int gameId, String username, String playerColor) {
   }
}
