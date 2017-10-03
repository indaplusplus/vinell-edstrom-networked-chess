package se.kth.vineds.roles;

import com.paul.game.Game;
import se.kth.cnlib.ChessServer;
import se.kth.vineds.chess.ChessInput;

public class ServerRole extends ChessServer {

  private Game game;

  private ChessInput white = new ChessInput();
  private ChessInput black = new ChessInput();

  public ServerRole() {
    this.game = new Game(white, black);
  }

  @Override
  public void run() {

  }
}
