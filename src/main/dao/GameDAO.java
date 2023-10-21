package dao;

import dataAccess.DataAccessException;
import models.Game;
import models.User;

/**
 * Data access object for the Game database
 */
public class GameDAO {
   /**
    * Finds and returns the game with the given id from the database
    *
    * @param gameId The id of the game to find
    * @return Return the game found
    * @throws DataAccessException when gameId is invalid or no games exist with that id
    */
   public Game getGame(int gameId) throws DataAccessException {
      return null;
   }

   /**
    * Gets a list of all the games in the database
    *
    * @return A list of games
    * @throws DataAccessException when database is inaccessible
    */
   public Game[] getAllGames() throws DataAccessException {
      return null;
   }

   /**
    * Inserts a game into the database.
    *
    * @param game The game to be inserted
    * @throws DataAccessException when database is inaccessible
    */
   public void insertGame(Game game) throws DataAccessException {
   }

   /**
    * Sets a given user to a particular color in a game
    *
    * @param player User claiming a color
    * @param color  Color being claimed
    * @param gameId Game that the user is claiming the color
    * @throws DataAccessException if the color is already claimed or the gameId is invalid
    */
   public void claimColor(User player, String color, int gameId) throws DataAccessException {
   }

   /**
    * Updates the game string at the given gameId by converting the given Game object
    * to a string and replacing the game string with the new string
    *
    * @param gameId      The game to be updated
    * @param updatedGame The updated game
    * @throws DataAccessException when the gameId is invalid
    */
   public void updateGame(int gameId, Game updatedGame) throws DataAccessException {
      String dbString = updatedGame.toString();
      //TODO: Add string to DB
   }

   /**
    * Removes a single game from the database
    *
    * @param gameId Game to remove from the database
    * @throws DataAccessException when the gameId is invalid
    */
   public void removeGame(int gameId) throws DataAccessException {
   }

   /**
    * Clears all games from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   public void clearGames() throws DataAccessException {
   }
}
