package se.kth.vineds.roles;

import com.paul.game.Game;
import java.io.IOException;
import se.kth.cnlib.ChessClient;
import se.kth.inda17plusplus.MoveOuterClass.Move;
import se.kth.vineds.chess.LocalPlayer;
import se.kth.vineds.chess.Notation;
import se.kth.vineds.chess.RemotePlayer;

public class ClientRole extends ChessClient {

  private Notation notation;

  private Game game;

  private RemotePlayer white = new RemotePlayer();
  private LocalPlayer black = new LocalPlayer();

  private boolean lastMoveErrored = false;

  public ClientRole() {
    super("127.0.0.1");

    this.game = new Game(white, black);
    this.black.game = this.game;
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

    white.addMoveToQueue(enemyMove);
    if (promotionOccured) {
      int promotionValue = 0;

      white.addPromotionToQueue(promotionValue);
    }
    // Unfinished

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
