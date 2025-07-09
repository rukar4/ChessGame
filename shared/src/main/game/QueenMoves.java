package game;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoves extends Piece {

   public QueenMoves(ChessGame.TeamColor color) {
      super(color, PieceType.QUEEN);
   }

   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
      Collection<ChessMove> moves = new HashSet<>();

      RookMoves lateralMoves = new RookMoves(color);
      BishopMoves diagonalMoves = new BishopMoves(color);
      moves.addAll(lateralMoves.pieceMoves(board, start));
      moves.addAll(diagonalMoves.pieceMoves(board, start));
      return moves;
   }
}
