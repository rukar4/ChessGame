package srcCode;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Game implements ChessGame {
   TeamColor TeamTurn = TeamColor.WHITE;
   Board chessBoard = new Board();

   @Override
   public TeamColor getTeamTurn() {
      return TeamTurn;
   }

   @Override
   public void setTeamTurn(TeamColor team) {
      TeamTurn = team;
   }

   @Override
   public Collection<ChessMove> validMoves(ChessPosition startPosition) {
      ChessPiece piece = chessBoard.getPiece(startPosition);
      Collection<ChessMove> pieceMoves = new HashSet<>();
      for (ChessMove move : piece.pieceMoves(chessBoard, startPosition)) {
         Game testGame = new Game();
         testGame.setBoard(chessBoard);
         testGame.movePiece(move, piece);

         if (!testGame.isInCheck(piece.getTeamColor())) pieceMoves.add(move);
      }
      return pieceMoves;
   }

   @Override
   public void makeMove(ChessMove move) throws InvalidMoveException {
      if (validMoves(move.getStartPosition()).contains(move)) {
         ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
         movePiece(move, piece);
      } else if (chessBoard.getPiece(move.getStartPosition()) != null) {
         throw new InvalidMoveException("Invalid move for " + chessBoard.getPiece(move.getStartPosition()).getPieceType() +
                 " from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString());
      } else {
         throw new InvalidMoveException("Invalid move for " + move.getStartPosition().toString() +
                 " to " + move.getEndPosition().toString() + "\nNo piece found");
      }
   }

   private void movePiece(ChessMove move, ChessPiece piece) {
      Piece chessPiece = (Piece) piece;
      chessBoard.addPiece(move.getEndPosition(), chessPiece);
      chessBoard.addPiece(move.getStartPosition(), null);
      if (!chessPiece.hasMoved) chessPiece.setHasMoved(true);
   }

   private Position getKingPosition(TeamColor kingColor) {
      for (int i = 1; i <= chessBoard.getLength(); ++i) {
         for (int j = 1; j <= chessBoard.getLength(); ++j) {
            Position position = new Position(i, j);
            ChessPiece piece = chessBoard.getPiece(position);
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == kingColor) {
               return position;
            }
         }
      }
      System.out.println("Warning: King not found");
      return null;
   }

   @Override
   public boolean isInCheck(TeamColor teamColor) {
      Position kingPosition = getKingPosition(teamColor);
      for (int i = 1; i <= chessBoard.getLength(); ++i) {
         for (int j = 1; j <= chessBoard.getLength(); ++j) {
            Position position = new Position(i, j);
            ChessPiece piece = chessBoard.getPiece(position);
            if (piece == null || piece.getTeamColor() == teamColor) continue;
            for (ChessMove move : piece.pieceMoves(chessBoard, position)) {
               if (move.getEndPosition().equals(kingPosition)) {
                  return true;
               }
            }
         }
      }
      return false;
   }

   @Override
   public boolean isInCheckmate(TeamColor teamColor) {
      return (isInCheck(teamColor) && isInStalemate(teamColor));
   }

   @Override
   public boolean isInStalemate(TeamColor teamColor) {
      for (int i = 1; i <= chessBoard.getLength(); ++i) {
         for (int j = 1; j <= chessBoard.getLength(); ++j) {
            Position position = new Position(i, j);
            ChessPiece piece = chessBoard.getPiece(position);
            if (piece != null && piece.getTeamColor() == teamColor && !validMoves(position).isEmpty()) return false;
         }
      }
      return true;
   }

   @Override
   public void setBoard(ChessBoard board) {
      this.chessBoard = new Board(board);
   }

   @Override
   public ChessBoard getBoard() {
      return chessBoard;
   }
}
