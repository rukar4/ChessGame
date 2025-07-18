package dao;

import dataAccess.DataAccessException;
import dataAccess.Database;
import models.User;
import svc.Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the User database
 */
public class UserDAO {
   private static UserDAO instance;
   private final Database db;

   private UserDAO() {
      this.db = Server.db;
   }

   public static UserDAO getInstance() {
      if (instance == null) {
         instance = new UserDAO();
      }
      return instance;
   }

   /**
    * Retrieve a user from the database
    *
    * @param username The username of the user
    * @return the user with the given username
    * @throws DataAccessException when user is not found
    */
   public User getUser(String username) throws DataAccessException {
      var conn = db.getConnection();
      String sql = "SELECT password, email FROM users WHERE username = ?;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setString(1, username);

         try (ResultSet resultSet = query.executeQuery()) {
            if (resultSet.next()) {
               String password = resultSet.getString("password");
               String email = resultSet.getString("email");

               return new User(username, password, email);
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
    * Retrieve all users in the database
    *
    * @return a list of all the users
    * @throws DataAccessException when database is inaccessible
    */
   public List<User> getAllUsers() throws DataAccessException {
      var conn = db.getConnection();
      String sql = "SELECT * FROM users";
      List<User> users = new ArrayList<>();

      try (PreparedStatement query = conn.prepareStatement(sql);
           ResultSet resultSet = query.executeQuery()) {

         while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email")
            );
            users.add(user);
         }
      } catch (Exception e) {
         throw new DataAccessException("Error: " + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }
      return users;
   }

   /**
    * Insert a user into the database
    *
    * @param user User to insert
    * @throws DataAccessException when user is not found
    */
   public void insertUser(User user) throws DataAccessException {
      var conn = db.getConnection();
      String sql = "INSERT INTO users (username, password, email) VALUE (?, ?, ?);";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.setString(1, user.getUsername());
         query.setString(2, user.getPassword());
         query.setString(3, user.getEmail());

         int usersInserted = query.executeUpdate();
         if (usersInserted > 1) {
            System.out.println("WARNING: " + usersInserted + " users were added");
         } else if (usersInserted != 1) {
            throw new DataAccessException("Error: unable to insert user");
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
    * Clears all users from the database
    *
    * @throws DataAccessException when database is inaccessible
    */
   public void clearUsers() throws DataAccessException {
      var conn = db.getConnection();
      String sql = "DELETE FROM users;";

      try (PreparedStatement query = conn.prepareStatement(sql)) {
         query.executeUpdate();
      } catch (Exception e) {
         throw new DataAccessException("Error: " + e.getMessage());
      } finally {
         db.returnConnection(conn);
      }
   }
}
