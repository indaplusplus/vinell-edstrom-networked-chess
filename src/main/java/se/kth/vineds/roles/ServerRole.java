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

  private LocalPlayer white = new LocalPlayer();
  private RemotePlayer black = new RemotePlayer();

  private boolean lastMoveErrored = false;

  public ServerRole() {
    this.game = new Game(white, black);
    this.white.game = this.game;
    this.notation = new Notation(game);
  }

  private void playerTurn() throws IOException {
    game.turn.turn();

    // Unfinished, convert move to algebraic notation
    String move = "";
    String resultingState = notation.getBoardForsythEdwards();

    this.send(move, resultingState, lastMoveErrored);

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

    // Unfinished, interpret the information sent
    boolean promotionOccured = false;
    int[] enemyMove = {0, 0, 0, 0};

    black.addMoveToQueue(enemyMove);
    if (promotionOccured) {
      int promotionValue = 0;

      black.addPromotionToQueue(promotionValue);
    }
    // Unfinished

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
      }
    }
  }
}
