package dao;

import dataAccess.DataAccessException;
import models.Game;
import models.User;
import svc.Result;

import java.util.ArrayList;

/**
 * Data access object for the Game database
 */
public class GameDAO {
   private final ArrayList<Game> tempGameDB = new ArrayList<>();
   private static GameDAO instance;

   private GameDAO() {
   }

   public static GameDAO getInstance(){
      if (instance == null){
         instance = new GameDAO();
      }
      return instance;
   }

   /**
    * Finds and returns the game with the given id from the database
    *
    * @param gameID The id of the game to find
    * @return Return the game found
    * @throws DataAccessException when gameId is invalid or no games exist with that id
    */
   public Game getGame(int gameID) throws DataAccessException {
      for (Game game : tempGameDB) {
         if (game.getGameID() == gameID) {
            return game;
         }
      }
      throw new DataAccessException("Error: game " + gameID + " not found", Result.ApiRes.BAD_REQUEST);
   }

   /**
    * Gets a list of all the games in the database
    *
    * @return A list of games
    * @throws DataAccessException when database is inaccessible
    */
   public ArrayList<Game> getAllGames() throws DataAccessException {
      return tempGameDB;
   }

   /**
    * Inserts a game into the database.
    *
    * @param game The game to be inserted
    * @throws DataAccessException when database is inaccessible
    */
   public void insertGame(Game game) throws DataAccessException {
      // Generate new game id to be one greater than the last game in the db
      int gameID;
      if (tempGameDB.isEmpty()) gameID = 1;
      else gameID = tempGameDB.get(tempGameDB.size() - 1).getGameID() + 1;

      game.setGameID(gameID);
      tempGameDB.add(game);
   }

   /**
    * Sets a given user to a particular color in a game
    *
    * @param username User claiming a color
    * @param color  Color being claimed
    * @param gameId Game that the user is claiming the color
    * @throws DataAccessException if the color is already claimed or the gameId is invalid
    */
   public void claimColor(String username, String color, int gameId) throws DataAccessException {
      Game game = getGame(gameId);
      if (game == null) throw new DataAccessException("Error: game not found", Result.ApiRes.BAD_REQUEST);
      switch (color.toLowerCase()) {
         case "white":
            if (game.getWhiteUsername() == null) {
               game.setWhiteUsername(username);
            } else {
               throw new DataAccessException("Error: already taken", Result.ApiRes.ALREADY_TAKEN);
            }
            break;
         case "black":
            if (game.getBlackUsername() == null) {
               game.setBlackUsername(username);
            } else {
               throw new DataAccessException("Error: already taken", Result.ApiRes.ALREADY_TAKEN);
            }
            break;
         default:
            throw new DataAccessException("Error: invalid color", Result.ApiRes.BAD_REQUEST);
      }
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
   }

   /**
    * Removes a single game from the database
    *
    * @param gameId Game to remove from the database
    * @throws DataAccessException when the gameId is invalid
    */
   public void removeGame(int gameId) throws DataAccessException {
      if (!tempGameDB.removeIf(game -> game.getGameID() == gameId)) {
         throw new DataAccessException("Error: invalid game ID", Result.ApiRes.BAD_REQUEST);
      };
   }

   /**
    * Clears all games from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   public void clearGames() throws DataAccessException {
      tempGameDB.clear();
   }
}
