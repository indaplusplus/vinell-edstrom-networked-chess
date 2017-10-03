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

public class Notation {

  private Game game;

  private final char[] letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

  public Notation(Game game) {
    this.game = game;
  }

  public int[] toMoveFromAlgebraicNotation(String algebraicNotation) {
    return new int [] {0, 0, 0, 0};
  }

  public String toAlgebraicNotationFromMove(int[] move) {
    return "";
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
    result += " KQkq";

    // This thing is supposed to add the square behind a en passant vulnerable pawn if any. But fuck that
    result += " -";

    // This is the number of half moves since the last capture or pawn advance, I'm just going say it's 10
    result += " 10";

    // The number of the full move. It starts at 1, and is incremented after Black's move.
    result += " 10";

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
