package org.joedog.bots.control;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;

import org.joedog.bots.model.Config;
import org.joedog.util.Sleep;

public class Game {
  private GameEngine control;
  private Canvas     view;

  public Game(GameEngine control, Canvas view) {
    this.control = control;
    this.view    = view;
  }

   /**
   * Invoked by the GameThread this method starts and 
   * conducts the flow of a game.
   * <p>
   * @param  none
   * @return void
   */
  public synchronized void start() {
    BufferedImage screen = new BufferedImage(Config.WIDTH, Config.HEIGHT+50, BufferedImage.TYPE_INT_RGB);
    Graphics graphics    = screen.getGraphics();
    Graphics display     = view.getGraphics();
    view.requestFocus();

    while (control.isPaused()) {
      try {
        Thread.sleep(500);
      } catch (Exception e) {}
    }

    long delta = 0l;

    int x = 0;
    while (true) {
      long lastTime = System.nanoTime();

      graphics.setColor(new Color(205, 201, 201));
      graphics.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

      control.update((float)(delta / 1000000000.0));
      control.render(graphics);

      if (screen != null) {
        display.drawImage(screen, 0, 0, null);
      }

      delta = System.nanoTime() - lastTime;
      if (delta < 20000000L) {
        try {
          Thread.sleep((20000000L - delta) / 1000000L);
        } catch (Exception e) {
          // It's an interrupted exception, and nobody cares
        }
      }

      x = (x < 26) ? x+1 : 0;
    }
  }
}

