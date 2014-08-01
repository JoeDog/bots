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
  private int status  = START;
  private AtomicBoolean hiatus   = new AtomicBoolean(false);
  private AtomicBoolean pause    = new AtomicBoolean(false);
  private boolean       over     = false; // XXX: should check life count
  private GameRenderer  engine;

  public GameEngine(Canvas view) {
    this.view       = view;
    this.arena      = Arena.getInstance();
    this.renderer   = new Renderer(arena);
    this.engine     = new GameRenderer();
  }

  public void start() {
    this.engine.start();
  }

  public void stop() {
    this.engine.stop();
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

  /**
   * Renders the game board; if paused, then it 
   * won't render the game board; it just does 
   * dialogs
   */
  public void render(boolean paused, Graphics g) {
    this.setGraphics(g);
    renderer.render(paused, g);
  }

  public void pause() {
    hiatus.set(true);
  }
  
  public void resume() {
    hiatus.set(false);
  }

  public boolean paused() {
    return hiatus.get();
  }

  public synchronized int status () {
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
          Sleep.milliseconds(300);
        } catch (Exception e) {}
        break;
    }
    return this.status;
  }


  private class GameRenderer implements Runnable {
    private Thread  thread;
    private boolean running = false;
    BufferedImage screen    = new BufferedImage(Config.WIDTH, Config.HEIGHT+50, BufferedImage.TYPE_INT_RGB);
    Graphics graphics       = screen.getGraphics();
    Graphics display        = getGraphics();

    public void start() {
      if (!running) {
         System.out.println("starting..."+this.running);
         this.running = true;
         thread = new Thread(this);
         thread.start();
      }
    }

    public void stop() {
      System.out.println("stopping...");
      thread.interrupt();
      running = false;
    }

    public void run() {
      while (running) {
        long delta = 0l;

        view.requestFocus();
        while (true) {
          long lastTime = System.nanoTime();
          graphics.setColor(new Color(205, 201, 201));
          graphics.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

          update((float)(delta / 1000000000.0));
          render(paused(), graphics);

          if (screen != null) {
            if (display != null) {
              display.drawImage(screen, 0, 0, null);
            } else {
              display = getGraphics();
            }
          }
          delta = System.nanoTime() - lastTime;
          if (delta < 20000000L) {
            Sleep.milliseconds((20000000L - delta) / 1000000L);
          }
        }
      }
    }
  }
}
