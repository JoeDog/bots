package org.joedog.bots.control;

import java.util.concurrent.TimeUnit;

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
    int status = GameEngine.LOAD;

    while (control.paused()) {
      Sleep.milliseconds(500);
    }

    while (status != GameEngine.DONE) {
      status = control.status();
      switch (status) {
        case GameEngine.INIT:
          control.start();
          break;
        case GameEngine.LOAD:
          control.newGame();
          break;
        case GameEngine.PLAY:
          break;
        case GameEngine.OVER:
          //control.pause();
          break;
        case GameEngine.SCORE:
          break;
        case GameEngine.DONE:
          break;
      }
      Sleep.milliseconds(20);
    } 
  }
}
