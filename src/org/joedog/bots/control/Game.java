package org.joedog.bots.control;

import java.util.concurrent.TimeUnit;

import org.joedog.bots.model.Config;
import org.joedog.util.Sleep;

public class Game {
  private GameEngine control;

  public Game(GameEngine control) {
    this.control = control;
  }

  /**
   * Invoked by the GameThread this method starts and 
   * conducts the flow of a game.
   * <p>
   * @param  none
   * @return void
   */
  public synchronized void start() {
    int status = GameEngine.START;

    while (control.isPaused()) {
      try {
        Thread.sleep(500);
      } catch (Exception e) {}
    }

    while (status != GameEngine.DONE) {
      status = control.gameStatus();
      switch (status) {
        case GameEngine.START:
          break;
        case GameEngine.PLAY:
          control.start();
          break;
        case GameEngine.OVER:
          control.pause();
          break;
        case GameEngine.SCORE:
          break;
        case GameEngine.DONE:
          break;
      }
    } 
  }
}
