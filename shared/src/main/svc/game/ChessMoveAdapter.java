package svc.game;

import chess.ChessMove;
import com.google.gson.*;
import game.*;

import java.lang.reflect.Type;

public class ChessMoveAdapter implements JsonDeserializer<ChessMove> {
   @Override
   public ChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      return ctx.deserialize(el, Move.class);
   }
}
