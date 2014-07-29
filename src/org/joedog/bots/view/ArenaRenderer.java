package org.joedog.bots.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

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

public class ArenaRenderer implements Renderer {
  private Arena   arena;
  private Color   bColor;
  private Color   cColor;
  private Color   tColor;
  private Color   eColor;
  private int     cellSize = 32; // XXX: HARDCODED!!!
  private boolean work     = false;
  
  public ArenaRenderer(Arena arena) {
    this.arena  = arena;
    this.work   = true;
    this.bColor = new Color(144, 128, 128);
    this.cColor = new Color(128, 128, 128);
    this.eColor = new Color(128, 128, 128);
    this.tColor = new Color(128, 144, 128);
  }
  
  @Override
  public void render(Graphics g) {
    Graphics2D g2 = (Graphics2D) g; 

    // render the grid
    int pad      = (cellSize/2);

    g.setFont(new Font("Helvetica", Font.PLAIN, 18));

    g.setColor(new Color(139, 137, 137));
    for (int i = 0; i <= arena.getCols(); i++) {
      g.drawLine(i * cellSize, 0, i * cellSize, arena.getRows() * cellSize);
    }
    for (int i = 0; i <= arena.getRows(); i++) {
      g.drawLine(0, i * cellSize, arena.getRows() * cellSize, i * cellSize);
    }
    
    // render the blocks 
    g.setColor(this.bColor);
    for (Actor actor : arena.getActors(Block.class)) {
      int x = (int) (actor.getX() * cellSize) + 2;
      int y = (int) (actor.getY() * cellSize) + 2;
      if (work) {
        g.drawString(Actor.BLOCK+"", x+pad, y+pad);
      } else {
        g.fillRect(x, y, cellSize - 4, cellSize - 4);
      }
    }

    // render the blanks
    g.setColor(Color.red);
    g.setColor(this.bColor);
    for (Actor actor : arena.getActors(Blank.class)) {
      int x = (int) (actor.getX() * cellSize) + 2;
      int y = (int) (actor.getY() * cellSize) + 2;
      if (work) {
        g.drawString(Actor.BLANK+"", x+pad, y+pad);
      } else {
        g.fillRect(x, y, cellSize - 4, cellSize - 4);
      }
    }

    // render the boney
    g.setColor(this.tColor);
    for (Actor actor : arena.getActors(Boney.class)) {
      int x = (int) (actor.getX() * cellSize) + 2;
      int y = (int) (actor.getY() * cellSize) + 2;
      if (work) {
        g.drawString(Actor.BONEY+"", x+pad, y+pad);
      } else {
        g.fillRect(x+4, y+12, cellSize-12, cellSize-24);
        g.fillOval(x, y+7,  8, 8);
        g.fillOval(x, y+15, 8, 8);
        g.fillOval(x+cellSize-12, y+7,  8, 8);
        g.fillOval(x+cellSize-12, y+15, 8, 8);
      }
    }
    
    // render the chewies 
    g.setColor(this.cColor);
    for (Actor actor : arena.getActors(Chewy.class)) {
      int x      = ((int)actor.getX() * cellSize) + 2;
      int y      = ((int)actor.getY() * cellSize) + 2;
      int xpos[] = {x,   x+14,  x+28};
      int ypos[] = {y+28, y+2,  y+28};
      if (work) {
        g.drawString(Actor.CHEWY+"", x+pad, y+pad);
      } else {
        g.fillPolygon(xpos, ypos, 3);
      }
    }

    // render the enemies
    g.setColor(this.eColor);
    for (Actor actor : arena.getActors(Enemy.class)) {
      int x = (int) (actor.getX() * cellSize);
      int y = (int) (actor.getY() * cellSize);
      if (work) {
        g.drawString(Actor.ENEMY+"", x+pad, y+pad);
      } else {
        g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
      }
    }

    // render Beddy 
    g.setColor(new Color(128,128,144));
    for (Actor actor : arena.getActors(Beddy.class)) {
      int x = (int) (actor.getX() * cellSize);
      int y = (int) (actor.getY() * cellSize);
      if (work) {
        g.drawString(Actor.BEDDY+"", x+pad, y+pad);
      } else {
        g.fillOval(x + 2, y + 10, cellSize - 4, cellSize - 14);
      }
    }
     
    // render Leash
    g2.setColor(new Color(128,128,144));
    g2.setStroke(new BasicStroke(4));
    for (Actor actor : arena.getActors(Leash.class)) {
      int x = (int) (actor.getX() * cellSize);
      int y = (int) (actor.getY() * cellSize);
      if (work) {
        g.drawString(Actor.LEASH+"", x+pad, y+pad);
      } else {
        g2.drawOval(x + 4, y + 8, cellSize - 8, cellSize - 16);
      }
    }
    g2.setStroke(new BasicStroke(1));

    // render Treatie
    g.setColor(new Color(205, 186,150)); 
    for (Actor actor : arena.getActors(Treat.class)) {
      int x = (int) (actor.getX() * cellSize);
      int y = (int) (actor.getY() * cellSize);
      if (work) {
        g.drawString(Actor.TREAT+"", x+pad, y+pad);
      } else {
        g.fillOval(x+8, y+6, cellSize-14, cellSize-18);
      }
    }
    
    
    // render player bully
    //g.setColor(new Color(0, 1f, 0));
    g.setColor(new Color(0, 100, 0));
    Bully bully = arena.getBully();
    int x = (int) (bully.getX() * cellSize);
    int y = (int) (bully.getY() * cellSize);
    if (work) {
      g.drawString(Actor.BULLY+"", x+pad, y+pad);
    } else {
      g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
    }
    if (bully.getThrust() == true) {
      if (bully.getThrustDirection() == Location.LEFT) {
        g.fillRect(x, y+10, 16, 16);
      } else {
        g.fillRect(x+cellSize-4, y+10, 6, 16);
      }
      bully.setThrust(false);
    }
    // render square on bully
    g.setColor(new Color(0.7f, 0.5f, 0f));
    g.fillRect(x + 10, y + 10, cellSize - 20, cellSize - 20);

    // location highlights
    g.setColor(Color.red);
    for (Location locale : arena.getLocations()) {
      x = (int) (locale.getX() * cellSize);
      y = (int) (locale.getY() * cellSize);
      if (locale.isHighlighted()) {
        g.drawRect(x, y, cellSize, cellSize);
        g.drawRect(x+1, y+1, cellSize-2, cellSize-2);
      }
    }
    g2.setStroke(new BasicStroke(6));
    g2.drawLine((int)arena.p1().getX(), (int)arena.p1().getY(), (int)arena.p2().getX(), (int)arena.p2().getY());
    g2.setStroke(new BasicStroke(1));

    // score board
    g.setColor(Color.GREEN);
    g.clearRect(0, (arena.getRows() * cellSize), arena.getWidth(),  (arena.getRows() * cellSize) + 20);
    g.setFont(new Font("Helvetica", Font.PLAIN, 12));
    g.drawString("Turns: "+arena.getTurns(), 5, (arena.getRows() * cellSize) + 20);

    if (arena.getTurns() == 0) {
      //g.setFont(new Font("Helvetica", Font.BOLD, 24));
      //g.drawString("GAME OVER", 5, (arena.getRows() / cellSize) + 20);
      gameOver(g);
    }
  }

  public void gameOver(Graphics g) {
    String over    = new String("GAME OVER");
    Graphics2D g2  = (Graphics2D) g;
    g2.setFont(new Font("Helvetica", Font.BOLD, 24));
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D r  = fm.getStringBounds(over, g2);
    int x = ((arena.getCols()*cellSize) - (int) r.getWidth()) / 2;
    int y = ((arena.getRows()*cellSize) - (int) r.getHeight()) / 2 + fm.getAscent();
    g2.setColor(new Color(194, 194, 194));
    g2.fill(new RoundRectangle2D.Double(x-20, y-(r.getHeight()+14), r.getWidth()+40, r.getHeight()+40, 10, 10));
    g2.setColor(new Color(32, 32, 32));
    g2.draw(new RoundRectangle2D.Double(x-20, y-(r.getHeight()+14), r.getWidth()+40, r.getHeight()+40, 10, 10));
    g.setColor(new Color(42, 67, 58));
    g2.drawString(over, x, y);
  }
}
