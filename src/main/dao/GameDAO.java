package dao;

import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import dataAccess.Database;
import game.ChsGame;
import models.Game;
import org.junit.jupiter.api.Assertions;
import svc.Result;
import svc.Server;
import svc.game.ChessPieceAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the Game database
 */
public class GameDAO {
   private static GameDAO instance;
   private final Database db;
   private final Gson gson = new Gson();

   private GameDAO() {
      this.db = Server.db;
   }

   public static GameDAO getInstance() {
      if (instance == null) {
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
      var conn = db.getConnection();
      String sql = "SELECT * FROM games WHERE gameID = ?;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setInt(1, gameID);

         try (ResultSet resultSet = query.executeQuery()) {
            if (resultSet.next()) {
               String gameName = resultSet.getString("gameName");

               Game game = new Game(gameName);

               game.setGameID(resultSet.getInt("gameID"));
               game.setWhiteUsername(resultSet.getString("white"));
               game.setBlackUsername(resultSet.getString("black"));

               String json = resultSet.getString("gameData");
               var builder = new GsonBuilder();
               builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());

               game.setGameData(builder.create().fromJson(json, ChsGame.class));

               return game;
            } else {
               throw new DataAccessException("Error: game " + gameID + " not found", Result.ApiRes.BAD_REQUEST);
            }
         }
      } catch (Exception e) {
         if (e instanceof DataAccessException) {
            throw (DataAccessException) e;
         } else {
            throw new DataAccessException("Error: " + e.getMessage());
         }
      } finally {
         db.returnConnection(conn);
      }
   }

   /**
    * Gets a list of all the games in the database
    *
    * @return A list of games
    * @throws DataAccessException when database is inaccessible
    */
   public List<Game> getAllGames() throws DataAccessException {
      var conn = db.getConnection();
      String sql = "SELECT gameID, white, black, gameName FROM games ORDER BY gameID;";
      List<Game> games = new ArrayList<>();

      try (PreparedStatement query = conn.prepareStatement(sql);
           ResultSet resultSet = query.executeQuery()) {

         while (resultSet.next()) {
            Game game = new Game(
                    resultSet.getInt("gameID"),
                    resultSet.getString("white"),
                    resultSet.getString("black"),
                    resultSet.getString("gameName")
            );
            games.add(game);
         }
      } catch (Exception e) {
         throw new DataAccessException("Database error in getAllGames\n" + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }

      return games;
   }

   /**
    * Inserts a game into the database.
    *
    * @param game The game to be inserted
    * @throws DataAccessException when database is inaccessible
    */
   public void insertGame(Game game) throws DataAccessException {
      var conn = db.getConnection();
      String sql =
              "INSERT INTO games (gameName, white, black, gameData) VALUE (?, ?, ?, ?);";

      try (PreparedStatement query = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         query.setString(1, game.getGameName());
         query.setString(2, game.getWhiteUsername());
         query.setString(3, game.getBlackUsername());
         query.setString(4, gson.toJson(game.getGameData()));

         int gamesInserted = query.executeUpdate();
         if (gamesInserted == 1) {
            try (ResultSet generatedKeys = query.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                  int gameID = generatedKeys.getInt(1);
                  game.setGameID(gameID);
               }
            }
         } else if (gamesInserted > 1) {
            System.out.println("WARNING: " + gamesInserted + " games were added");
            try (ResultSet generatedKeys = query.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                  int gameID = generatedKeys.getInt(1);
                  game.setGameID(gameID);
               }
            }
         } else {
            throw new DataAccessException("Error: unable to insert game");
         }
      } catch (Exception e) {
         if (e instanceof DataAccessException) {
            throw (DataAccessException) e;
         } else {
            throw new DataAccessException("Error: " + e.getMessage());
         }
      } finally {
         db.returnConnection(conn);
      }
   }

   /**
    * Sets a given user to a particular color in a game
    *
    * @param username User claiming a color
    * @param color    Color being claimed
    * @param gameID   Game that the user is claiming the color
    * @throws DataAccessException if the color is already claimed or the gameID is invalid
    */
   public void claimColor(String username, String color, int gameID) throws DataAccessException {
      // Check if the game exists. If it doesn't, the getGame function throws a BAD_REQUEST error
      getGame(gameID);

      // After verifying the game exists, we try to insert the user as the desired color
      var conn = db.getConnection();

      // Color should always be white or black from the JoinGameService
      try {
         Assertions.assertTrue(color.equalsIgnoreCase("white") || color.equalsIgnoreCase("black"));
      } catch (AssertionError e) {
         throw new DataAccessException("Error: invalid color", Result.ApiRes.BAD_REQUEST);
      }

      String sql = "UPDATE games SET " + color + " = ? WHERE gameID = " + gameID + " AND " + color + " IS NULL;";

      try (PreparedStatement query = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         query.setString(1, username);

         int gamesUpdated = query.executeUpdate();

         if (gamesUpdated > 1) {
            System.out.println("WARNING: " + gamesUpdated + " games were updated");
         } else if (gamesUpdated != 1) {
            throw new DataAccessException("Error: already taken", Result.ApiRes.ALREADY_TAKEN);
         }
      } catch (Exception e) {
         if (e instanceof DataAccessException) {
            throw (DataAccessException) e;
         } else {
            throw new DataAccessException("Error: " + e.getMessage());
         }
      } finally {
         db.returnConnection(conn);
      }
   }

   /**
    * Updates the game string at the given gameID by converting the given Game object
    * to a string and replacing the game string with the new string
    *
    * @param gameID      The game to be updated
    * @param updatedGame The updated game
    * @throws DataAccessException when the gameID is invalid
    */
   public void updateGame(int gameID, Game updatedGame) throws DataAccessException {
      // Check if the game exists. If it doesn't, the getGame function throws a BAD_REQUEST error
      getGame(gameID);

      var conn = db.getConnection();
      String sql =
              "UPDATE games SET gameData = ? WHERE gameID = ?;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setString(1, gson.toJson(updatedGame.getGameData()));
         query.setInt(2, gameID);

         int updatedRows = query.executeUpdate();
         if (updatedRows == 0) {
            throw new DataAccessException("Error: Unable to update game " + gameID);
         }

      } catch (Exception e) {
         if (e instanceof DataAccessException) {
            throw (DataAccessException) e;
         } else {
            throw new DataAccessException("Error: " + e.getMessage());
         }
      } finally {
         db.returnConnection(conn);
      }
   }

   /**
    * Clears all games from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   public void clearGames() throws DataAccessException {
      var conn = db.getConnection();
      String sql = "DELETE FROM games;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.executeUpdate();
      } catch (Exception e) {
         throw new DataAccessException("Error: " + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }
   }
}
