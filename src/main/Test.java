import chess.ChessGame;
import chess.ChessPiece;
import srcCode.*;

public class Test {

   public static void main(String[] args){
      Board chess = new Board();
      Piece rook = new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
      Piece bishop = new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
      Piece king = new KingMoves(ChessGame.TeamColor.WHITE);
      Piece knight = new KnightMoves(ChessGame.TeamColor.BLACK);
      Position position = new Position(4, 3);
//      chess.addPiece(position, rook);
//      chess.addPiece(position, bishop);
      chess.addPiece(position, knight);
      knight.pieceMoves(chess, position);


      System.out.println(chess.toString());
   }
}