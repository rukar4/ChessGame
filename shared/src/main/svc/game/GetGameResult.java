package svc.game;

import models.Game;
import svc.Result;

public class GetGameResult extends Result {
   private Game game;

   public GetGameResult() {
   }

   public Game getGame() {
      return game;
   }

   public void setGame(Game game) {
      this.game = game;
   }
}
