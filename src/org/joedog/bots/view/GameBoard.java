package org.joedog.bots.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import org.joedog.bots.model.Arena;
import org.joedog.bots.model.Location;
import org.joedog.bots.actor.Actor;
import org.joedog.bots.actor.Block;
import org.joedog.bots.actor.Blank;
import org.joedog.bots.actor.Bully;
import org.joedog.bots.actor.Boney;
import org.joedog.bots.actor.Chewy;
import org.joedog.bots.actor.Enemy;
import org.joedog.bots.actor.Beddy;
import org.joedog.bots.actor.Leash;
import org.joedog.bots.actor.Treat;
import org.joedog.bots.view.dialog.*;
import org.joedog.util.Sleep;

public class GameBoard extends JDesktopPane {
  private Arena   arena;
  private Color   bColor;
  private Color   cColor;
  private Color   tColor;
  private Color   eColor;
  private int     cellsize = 32;    // XXX: HARDCODED!!!
  private int     width;
  private int     height;
  private boolean workmode = false;
  
  public GameBoard (Arena arena) {
    this.arena    = arena;
    this.workmode = true;
    this.bColor   = new Color(144, 128, 128);
    this.cColor   = new Color(128, 128, 128);
    this.eColor   = new Color(128, 128, 128);
    this.tColor   = new Color(128, 144, 128);
    this.width    = arena.getCols()*cellsize;
    this.height   = (arena.getRows()*cellsize)+50;
  }

  public String over() {
    InternalDialog dialog = new InternalDialog("GAME OVER");
    this.add(dialog);
    dialog.setVisible(true);
    String res = (String)dialog.display(this.getWidth(), this.getHeight());
    return res; 
  }

