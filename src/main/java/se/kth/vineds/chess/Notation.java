package se.kth.vineds.chess;

import com.paul.game.Game;
import com.paul.game.map.Tile;

public class Notation {

  private Game game;

  private final char[] letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

  public Notation(Game game) {
    this.game = game;
  }

  public String toAlgebraicNotation(int x, int y) {
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
    return "";
  }
}
