package se.kth.vineds.roles;

import com.paul.game.Game;
import java.io.IOException;
import se.kth.cnlib.ChessServer;
import se.kth.inda17plusplus.MoveOuterClass.Move;
import se.kth.vineds.chess.LocalPlayer;
import se.kth.vineds.chess.RemotePlayer;
import se.kth.vineds.chess.Notation;

public class ServerRole extends ChessServer {

  private Notation notation;

  private Game game;

  private LocalPlayer white;
  private RemotePlayer black;

  private boolean lastMoveErrored = false;

  public ServerRole() {
    this.white = new LocalPlayer();
    this.black = new RemotePlayer();

    this.game = new Game(white, black);
    this.notation = new Notation(game);

    this.white.notation = this.notation;
    this.white.game = this.game;
  }

  private void playerTurn() throws IOException {
    game.turn.turn();

    String resultingState = notation.getBoardForsythEdwards();

    this.send(white.lastMoveString, resultingState, lastMoveErrored);

    lastMoveErrored = false;
  }

  private void enemyTurn() throws IOException {
    Move action = this.receive();

    String move = action.getMove();
    String resultingState = action.getResultingState();
    boolean lastMoveErrored = action.getLastMoveErrored();

    if (lastMoveErrored) {
      System.out.println("Our last move errored.");
    }

    int promotion = notation.getPromotion(move);
    int[] enemyMove = {0, 0, 0, 0};

    black.addMoveToQueue(enemyMove);
    if (promotion != -1) {
      black.addPromotionToQueue(promotion);
    }

    game.turn.turn();

    if (!notation.getBoardForsythEdwards().equals(resultingState)) {
      System.out.println("Their last move errored.");

      lastMoveErrored = true;
    }
  }

  private void turn() throws IOException {
    if (game.turn.getActive().isWhite()) {
      playerTurn();
    } else {
      enemyTurn();
    }
  }

  @Override
  public void run() {
    while(!this.isInterrupted()) {
      try {
        if (this.getSocket() == null) {
          System.out.println("Server waiting to connect");
          this.accept();
          System.out.println("Server connected");
        }

        if (this.getSocket().isConnected()) {
          turn();
        }
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
    }
  }
}
