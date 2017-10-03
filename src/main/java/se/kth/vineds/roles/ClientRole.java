package se.kth.vineds.roles;

import se.kth.cnlib.ChessClient;

public class ClientRole extends ChessClient {

  public ClientRole() {
    super("127.0.0.1");
  }

  @Override
  public void run() {
    // This will be filled in when an outline for ServerRole is finished
  }
}
