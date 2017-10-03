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

  public ServerRole() {
    this.game = new Game(white, black);
    this.white.game = this.game;
    this.notation = new Notation(game);
  }

  private void playerTurn() {
    /* Signals the game to handle the new turn.
    The game in turn will call upon LocalPlayer. */
    game.turn.turn();

    // Insert code to get the game state, then send it
  }

  private void enemyTurn() throws IOException {
    Move action = this.receive();

    // Insert code to interpret received message as something our engine can process

    game.turn.turn();

    // Insert code to check if it errored
  }

  private void turn() throws IOException {
    if (game.turn.getActive().isWhite() == true) {
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
          this.accept();
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
