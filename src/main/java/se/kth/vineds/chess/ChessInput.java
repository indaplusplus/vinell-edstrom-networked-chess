package se.kth.vineds.chess;

import com.paul.game.player.Player;
import java.util.LinkedList;
import java.util.Queue;

public class ChessInput extends Player {

  private Queue<int[]> moveQueue = new LinkedList<>();
  private Queue<Integer> promotionQueue = new LinkedList<>();


  public void addMoveToQueue(int[] move) {
    this.moveQueue.add(move);
  }

  public int getMoveQueueSize() {
    return this.moveQueue.size();
  }

  public void addPromotionToQueue(int promotion) {
    this.promotionQueue.add(promotion);
  }

  @Override
  public int[] turn() {
    return moveQueue.remove();
  }

  @Override
  public int promotion() {
    return promotionQueue.remove();
  }
}
