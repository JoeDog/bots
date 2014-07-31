package org.joedog.bots.control;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Event;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;

import org.joedog.bots.model.Arena;
import org.joedog.bots.model.Config;
import org.joedog.bots.model.Location;
import org.joedog.bots.actor.Bully;
import org.joedog.bots.view.Renderer;
import org.joedog.util.Sleep;

public class GameEngine {
  public final static int START  = 0;
  public final static int PLAY   = 1;
  public final static int OVER   = 2;
  public final static int SCORE  = 3;
  public final static int DONE   = 4;

  private Thread      thread;
  private Canvas      view;
  private Arena       arena;
  private Renderer    renderer;
  private Graphics    g = null;
  private int x = 0;
  private int y = 0;
  private int status = START;
  private AtomicBoolean hiatus = new AtomicBoolean(false);
  private boolean    over      = false; // XXX: should check life count
  private boolean    running;
  private GameRunner runner;

  public GameEngine(Canvas view) {
    this.view       = view;
    this.arena      = Arena.getInstance();
    this.renderer   = new Renderer(arena);
    this.runner     = new GameRunner();
  }

  public void start() {
    this.running    = true;
    this.runner.start();
  }

  public void clear() {
    this.arena.clear();
  }

  public void slide(int x, int y) {
    this.arena.clear();
    this.arena.takeTurn(this.x, this.y, x, y);
  }

  public void select(int x, int y) {
    this.x = x;
    this.y = y;
    this.arena.clear();
    this.arena.takeTurn(x, y);
    if (x < 32 && y < 32) {
      arena.printActor(x, y);
      System.out.println(arena.toString());
    } else {
      arena.printActor(x, y);
    }
  }

  public void update(float deltaTime) {
    //controller.update(deltaTime);
  }

  public void setGraphics(Graphics g) {
    if (this.g == null) {
     this.g = g;
    }
  }

  public Graphics getGraphics() {
    return this.view.getGraphics();
  }

  public void render() {
    if (this.g != null) {
      renderer.render(this.g);
    }
  }

  public void render(Graphics g) {
    this.setGraphics(g);
    renderer.render(g);
  }

  public synchronized void pause() {
    hiatus.set(true);
  }

  public synchronized void pause(boolean b) {
    hiatus.set(b);
  }

  public synchronized boolean isPaused() {
    return hiatus.get();
  }

  public synchronized int gameStatus () {
    switch (this.status) {
      case START:
        if (arena.isReady()) 
          this.status = PLAY;
        break;
      case PLAY:
        if (arena.getTurns() == 0) {
          this.status = OVER;
        }
        break;
      case OVER:
        break;
      case SCORE:
        this.over = false;
        break;
      case DONE:
        /**
         * we should loop in this case  
         * until the user selects exit
         * or gains a life
         */
        this.over = true;
        try {
          Sleep.sleep(300);
        } catch (Exception e) {}
        break;
    }
    return this.status;
  }


  private class GameRunner extends Thread {
    BufferedImage screen = new BufferedImage(Config.WIDTH, Config.HEIGHT+50, BufferedImage.TYPE_INT_RGB);
    Graphics graphics    = screen.getGraphics();
    Graphics display     = getGraphics();

    public void run() {
      while (running) {
        view.requestFocus();

        while (isPaused()) {
          try {
            Thread.sleep(500);
          } catch (Exception e) {}
        }

        long delta = 0l;

        int x = 5;
        while (true) {
          long lastTime = System.nanoTime();
          graphics.setColor(new Color(205, 201, 201));
          graphics.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

          update((float)(delta / 1000000000.0));
          render(graphics);

          if (screen != null) {
            if (display != null) {
              display.drawImage(screen, 0, 0, null);
            } else {
              display = getGraphics();
            }
          }
          delta = System.nanoTime() - lastTime;
          if (delta < 20000000L) {
            try {
              Thread.sleep((20000000L - delta) / 1000000L);
            } catch (Exception e) {
              // It's an interrupted exception, and nobody cares
            }
          }
          Sleep.sleep(30);
          if (isPaused()) x--;
          if (x == 0) break;
        }
      }
    }
  }
}
