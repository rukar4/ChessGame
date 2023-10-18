package models;

import chess.ChessGame;

/**
 * Game class manages the creation of a chess game. It stores its id, the usernames of the players, the game name, and
 * the chess game itself.
 */
public class Game {
   private int gameId;
   private String whiteUsername;
   private String blackUsername;
   private String gameName;
   private ChessGame game;

   /**
    * Game constructor initializes the chess game with the given name and game id
    *
    * @param gameName Name of the game
    * @param gameId The game id
    */
   public Game(String gameName, int gameId) {
   }

   /**
    * Convert Game to a string
    *
    * @return Game string
    */
   @Override
   public String toString() {
      return "Game{}"; // JSON?
   }

   public int getGameId() {
      return gameId;
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

   public void setGameName(String gameName) {
      this.gameName = gameName;
   }

   public ChessGame getGame() {
      return game;
   }
}
