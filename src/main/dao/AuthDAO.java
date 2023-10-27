package dao;

import dataAccess.DataAccessException;
import models.AuthToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Data access object for auth tokens.
 */
public class AuthDAO {
   private final Map<String, AuthToken> tempAuthDB = new HashMap<>();
   private static AuthDAO instance;

   private AuthDAO() {
   }

   public static AuthDAO getInstance(){
      if (instance == null){
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
      return tempAuthDB.get(authToken);
   }

   /**
    * Return a list of all the authentication tokens in the database
    *
    * @return an array of tokens
    * @throws DataAccessException if database is inaccessible
    */
   public AuthToken[] getTokens() throws DataAccessException {
      return null;
   }

   /**
    * Inserts a given token into the database
    *
    * @param authToken The token to insert
    * @throws DataAccessException if database is inaccessible
    */
   public void insertToken(AuthToken authToken) throws DataAccessException {
      tempAuthDB.put(authToken.getAuthToken(), authToken);
   }

   /**
    * Update the token for a given user to the given token
    *
    * @param newToken The new token to replace the old one
    * @throws DataAccessException if the user does not have a token to update
    */
   public void updateToken(AuthToken newToken) throws DataAccessException {
      tempAuthDB.put(newToken.getUsername(), newToken);
   }

   /**
    * Remove the given authentication token from the database
    *
    * @param authToken The token to be removed
    * @throws DataAccessException if the token cannot be found
    */
   public void removeToken(String authToken) throws DataAccessException {
      tempAuthDB.remove(authToken);
   }

   /**
    * Clear all tokens from the database
    *
    * @throws DataAccessException if database is inaccessible
    */
   public void clearTokens() throws DataAccessException {
      tempAuthDB.clear();
   }
}
