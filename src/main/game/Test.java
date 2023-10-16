package game;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;

public class Test {

   public static void main(String[] args) throws InvalidMoveException {
      ChessGame chess = new Game();
      ChessBoard chessBoard = new Board();
      Piece wKing = new KingMoves(ChessGame.TeamColor.WHITE);
      Piece wRook = new RookMoves(ChessGame.TeamColor.WHITE);
      Piece bKing = new KingMoves(ChessGame.TeamColor.BLACK);
      Piece bishop = new BishopMoves(ChessGame.TeamColor.WHITE);

//      chessBoard.addPiece(new Position(1, 5), wKing);
      chessBoard.addPiece(new Position(4, 5), bishop);
//      chessBoard.addPiece(new Position(1, 8), wRook);

      final long startTime = System.currentTimeMillis();
      bishop.pieceMoves(chessBoard, new Position(4, 5));
      final long endTime = System.currentTimeMillis();
      System.out.println("Total execution time: " + (endTime - startTime));


      Move bishopMove = new Move(new Position(4, 5), new Position(5,6));
      System.out.println(bishopMove);

      System.out.println(chessBoard);
   }
}