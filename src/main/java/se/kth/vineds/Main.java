package se.kth.vineds;

import se.kth.vineds.roles.ClientRole;
import se.kth.vineds.roles.ServerRole;

public class Main {

  public static void main(String[] args) {
    new ServerRole().start();
    new ClientRole().start();
  }
}
