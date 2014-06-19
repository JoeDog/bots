package org.joedog.bots;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.joedog.bots.control.GameEngine;
import org.joedog.bots.model.Config;
import org.joedog.bots.model.Location;

public class Main extends Applet implements Runnable, MouseListener {
  private static final long serialVersionUID = -2666347118493388423L;
  private GameEngine engine = new GameEngine();

  public void init() {
    this.addMouseListener(this);
    
    this.setFocusable(true);
  }

  public void start() {
    new Thread(this).start();
  }

  public void run() {
    
    setSize(Config.WIDTH, Config.HEIGHT+50); // for appletviewer (remove)

    BufferedImage screen = new BufferedImage(Config.WIDTH, Config.HEIGHT+50, BufferedImage.TYPE_INT_RGB);
    Graphics g = screen.getGraphics();
    Graphics appletGraphics = getGraphics();

    long delta = 0l;
   
    int x = 0; 
    while (true) {
      long lastTime = System.nanoTime();

      g.setColor(new Color(205, 201, 201));
      g.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

      engine.update((float)(delta / 1000000000.0));
      engine.render(g);
      
      appletGraphics.drawImage(screen, 0, 0, null);

      delta = System.nanoTime() - lastTime;
      //if (x == 25) engine.clear();
      if (delta < 20000000L) {
        try {
          Thread.sleep((20000000L - delta) / 1000000L);
        } catch (Exception e) {
          // It's an interrupted exception, and nobody cares
        }
      }

      if (!isActive()) {
        return;
      }
      x = (x < 26) ? x+1 : 0;
    }
  }

  public void mousePressed(MouseEvent e){
    int x = e.getX();
    int y = e.getY();
    engine.select(x, y);
  }
  public void mouseReleased(MouseEvent e){
    int x = e.getX();
    int y = e.getY();
    engine.slide(x, y);
  }
  public void mouseClicked(MouseEvent e) {} 
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
}
