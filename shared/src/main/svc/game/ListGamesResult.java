package svc.game;

import models.Game;
import svc.Result;

import java.util.List;

/**
 * Result that stores the entire list of games in the database
 */
public class ListGamesResult extends Result {
   /**
    * List of games in the database if the request is valid
    */
   private List<Game> games;

   /**
    * Stores a list of games in addition to the generic result variables
    */
   public ListGamesResult() {
   }

   public List<Game> getGames() {
      return games;
   }

   public void setGames(List<Game> games) {
      this.games = games;
   }
}
