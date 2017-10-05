package se.kth.vineds.roles;

import com.paul.game.Game;
import java.io.IOException;
import se.kth.cnlib.ChessClient;
import se.kth.inda17plusplus.MoveOuterClass.Move;
import se.kth.vineds.chess.LocalPlayer;
import se.kth.vineds.chess.Notation;
import se.kth.vineds.chess.RemotePlayer;

public class ClientRole extends ChessClient {

  public Notation notation;

  private Game game;

  private RemotePlayer white;
  private LocalPlayer black;

  private boolean lastMoveErrored = false;

  public ClientRole() {
    super("127.0.0.1");
    this.white = new RemotePlayer();
    this.black = new LocalPlayer();

    this.game = new Game(white, black);
    this.notation = new Notation(game);

    this.black.notation = this.notation;
    this.black.game = this.game;
  }

  private void playerTurn() throws IOException {
    game.turn.turn();

    String resultingState = notation.getBoardForsythEdwards();

    this.send(black.lastMoveString, resultingState, lastMoveErrored);

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
    int[] enemyMove= notation.toMoveFromAlgebraicNotation(move);

    white.addMoveToQueue(enemyMove);
    if (promotion != -1) {
      white.addPromotionToQueue(promotion);
    }

    game.turn.turn();

    if (!notation.getBoardForsythEdwards().equals(resultingState)) {
      System.out.println("Their last move errored.");

      lastMoveErrored = true;
    }
  }

  private void turn() throws IOException {
    if (!game.turn.getActive().isWhite()) {
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
          System.out.println("Client waiting to connect");
          this.connect();
          System.out.println("Client connected");
        } else if (this.getSocket().isConnected()) {
          turn();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
