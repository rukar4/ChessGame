package srcCode;

import chess.*;

import java.util.Collection;

public class Game implements ChessGame {
   TeamColor turn = TeamColor.WHITE;
   ChessBoard chessBoard = new Board();

   @Override
   public TeamColor getTeamTurn() {
      return turn;
   }

   @Override
   public void setTeamTurn(TeamColor team) {
      if (turn == TeamColor.WHITE){
         turn = TeamColor.BLACK;
      } else {
         turn = TeamColor.WHITE;
      }
   }

   @Override
   public Collection<ChessMove> validMoves(ChessPosition startPosition) {
      ChessPiece piece = chessBoard.getPiece(startPosition);
      Collection<ChessMove> pieceMoves = piece.pieceMoves(chessBoard, startPosition);

      //TODO: Check for valid moves (ie, is the king safe, are other pieces in the way, etc)
      return pieceMoves;
   }

   @Override
   public void makeMove(ChessMove move) throws InvalidMoveException {

   }

   @Override
   public boolean isInCheck(TeamColor teamColor) {
      return false;
   }

   @Override
   public boolean isInCheckmate(TeamColor teamColor) {
      return false;
   }

   @Override
   public boolean isInStalemate(TeamColor teamColor) {
      return false;
   }

   @Override
   public void setBoard(ChessBoard board) {

   }

   @Override
   public ChessBoard getBoard() {
      return chessBoard;
   }
}