  @Override
  public void paintComponent(Graphics g) {
    BufferedImage screen = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = (Graphics2D) screen.getGraphics(); 

    g2.setColor(new Color(205, 201, 201));
    g2.fillRect(0, 0, this.width, this.height);

    int pad      = (this.cellsize/2);

    g2.setFont(new Font("Helvetica", Font.PLAIN, 18));

    g2.setColor(new Color(139, 137, 137));
    for (int i = 0; i <= arena.getCols(); i++) {
      g2.drawLine(i * cellsize, 0, i * cellsize, arena.getRows() * cellsize);
    }
    for (int i = 0; i <= arena.getRows(); i++) {
      g2.drawLine(0, i * cellsize, arena.getRows() * cellsize, i * cellsize);
    }
    
    // render the blocks 
    g2.setColor(this.bColor);
    for (Actor actor : arena.getActors(Block.class)) {
      int x = (int) (actor.getX() * cellsize) + 2;
      int y = (int) (actor.getY() * cellsize) + 2;
      if (workmode) {
        g2.drawString(Actor.BLOCK+"", x+pad, y+pad);
      } else {
        g2.fillRect(x, y, cellsize - 4, cellsize - 4);
      }
    }

    // render the blanks
    g2.setColor(Color.red);
    g2.setColor(this.bColor);
    for (Actor actor : arena.getActors(Blank.class)) {
      int x = (int) (actor.getX() * cellsize) + 2;
      int y = (int) (actor.getY() * cellsize) + 2;
      if (workmode) {
        g2.drawString(Actor.BLANK+"", x+pad, y+pad);
      } else {
        g2.fillRect(x, y, cellsize - 4, cellsize - 4);
      }
    }

    // render the boney
    g2.setColor(this.tColor);
    for (Actor actor : arena.getActors(Boney.class)) {
      int x = (int) (actor.getX() * cellsize) + 2;
      int y = (int) (actor.getY() * cellsize) + 2;
      if (workmode) {
        g2.drawString(Actor.BONEY+"", x+pad, y+pad);
      } else {
        g2.fillRect(x+4, y+12, cellsize-12, cellsize-24);
        g2.fillOval(x, y+7,  8, 8);
        g2.fillOval(x, y+15, 8, 8);
        g2.fillOval(x+cellsize-12, y+7,  8, 8);
        g2.fillOval(x+cellsize-12, y+15, 8, 8);
      }
    }
    
    // render the chewies 
    g2.setColor(this.cColor);
    for (Actor actor : arena.getActors(Chewy.class)) {
      int x      = ((int)actor.getX() * cellsize) + 2;
      int y      = ((int)actor.getY() * cellsize) + 2;
      int xpos[] = {x,   x+14,  x+28};
      int ypos[] = {y+28, y+2,  y+28};
      if (workmode) {
        g2.drawString(Actor.CHEWY+"", x+pad, y+pad);
      } else {
        g2.fillPolygon(xpos, ypos, 3);
      }
    }

    // render the enemies
    g2.setColor(this.eColor);
    for (Actor actor : arena.getActors(Enemy.class)) {
      int x = (int) (actor.getX() * cellsize);
      int y = (int) (actor.getY() * cellsize);
      if (workmode) {
        g2.drawString(Actor.ENEMY+"", x+pad, y+pad);
      } else {
        g2.fillOval(x + 2, y + 2, cellsize - 4, cellsize - 4);
      }
    }

    // render Beddy 
    g2.setColor(new Color(128,128,144));
    for (Actor actor : arena.getActors(Beddy.class)) {
      int x = (int) (actor.getX() * cellsize);
      int y = (int) (actor.getY() * cellsize);
      if (workmode) {
        g2.drawString(Actor.BEDDY+"", x+pad, y+pad);
      } else {
        g2.fillOval(x + 2, y + 10, cellsize - 4, cellsize - 14);
      }
    }
     
    // render Leash
    g2.setColor(new Color(128,128,144));
    g2.setStroke(new BasicStroke(4));
    for (Actor actor : arena.getActors(Leash.class)) {
      int x = (int) (actor.getX() * cellsize);
      int y = (int) (actor.getY() * cellsize);
      if (workmode) {
        g2.drawString(Actor.LEASH+"", x+pad, y+pad);
      } else {
        g2.drawOval(x + 4, y + 8, cellsize - 8, cellsize - 16);
      }
    }
    g2.setStroke(new BasicStroke(1));

    // render Treatie
    g2.setColor(new Color(205, 186,150)); 
    for (Actor actor : arena.getActors(Treat.class)) {
      int x = (int) (actor.getX() * cellsize);
      int y = (int) (actor.getY() * cellsize);
      if (workmode) {
        g2.drawString(Actor.TREAT+"", x+pad, y+pad);
      } else {
        g2.fillOval(x+8, y+6, cellsize-14, cellsize-18);
      }
    }
    
    
    // render player bully
    //g.setColor(new Color(0, 1f, 0));
    g2.setColor(new Color(0, 100, 0));
    Bully bully = arena.getBully();
    int x = (int) (bully.getX() * cellsize);
    int y = (int) (bully.getY() * cellsize);
    if (workmode) {
      g2.drawString(Actor.BULLY+"", x+pad, y+pad);
    } else {
      g2.fillOval(x + 2, y + 2, cellsize - 4, cellsize - 4);
    }
    if (bully.getThrust() == true) {
      if (bully.getThrustDirection() == Location.LEFT) {
        g2.fillRect(x, y+10, 16, 16);
      } else {
        g2.fillRect(x+cellsize-4, y+10, 6, 16);
      }
      bully.setThrust(false);
    }
    // render square on bully
    g2.setColor(new Color(0.7f, 0.5f, 0f));
    g2.fillRect(x + 10, y + 10, cellsize - 20, cellsize - 20);

    // location highlights
    g2.setColor(Color.red);
    for (Location locale : arena.getLocations()) {
      x = (int) (locale.getX() * cellsize);
      y = (int) (locale.getY() * cellsize);
      if (locale.isHighlighted()) {
        g2.drawRect(x, y, cellsize, cellsize);
        g2.drawRect(x+1, y+1, cellsize-2, cellsize-2);
      }
    }
    g2.setStroke(new BasicStroke(6));
    g2.drawLine((int)arena.p1().getX(), (int)arena.p1().getY(), (int)arena.p2().getX(), (int)arena.p2().getY());
    g2.setStroke(new BasicStroke(1));

    // score board
    g2.setColor(Color.GREEN);
    g2.clearRect(0, (arena.getRows() * cellsize), arena.getWidth(),  (arena.getRows() * cellsize) + 20);
    g2.setFont(new Font("Helvetica", Font.PLAIN, 12));
    g2.drawString("Turns: "+arena.getTurns(), 5, (arena.getRows() * cellsize) + 20);

    g.drawImage(screen, 0, 0, null);
  }
}
