package svc.game;

import models.AuthToken;

/**
 * Service to list all the games in the database. The request only requires a valid auth token to retrieve the list of
 * games.
 */
public class ListGamesService {
   /**
    * Retrieve a list of all the games in the database.
    * Return 200 with a list of all the games on success.
    * Return error code and error message on failure.
    *
    * @param authToken The client's auth token
    * @return the result of the request
    */
   public ListGamesResult getGames(AuthToken authToken) {
      return null;
   }
}
