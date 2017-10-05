package se.kth.vineds;

import com.paul.game.Game;
import se.kth.vineds.chess.LocalPlayer;
import se.kth.vineds.chess.Notation;
import se.kth.vineds.chess.RemotePlayer;
import se.kth.vineds.roles.ClientRole;
import se.kth.vineds.roles.ServerRole;

public class Main {

  public static void main(String[] args) {
    new ServerRole().start();
    new ClientRole().start();
  }
}
