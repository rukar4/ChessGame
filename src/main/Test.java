import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import srcCode.*;

public class Test {

   public static void main(String[] args) throws InvalidMoveException {
      ChessGame chess = new Game();
      ChessBoard chessBoard = new Board();
//      Piece wKing = new KingMoves(ChessGame.TeamColor.WHITE);
//      Piece bKing = new KingMoves(ChessGame.TeamColor.BLACK);
//
//      chessBoard.addPiece(new Position(5, 6), wKing);
//      chessBoard.addPiece(new Position(6, 8), bKing);
//      chessBoard.addPiece(new Position(3, 6), new RookMoves(ChessGame.TeamColor.BLACK));
//
//      Move kingMove = new Move(new Position(5, 6), new Position(5, 5));
//
//      chess.setBoard(chessBoard);
//      System.out.println(chess.getBoard().toString());

//      chess.makeMove(kingMove);
//      boolean check = chess.isInCheck(ChessGame.TeamColor.WHITE);
//      System.out.printf("Is is check? ------------- %b\n", check);

      chess.getBoard().resetBoard();

      System.out.println(chess.getBoard().toString());

      Move invalid = new Move(new Position(2, 1), new Position(5, 1));

      chess.makeMove(invalid);

      System.out.println(chess.getBoard().toString());
   }
}