package de.home_fm;

import de.home_fm.boundary.HomeFmServer;
import de.home_fm.control.App;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  // start point
  public static void main(String[] args) {
    try {
      LOGGER.info("preparing server");
      HomeFmServer server = new HomeFmServer();

      LOGGER.info("preparing app");
      App.init();

      LOGGER.info("starting server");
      server.start();

      LOGGER.info("starting app");
      App.getSingleton().enterMainLoop();

      LOGGER.info("stopping server");
      server.stop();

      LOGGER.info("stopping app");
      App.getSingleton().stop();

      LOGGER.info("goodbye :)");
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, null, ex);
    }
  }
}
