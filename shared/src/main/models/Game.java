package models;

import chess.ChessGame;
import game.ChsGame;

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
   private String gameName;

   /**
    * The chess game object holds all the information to play the game
    */
   transient private ChessGame game = new ChsGame();

   /**
    * Game constructor initializes the chess game with the given name
    *
    * @param gameName Name of the game
    */
   public Game(String gameName) {
      this.gameName = gameName;
      game.getBoard().resetBoard();
   }

   public Game(int gameID, String whiteUsername, String blackUsername, String gameName) {
      this.gameID = gameID;
      this.whiteUsername = whiteUsername;
      this.blackUsername = blackUsername;
      this.gameName = gameName;
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

   public void setGameName(String gameName) {
      this.gameName = gameName;
   }

   public ChessGame getGame() {
      return game;
   }

   public void setGame(ChessGame game) {
      this.game = game;
   }
}
