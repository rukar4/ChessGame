package dao;

import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;

public class AuthDAO {
   /**
    * Return a token for a given user
    *
    * @param user The user to find the token for
    * @return the authentication token of the user or null if one does not exist for the given user
    * @throws DataAccessException if database is inaccessible
    */
   public AuthToken getToken(User user) throws DataAccessException {
      return null;
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
   }

   /**
    * Update the token for a given user to the given token
    *
    * @param user The user whose token will be updated
    * @param authToken The new token to replace the old one
    * @throws DataAccessException if the user does not have a token to update
    */
   public void updateToken(User user, AuthToken authToken) throws DataAccessException {
   }

   /**
    * Remove the given authentication token from the database
    *
    * @param authToken The token to be removed
    * @throws DataAccessException if the token cannot be found
    */
   public void removeToken(AuthToken authToken) throws DataAccessException {
   }

   /**
    * Clear all tokens from the database
    *
    * @throws DataAccessException if database is inaccessible
    */
   public void clearTokens() throws DataAccessException {
   }
}
