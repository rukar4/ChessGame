package dao;

import dataAccess.DataAccessException;
import models.*;
import svc.*;

public class GameDAO {
   /**
    * Finds and returns the game with the given id from the database
    *
    * @param gameId The id of the game to find
    * @return Return the game found
    * @throws DataAccessException when database is inaccessible
    */
   Game getGame(int gameId) throws DataAccessException {
      return null;
   }

   /**
    * Gets a list of all the games in the database
    *
    * @return A list of games
    * @throws DataAccessException when database is inaccessible
    */
   Game[] getAllGames() throws DataAccessException {
      return null;
   }

   /**
    * Creates a new game in the database
    *
    * @return The newly created game
    * @throws DataAccessException when database is inaccessible
    */
   Game createGame() throws DataAccessException {
      return null;
   }

   /**
    * Sets a given user to a particular color in a game
    *
    * @param player User claiming a color
    * @param color Color being claimed
    * @param gameId Game that the user is claiming the color
    * @throws DataAccessException when database is inaccessible
    */
   void claimColor(User player, String color, int gameId) throws DataAccessException {
   }

   /**
    * Removes a single game from the database
    *
    * @param game Game to remove from the database
    * @throws DataAccessException when database is inaccessible
    */
   void removeGame(Game game) throws DataAccessException {
   }

   /**
    * Clears all games from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   void clearGames() throws DataAccessException {
   }
}
