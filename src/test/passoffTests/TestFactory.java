package passoffTests;

import chess.*;
import game.*;

public class TestFactory {

   //Chess Functions
   //------------------------------------------------------------------------------------------------------------------
   public static ChessBoard getNewBoard() {
      return new Board();
   }

   public static ChessGame getNewGame() {
      return new Game();
   }

   public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
      switch (type) {
         case PAWN -> {
            return new PawnMoves(pieceColor);
         }
         case ROOK -> {
            return new RookMoves(pieceColor);
         }
         case BISHOP -> {
            return new BishopMoves(pieceColor);
         }
         case KNIGHT -> {
            return new KnightMoves(pieceColor);
         }
         case KING -> {
            return new KingMoves(pieceColor);
         }
         case QUEEN -> {
            return new QueenMoves(pieceColor);
         }
      }
      return null;
   }

   public static ChessPosition getNewPosition(Integer row, Integer col) {
      return new Position(row, col);
   }

   public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
      return new Move(startPosition, endPosition, promotionPiece);
   }
   //------------------------------------------------------------------------------------------------------------------


   //Server API's
   //------------------------------------------------------------------------------------------------------------------
   public static String getServerPort() {
      return "8080";
   }
   //------------------------------------------------------------------------------------------------------------------


   //Websocket Tests
   //------------------------------------------------------------------------------------------------------------------
   public static Long getMessageTime() {
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
      return 3000L;
   }
   //------------------------------------------------------------------------------------------------------------------
}
