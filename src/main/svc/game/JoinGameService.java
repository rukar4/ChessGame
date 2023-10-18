package svc.game;

import svc.Result;

/**
 * JoinGameService manages joining a chess game from a given result. If the client is authorized and the request
 * is valid, the service adds them to the game requested.
 */
public class JoinGameService {
   /**
    * Add user to the game. Takes in the client username and client request to add them to the game.
    *
    * @param username The user trying to join
    * @param request  The client request with desired color and game id
    * @return the result from the request
    */
   public Result joinGame(String username, JoinGameRequest request) {
      return null;
   }

   /**
    * Add user to an existing game in the database.
    *
    * @param gameId      The game to add the user to
    * @param username    The user to add
    * @param playerColor The color the user will join as. This can be WHITE, BLACK, or null
    */
   private void addToGame(int gameId, String username, String playerColor) {
   }
}
