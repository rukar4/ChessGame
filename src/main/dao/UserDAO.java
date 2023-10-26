package dao;

import dataAccess.DataAccessException;
import models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Data access object for the User database
 */
public class UserDAO {
   private final Map<String, User> tempUserDB = new HashMap<>();

   /**
    * Retrieve a user from the database
    *
    * @param username The username of the user
    * @return the user with the given username
    * @throws DataAccessException when user is not found
    */
   public User getUser(String username) throws DataAccessException {
      return tempUserDB.get(username);
   }

   /**
    * Retrieve all users in the database
    *
    * @return an array of all the users
    * @throws DataAccessException when database is inaccessible
    */
   public User[] getAllUsers() throws DataAccessException {
      return null;
   }

   /**
    * Insert a user into the database
    *
    * @param user User to insert
    * @throws DataAccessException when user is not found
    */
   public void insertUser(User user) throws DataAccessException {
      tempUserDB.put(user.getUsername(), user);
   }

   /**
    * Remove a user from the database
    *
    * @param username User to remove
    * @throws DataAccessException when user is not found
    */
   public void removeUser(String username) throws DataAccessException {
      tempUserDB.remove(username);
   }

   /**
    * Updates the user's username
    *
    * @param user        The user to update
    * @param newUsername The new username
    * @throws DataAccessException when user is not found
    */
   public void updateUsername(User user, String newUsername) throws DataAccessException {
      User updatedUser = new User(newUsername, user.getPassword());
      removeUser(user.getUsername());
      insertUser(updatedUser);
   }

   /**
    * Updates the user's password
    *
    * @param user    The user to update
    * @param newPass The new password
    * @throws DataAccessException when user is not found
    */
   public void updatePassword(User user, String newPass) throws DataAccessException {
      User updatedUser = new User(user.getUsername(), newPass);
      tempUserDB.put(user.getUsername(), updatedUser);
   }

   /**
    * Clears all users from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   public void clearUsers() throws DataAccessException {
      tempUserDB.clear();
   }
}
