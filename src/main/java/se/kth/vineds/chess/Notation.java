package se.kth.vineds.chess;

import com.paul.game.Game;
import com.paul.game.map.Tile;
import com.paul.game.piece.Bishop;
import com.paul.game.piece.King;
import com.paul.game.piece.Knight;
import com.paul.game.piece.Pawn;
import com.paul.game.piece.Piece;
import com.paul.game.piece.Queen;
import com.paul.game.piece.Rook;
import com.paul.game.piece.movement.Promotion;

public class Notation {

  private Game game;

  private final char[] letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

  public Notation(Game game) {
    this.game = game;
  }

  /**
   * @param algebraicNotation The move in algebraic notation
   * @return -1 for non-promotions, otherwise the corresponding value of the promotion
   * in the Promotion class
   */
  public int getPromotion(String algebraicNotation) {
    if (algebraicNotation.length() == 3 && Character.isAlphabetic(algebraicNotation.charAt(2))) {
      String promoteTo = String.valueOf(algebraicNotation.charAt(2));
      for (Tile t : this.game.board.getTileList()) {
        Tile moving = this.toTile(algebraicNotation.substring(0, algebraicNotation.length() - 1));
        if (t.getPiece() instanceof Pawn && t.getPiece().isAllowedMove(moving)) {
          switch (promoteTo.toLowerCase()) {
            case "q":
              return Promotion.QUEEN;
            case "n":
              return Promotion.KNIGHT;
            case "r":
              return Promotion.ROOK;
            case "b":
              return Promotion.BISHOP;
          }
        }
      }
    }
    return -1;
  }

  public int[] toMoveFromAlgebraicNotation(String algebraicNotation) {
    switch(algebraicNotation.length()) {
      //should only be relevant for pawns.
      case 2:
        for (Tile t : this.game.board.getTileList()) {
          Tile moving = this.toTile(algebraicNotation);
          if (t != null && t.getPiece() instanceof Pawn && t.getPiece().isAllowedMove(moving)) {
            return new int [] {t.getX(), t.getY(), moving.getX(), moving.getY()};
          }
        }
        break;
      case 3:

        //kingside castling, 0-0
        if (algebraicNotation.charAt(1) == '-') {
          King king = this.game.board.getKing(this.game.turn.getActive());
          return new int [] {king.getX(), king.getY(), king.getX() + 2, king.getY()};
        }

        //if the last character is a digit it's just a move
        if (Character.isDigit(algebraicNotation.charAt(2))) {
          // Type|X|Y
          String type = String.valueOf(algebraicNotation.charAt(0));
          for (Tile t : this.game.board.getTileList()) {
            Tile moving = this.toTile(algebraicNotation.substring(1));
            if (t.getPiece().isAllowedMove(moving) && type == this.getPieceCharacter(t.getPiece())) {
              return new int [] {t.getX(), t.getY(), moving.getX(), moving.getY()};
            }
          }
        }
        //otherwise it's a pawn promotion (for example: e8Q,promoting to queen)
        else {
          for (Tile t : this.game.board.getTileList()) {
            Tile moving = this.toTile(algebraicNotation.substring(0, algebraicNotation.length() - 1));
            if (t.getPiece().isAllowedMove(moving)) {
              return new int [] {t.getX(), t.getY(), moving.getX(), moving.getY()};
            }
          }
        }

        break;
      case 4:
        //start x|move type|destination x|destination y
        for (Tile t : this.game.board.getTileList()) {

          //copypasta from toTile
          int startX = 0;
          for (int i = 0; i < letter.length; i++) {
            if (letter[i] == algebraicNotation.charAt(0)) {
              startX = i + 1;
              break;
            }
          }

          String moveType = String.valueOf(algebraicNotation.charAt(1)); //might be useful...

          Tile moving = this.toTile(algebraicNotation.substring(2));
          if (t.getPiece().isAllowedMove(moving) && t.getX() == startX) {
            return new int [] {t.getX(), t.getY(), moving.getX(), moving.getY()};
          }
        }
        break;
      case 5:
        //queenside castling, 0-0-0
        if (algebraicNotation.charAt(1) == '-') {
          King king = this.game.board.getKing(this.game.turn.getActive());
          return new int [] {king.getX(), king.getY(), king.getX() - 2, king.getY()};
        }
        break;
      case 8:
        break;
    }

    //might be worth returning something else if the algebraic move was invalid.
    return new int [] {0, 0, 0, 0};
  }

