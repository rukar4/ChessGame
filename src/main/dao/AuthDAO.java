package dao;

import dataAccess.DataAccessException;
import dataAccess.Database;
import models.AuthToken;
import svc.Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data access object for auth tokens.
 */
public class AuthDAO {
   private final Database db;
   private final Map<String, AuthToken> tempAuthDB = new HashMap<>();
   private static AuthDAO instance;

   private AuthDAO() {
      this.db = Server.db;
   }

   public static AuthDAO getInstance() {
      if (instance == null) {
         instance = new AuthDAO();
      }
      return instance;
   }

   /**
    * Return a token for a given user
    *
    * @param authToken The token to find
    * @return the authentication token of the user or null if one does not exist for the given user
    * @throws DataAccessException if database is inaccessible
    */
   public AuthToken getToken(String authToken) throws DataAccessException {
      var conn = db.getConnection();
      String sql = "SELECT * FROM authTokens WHERE authToken = ?;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setString(1, authToken);

         try (ResultSet resultSet = query.executeQuery()) {
            if (resultSet.next()) {
               String username = resultSet.getString("username");

               return new AuthToken(username, authToken);
            } else {
               return null;
            }
         }
      } catch (Exception e) {
         throw new DataAccessException("Error: " + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }
   }

   /**
    * Returns all tokens
    *
    * @return all tokens in the database
    * @throws DataAccessException if database is inaccessible
    */
   public List<AuthToken> getAllTokens() throws DataAccessException {
      var conn = db.getConnection();
      String sql = "SELECT * FROM authTokens";
      List<AuthToken> authTokens = new ArrayList<>();

      try (PreparedStatement query = conn.prepareStatement(sql);
           ResultSet resultSet = query.executeQuery()) {

         while (resultSet.next()) {
            AuthToken authToken = new AuthToken(
                    resultSet.getString("username"),
                    resultSet.getString("authToken")
            );
            authTokens.add(authToken);
         }
      } catch (Exception e) {
         throw new DataAccessException("Error: " + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }
      return authTokens;
   }

   /**
    * Inserts a given token into the database
    *
    * @param authToken The token to insert
    * @throws DataAccessException if database is inaccessible
    */
   public void insertToken(AuthToken authToken) throws DataAccessException {
      var conn = db.getConnection();
      String sql = "INSERT INTO authTokens (authToken, username) VALUE (?, ?);";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setString(1, authToken.getAuthToken());
         query.setString(2, authToken.getUsername());

         int tokensInserted = query.executeUpdate();
         if (tokensInserted == 1) {
            System.out.println("A new token was added");
         } else if (tokensInserted > 1) {
            System.out.println("WARNING: " + tokensInserted + " tokens were added");
         } else {
            throw new DataAccessException("Error: unable to insert token");
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
    * Remove the given authentication token from the database
    *
    * @param authToken The token to be removed
    * @throws DataAccessException if the token cannot be found
    */
   public void removeToken(String authToken) throws DataAccessException {
      var conn = db.getConnection();
      String sql = "DELETE FROM authTokens WHERE authToken = ?;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setString(1, authToken);

         int removedRows = query.executeUpdate();
         if (removedRows == 0) throw new DataAccessException("Error: Unable to remove token");

         System.out.println("The token was deleted. Number of tokens removed: " + removedRows);
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
    * Clear all tokens from the database
    *
    * @throws DataAccessException if database is inaccessible
    */
   public void clearTokens() throws DataAccessException {
      var conn = db.getConnection();
      String sql = "DELETE FROM authTokens;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         int removedRows = query.executeUpdate();

         System.out.println("The authTokens database was cleared.\n Number of rows deleted: " + removedRows);
      } catch (Exception e) {
         throw new DataAccessException("Error: " + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }
   }
}
