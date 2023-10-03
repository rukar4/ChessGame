import chess.ChessGame;
import chess.ChessPiece;
import srcCode.Board;
import srcCode.KingMoves;
import srcCode.Piece;
import srcCode.Position;

public class Test {

   public static void main(String[] args){
      Board chess = new Board();
      Piece rook = new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
      Piece bishop = new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
      Piece king = new KingMoves(ChessGame.TeamColor.WHITE);
      Position position = new Position(1, 8);
//      chess.addPiece(position, rook);
//      chess.addPiece(position, bishop);
      chess.addPiece(position, king);
      king.pieceMoves(chess, position);


      System.out.println(chess.toString());
   }
}