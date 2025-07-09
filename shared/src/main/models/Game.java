package models;

import chess.ChessGame;
import game.ChsGame;

import java.util.Objects;

/**
 * Game class manages the creation of a chess game. It stores its id, the usernames of the players, the game name, and
 * the chess game itself.
 */
public class Game {
   /**
    * ID number for the game instance
    */
   private int gameID;

   /**
    * Username for white
    */
   private String whiteUsername;

   /**
    * Username for black
    */
   private String blackUsername;

   /**
    * Name of the game
    */
   private final String gameName;

   /**
    * The chess game object holds all the information to play the game
    */
   transient private ChsGame gameData = new ChsGame();

   /**
    * Game constructor initializes the chess game with the given name
    *
    * @param gameName Name of the game
    */
   public Game(String gameName) {
      this.gameName = gameName;
      gameData.getBoard().resetBoard();
   }

   public Game(int gameID, String whiteUsername, String blackUsername, String gameName) {
      this.gameID = gameID;
      this.whiteUsername = whiteUsername;
      this.blackUsername = blackUsername;
      this.gameName = gameName;
   }

   public String getPlayerFromColor(ChessGame.TeamColor color) {
      switch (color) {
         case WHITE -> {
            return whiteUsername;
         }
         case BLACK -> {
            return blackUsername;
         }
         default -> {
            return null;
         }
      }
   }

   public ChessGame.TeamColor getPlayerColor(String username) {
      if (Objects.equals(whiteUsername, username)) return ChessGame.TeamColor.WHITE;
      else if (Objects.equals(blackUsername, username)) return ChessGame.TeamColor.BLACK;
      else return null;
   }

   public ChessGame.TeamColor getOpponentColor(String username) {
      if (Objects.equals(whiteUsername, username)) return ChessGame.TeamColor.BLACK;
      else if (Objects.equals(blackUsername, username)) return ChessGame.TeamColor.WHITE;
      else return null;
   }

   public String getOpponentName(String username) {
      if (Objects.equals(whiteUsername, username)) return blackUsername;
      else if (Objects.equals(blackUsername, username)) return whiteUsername;
      else return null;
   }

   public int getGameID() {
      return gameID;
   }

   public void setGameID(int gameID) {
      this.gameID = gameID;
   }

   public String getWhiteUsername() {
      return whiteUsername;
   }

   public void setWhiteUsername(String whiteUsername) {
      this.whiteUsername = whiteUsername;
   }

   public String getBlackUsername() {
      return blackUsername;
   }

   public void setBlackUsername(String blackUsername) {
      this.blackUsername = blackUsername;
   }

   public String getGameName() {
      return gameName;
   }

   public ChsGame getGameData() {
      return gameData;
   }

   public void setGameData(ChsGame gameData) {
      this.gameData = gameData;
   }
}
