package dao;

import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import models.User;
import svc.Result;

import java.security.cert.CertificateRevokedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    * @param gameId The id of the game to find
    * @return Return the game found
    * @throws DataAccessException when gameId is invalid or no games exist with that id
    */
   public Game getGame(int gameId) throws DataAccessException {
      for (Game game : tempGameDB) {
         if (game.getGameId() == gameId) {
            return game;
         }
      }
      throw new DataAccessException("Error: game " + gameId + " not found");
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
      // Generate new game id to be one greater than the last game in the db
      int gameId = tempGameDB.get(tempGameDB.size() - 1).getGameId() + 1;
      tempGameDB.add(game);
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
      Game game = getGame(gameId);
      if (game == null) throw new DataAccessException("Error: game not found");
      switch (color.toLowerCase()) {
         case "white":
            if (game.getWhiteUsername() == null) {
               game.setWhiteUsername(player.getUsername());
            } else {
               throw new DataAccessException("Error: already taken", Result.ApiRes.ALREADY_TAKEN);
            }
            break;
         case "black":
            if (game.getBlackUsername() == null) {
               game.setBlackUsername(player.getUsername());
            } else {
               throw new DataAccessException("Error: already taken", Result.ApiRes.ALREADY_TAKEN);
            }
            break;
         default:
            throw new DataAccessException("Error: invalid color");
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
      if (!tempGameDB.removeIf(game -> game.getGameId() == gameId)) {
         throw new DataAccessException("Error: invalid game ID");
      };
   }

   /**
    * Clears all games from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   public void clearGames() throws DataAccessException {
   }
}
