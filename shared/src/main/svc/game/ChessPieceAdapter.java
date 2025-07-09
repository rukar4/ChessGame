package svc.game;

import chess.ChessPiece;
import com.google.gson.*;
import game.*;

import java.lang.reflect.Type;

public class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
   @Override
   public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
      JsonObject piece = el.getAsJsonObject();

      if (piece.has("piece")) {
         ChessPiece.PieceType pieceType = ChessPiece.PieceType.valueOf(piece.get("piece").getAsString());

         return switch (pieceType) {
            case ROOK -> ctx.deserialize(piece, RookMoves.class);
            case BISHOP -> ctx.deserialize(piece, BishopMoves.class);
            case KING -> ctx.deserialize(piece, KingMoves.class);
            case KNIGHT -> ctx.deserialize(piece, KnightMoves.class);
            case QUEEN -> ctx.deserialize(piece, QueenMoves.class);
            case PAWN -> ctx.deserialize(piece, PawnMoves.class);
         };
      }
      return null;
   }
}
