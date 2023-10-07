import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import srcCode.*;

public class Test {

   public static void main(String[] args) throws InvalidMoveException {
      ChessGame chess = new Game();
      ChessBoard chessBoard = new Board();
      Piece wKing = new KingMoves(ChessGame.TeamColor.WHITE);
      Piece wRook = new RookMoves(ChessGame.TeamColor.WHITE);
//      Piece bKing = new KingMoves(ChessGame.TeamColor.BLACK);
//
      chessBoard.addPiece(new Position(1, 5), wKing);
      chessBoard.addPiece(new Position(1, 1), wRook);
      chessBoard.addPiece(new Position(1, 8), wRook);

      chess.setBoard(chessBoard);

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

      System.out.println(chess.getBoard().toString());

      Move move = new Move(new Position(1, 5), new Position(1, 7));

      chess.makeMove(move);

//      move = new Move(new Position(7, 4), new Position(5, 4));
//
//      chess.makeMove(move);
//
//      move = new Move(new Position(1, 2), new Position(3, 3));
//
//      chess.makeMove(move);

      System.out.println(chess.getBoard().toString());
   }
}