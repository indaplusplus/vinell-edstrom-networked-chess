package se.kth.vineds.chess;

import com.paul.game.listeners.EventListener;
import com.paul.game.piece.Piece;
import com.paul.game.player.Player;

public class ChessOutput implements EventListener {

  @Override
  public void eventPieceKilled(Piece piece, Piece piece1) {

  }

  @Override
  public void eventStalemate() {

  }

  @Override
  public void eventCheckmate(Player player) {

  }

  @Override
  public void eventCheck(Player player) {

  }

  @Override
  public void eventPromotion(Piece piece) {

  }
}
