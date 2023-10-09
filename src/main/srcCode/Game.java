package srcCode;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Game implements ChessGame {
   TeamColor TeamTurn = TeamColor.WHITE;
   Board chessBoard = new Board();

   @Override
   public TeamColor getTeamTurn() {
      return TeamTurn;
   }

   @Override
   public void setTeamTurn(TeamColor team) {
      resetEnPassant(team);
      TeamTurn = team;
   }

   @Override
   public Collection<ChessMove> validMoves(ChessPosition startPosition) {
      Piece piece = (Piece) chessBoard.getPiece(startPosition);
      Collection<ChessMove> pieceMoves = new ArrayList<>();
      for (ChessMove move : piece.pieceMoves(chessBoard, startPosition)) {
         Game testGame = new Game();
         testGame.setBoard(chessBoard);
         ChessPiece testPiece = testGame.getBoard().getPiece(startPosition);

         testGame.movePiece(move, testPiece);
         if (!testGame.isInCheck(piece.getTeamColor())) pieceMoves.add(move);
      }
      if (piece.getPieceType() == ChessPiece.PieceType.KING) {
         checkValidCastles(piece, pieceMoves, startPosition);
      }
      return pieceMoves;
   }

   private void checkValidCastles(Piece king, Collection<ChessMove> validMoves, ChessPosition startPosition) {
      // No valid castles if in check or if king has moved
      if (isInCheck(getTeamTurn()) || king.hasMoved) {
         validMoves.removeIf(move -> move instanceof Castle);
         return;
      }

      ArrayList<Castle.CastleType> castleSides = new ArrayList<>();
      castleSides.add(Castle.CastleType.KING_SIDE);
      castleSides.add(Castle.CastleType.QUEEN_SIDE);

      // Cannot castle through check
      int direction = 1;
      for (Castle.CastleType side : castleSides) {
         if (side == Castle.CastleType.QUEEN_SIDE) direction = -1;
         if (!validMoves.contains(new Move(startPosition, new Position(startPosition.getRow(), startPosition.getColumn() + direction)))) {
            validMoves.removeIf(move -> move instanceof Castle castle && castle.type == side);
         }
      }
   }

   @Override
   public void makeMove(ChessMove move) throws InvalidMoveException {

      ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
      ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
      if (validMoves.contains(move) && piece.getTeamColor() == getTeamTurn()) {
         Piece chessPiece = (Piece) piece;

         ChessMove validMove = validMoves.get(validMoves.indexOf(move));
         movePiece(validMove, chessPiece);

         if (!chessPiece.hasMoved) chessPiece.setHasMoved(true);
         if (chessPiece.getTeamColor() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
         } else {
            setTeamTurn(TeamColor.WHITE);
         }
      } else if (chessBoard.getPiece(move.getStartPosition()) != null) {
         throw new InvalidMoveException("Invalid move for " + chessBoard.getPiece(move.getStartPosition()).getTeamColor() + " " +
                 chessBoard.getPiece(move.getStartPosition()).getPieceType() +
                 " from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString());
      } else {
         throw new InvalidMoveException("Invalid move for " + move.getStartPosition().toString() +
                 " to " + move.getEndPosition().toString() + "\nNo piece found");
      }
   }

   private void movePiece(ChessMove move, ChessPiece piece) {
      Piece chessPiece = (Piece) piece;
      if (move.getPromotionPiece() == null) {
         chessBoard.addPiece(move.getEndPosition(), chessPiece);
         chessBoard.addPiece(move.getStartPosition(), null);

         if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN && !chessPiece.hasMoved) {
            ((PawnMoves) chessPiece).setMovedTwice(
                    Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow()) > 1);
         }
         if (move instanceof Castle castle) {
            Piece rook = (Piece) chessBoard.getPiece(castle.getRookStart());
            chessBoard.addPiece(castle.getRookEnd(), rook);
            chessBoard.addPiece(castle.getRookStart(), null);
            rook.setHasMoved(true);
         }
         if (move instanceof EnPassant enPassant) {
            chessBoard.addPiece(enPassant.pawnToCapture, null);
         }
      } else {
         chessBoard.addPiece(move.getEndPosition(), chessBoard.createNewPiece(move.getPromotionPiece(), chessPiece.getTeamColor()));
         chessBoard.addPiece(move.getStartPosition(), null);
      }
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

   private void resetEnPassant(TeamColor team) {
      for (int i = 1; i <= chessBoard.getLength(); ++i) {
         for (int j = 1; j <= chessBoard.getLength(); ++j) {
            Position position = new Position(i, j);
            Piece piece = (Piece) chessBoard.getPiece(position);
            if (piece instanceof PawnMoves pawn && piece.getTeamColor() == team) {
               pawn.setMovedTwice(false);
            }
         }
      }
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
