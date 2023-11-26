package svc.ServiceClasses;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import svc.game.GetGameResult;

import java.lang.reflect.Type;

public class GetGameResultAdapter implements JsonSerializer<GetGameResult> {
   @Override
   public JsonElement serialize(GetGameResult getGameResult, Type type, JsonSerializationContext context) {
      JsonObject obj = new JsonObject();

      if (getGameResult != null && getGameResult.getGame() != null) {
         JsonObject gameObject = context.serialize(getGameResult.getGame()).getAsJsonObject();

         if (getGameResult.getGame().getGame() != null) {
            gameObject.add("gameData", context.serialize(getGameResult.getGame().getGame()));
         }

         obj.add("game", gameObject);
      }

      return obj;
   }
}
