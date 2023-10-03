package srcCode;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Piece implements ChessPiece {
   ChessGame.TeamColor color;
   PieceType piece;
   Collection<ChessMove> moves = new HashSet<>();
   boolean hasMoved = false;

   public Piece(ChessGame.TeamColor color, PieceType piece){
      this.color = color;
      this.piece = piece;
   }
   @Override
   public ChessGame.TeamColor getTeamColor() {
      return color;
   }

   @Override
   public PieceType getPieceType() {
      return piece;
   }

   @Override
   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
      moves.clear();
      switch (piece){
         case ROOK:
            moves = new RookMoves(color).pieceMoves(board, myPosition);
            break;
         case KNIGHT:
            moves = new RookMoves(color).pieceMoves(board, myPosition);
            break;
         case BISHOP:
            moves = new BishopMoves(color).pieceMoves(board, myPosition);
            break;
         case KING:
            moves = new KingMoves(color).pieceMoves(board, myPosition);
            break;
         case QUEEN:
            moves = new QueenMoves(color).pieceMoves(board, myPosition);
            break;
         case PAWN:
            moves = new RookMoves(color).pieceMoves(board, myPosition);
            break;
      }
      return moves;
   }

   public boolean getHasMoved(){
      return hasMoved;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (obj instanceof Piece objPiece) {
         return moves.equals(objPiece.moves) && hasMoved == objPiece.hasMoved;
      } else return false;
   }
}