  public String toAlgebraicNotationFromMove(int[] move) {
    Piece p = this.game.board.getTileAt(move[2], move[3]).getPiece();
    String chessMove = "";
    //just a move, no piece exists at that tile.
    if (p == null) {
      if (p instanceof Pawn) {
        chessMove += this.getPieceCharacter(p);
      }
      chessMove += String.format("%s%d", letter[move[2]], move[3]);

    } else {

    }
    return chessMove;
  }

  public String toAlgebraicNotationTile(int x, int y) {
    return letter[x - 1] + String.valueOf(9 - y);
  }

  public Tile toTile(String algebraicNotation) {
    char[] letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    int x = 0;
    for (int i = 0; i < letter.length; i++) {
      if (letter[i] == algebraicNotation.charAt(0)) {
        x = i + 1;
        break;
      }
    }

    int y = 9 - Integer.valueOf(algebraicNotation.charAt(1));

    return game.board.getTileAt(x, y);
  }

  public String getBoardForsythEdwards() {
    String result = "";

    // Adds piece positions
    int whiteSpaceLength = 0;
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        Piece piece = game.board.getTileAt(x, y).getPiece();

        if (piece == null) {
          whiteSpaceLength++;
        } else {
          if (whiteSpaceLength > 0) {
            result += whiteSpaceLength;
            whiteSpaceLength = 0;
          }

          result += getPieceCharacter(piece);
        }
      }

      if (whiteSpaceLength > 0) {
        result += whiteSpaceLength;
      }
      whiteSpaceLength = 0;

      if (y < 8) {
        result += "/";
      }
    }

    // Adds which player is active, white/black
    result += " " + (game.turn.getActive().isWhite() ? "w" : "b");

    // This adds castling availability. I don't get algebraic notation, so this is bullshit filler
    String availability = "";
    if (!game.board.getKing(game.turn.getWhite()).getHasMoved()) {
      Piece leftRook = game.board.getTileAt(1, 8).getPiece();
      Piece rightRook = game.board.getTileAt(8, 8).getPiece();

      if (rightRook != null && rightRook instanceof Rook && !rightRook.getHasMoved()) {
        availability += "K";
      }

      if (leftRook != null && leftRook instanceof Rook && !leftRook.getHasMoved()) {
        availability += "Q";
      }
    }
    if (!game.board.getKing(game.turn.getBlack()).getHasMoved()) {
      Piece leftRook = game.board.getTileAt(1, 1).getPiece();
      Piece rightRook = game.board.getTileAt(8, 1).getPiece();

      if (rightRook != null && rightRook instanceof Rook && !rightRook.getHasMoved()) {
        availability += "k";
      }

      if (leftRook != null && leftRook instanceof Rook && !leftRook.getHasMoved()) {
        availability += "q";
      }
    }
    result += " " + availability;

    // This thing is supposed to add the square behind a en passant vulnerable pawn if any. But fuck that
    if (game.board.enPassant.getVulnerablePawns().size() == 0) {
      result += " -";
    } else {
      Pawn pawn = game.board.enPassant.getVulnerablePawns().get(0);

      if (pawn.isWhite()) {
        result += " " + toAlgebraicNotationTile(pawn.getX(), pawn.getY() + 1);
      } else {
        result += " " + toAlgebraicNotationTile(pawn.getX(), pawn.getY() - 1);
      }
    }

    // This is the number of half moves since the last capture or pawn advance, I'm just going say it's 10
    result += " " + game.turn.getHalfMove();

    // The number of the full move. It starts at 1, and is incremented after Black's move.
    result += " " + game.turn.getFullMove();

    return result;
  }

  public String getPieceCharacter(Piece p) {
    if (p != null) {
      if (p instanceof Pawn) {
        return p.isWhite() ? "P" : "p";
      } else if (p instanceof Bishop) {
        return p.isWhite() ? "B" : "b";
      } else if (p instanceof Rook) {
        return p.isWhite() ? "R" : "r";
      } else if (p instanceof Knight) {
        return p.isWhite() ? "N" : "n";
      } else if (p instanceof Queen) {
        return p.isWhite() ? "Q" : "q";
      } else if (p instanceof King) {
        return p.isWhite() ? "K" : "k";
      }
    }

    return ".";
  }
}
