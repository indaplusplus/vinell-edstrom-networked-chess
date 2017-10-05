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
import com.paul.game.player.Player;
import java.util.Scanner;

public class LocalPlayer extends Player {

  public Game game;
  public Notation notation;

  public int[] lastMoveInt;
  public String lastMoveString;
  public int promotion = -1;

  @Override
  public int[] turn() {
    print();

    promotion = -1; //reset promotion. promotion() occurs after turn(), so that works
    Scanner s = new Scanner(System.in);

    lastMoveInt = new int[4];

    lastMoveInt[0] = s.nextInt();
    lastMoveInt[1] = s.nextInt();
    lastMoveInt[2] = s.nextInt();
    lastMoveInt[3] = s.nextInt();

    s.close();

    lastMoveString = notation.toAlgebraicNotationFromMove(lastMoveInt);

    return lastMoveInt;
  }

  @Override
  public int promotion() {
    System.out.println("Queen: " + Promotion.QUEEN);
    System.out.println("Knight: " + Promotion.KNIGHT);
    System.out.println("Rook: " + Promotion.ROOK);
    System.out.println("Bishop: " + Promotion.BISHOP);

    Scanner s = new Scanner(System.in);
    promotion = s.nextInt();
    s.close();

    return promotion;
  }

  public void print() {
    System.out.println();
    for (int y = 1; y <= 8; y++) {
      for (int x = 1; x <= 8; x++) {
        printProper(game.board.getTileAt(x, y));
      }
      System.out.println();
    }
  }

  public void printProper(Tile t) {
    Piece p = t.getPiece();

    if (p != null) {
      if (p instanceof Pawn) {
        System.out.print(p.isWhite() ? "P" : "p");
      } else if (p instanceof Bishop) {
        System.out.print(p.isWhite() ? "B" : "b");
      } else if (p instanceof Rook) {
        System.out.print(p.isWhite() ? "R" : "r");
      } else if (p instanceof Knight) {
        System.out.print(p.isWhite() ? "N" : "n");
      } else if (p instanceof Queen) {
        System.out.print(p.isWhite() ? "Q" : "q");
      } else if (p instanceof King) {
        System.out.print(p.isWhite() ? "K" : "k");
      }
    } else {
      System.out.print(".");
    }
  }
}
