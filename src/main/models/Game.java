package models;

import chess.ChessGame;


public class Game {
   int gameId;
   String whiteUsername;
   String blackUsername;
   String gameName;
   ChessGame game;

   /**
    * Game constructor generates a gameId and initializes the
    * chess game
    *
    * @param gameName Name of the game
    */
   public Game (String gameName){
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

   public void setGameId(int gameId) {
      this.gameId = gameId;
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
}
