package org.joedog.bots.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import org.joedog.util.Sleep;
import org.joedog.util.RandomUtils;

public class GameOver extends Shape {
  private static int w = 0;
  private static int h = 0;
  private static boolean clicked        = false;
  private static int     moves          = 66;
  private static int     wobble         = (moves - 6);
  private static final   String message = new String("GAME OVER");

  public GameOver(int x, int y) {
    super(x, y); // midpoint we'll adjust them below
  } 

  public void size(Graphics g) {
    if (this.getWidth() == 0 || this.getHeight() == 0) {
      Graphics2D g2  = (Graphics2D) g;
      g2.setFont(new Font("Helvetica", Font.BOLD, 24));
      FontMetrics fm = g2.getFontMetrics();
      Rectangle2D r  = fm.getStringBounds(message, g2);
      this.setX(((this.getX() - (int) r.getWidth()) / 2)-20);
      this.setY(((this.getY() - (int) r.getHeight()) / 2 + fm.getAscent())-((int)r.getHeight()+14));
      this.setWidth((int)r.getWidth()+40);
      this.setHeight((int)r.getHeight()+40);
    }
  }

  @Override 
  public void click() {
    this.clicked = true; 
  }

  @Override
  public boolean reclaim() {
    return (this.moves < 0);
  }

  @Override
  public void draw(Graphics g) {
    if (this.clicked && this.moves >= this.wobble) {
      this.moveTo(RandomUtils.range(-8, 8), RandomUtils.range(-1, 1));
      this.moves--; 
    }
    if (this.clicked && (this.moves >= 0 && this.moves < this.wobble)) {
      this.moveTo(0, -8);
      this.moves--; 
    }
    if (this.moves < 0) {
      return;
    }
    this.size(g);
    Graphics2D g2  = (Graphics2D) g;
    g2.setFont(new Font("Helvetica", Font.BOLD, 24));
    g2.setColor(new Color(194, 194, 194));
    g2.fill(new RoundRectangle2D.Double(
      this.getX(), this.getY(), this.getWidth(), this.getHeight(), 10, 10)
    );
    g2.setColor(new Color(32, 32, 32));
    g2.draw(new RoundRectangle2D.Double(
      this.getX(), this.getY(), this.getWidth(), this.getHeight(), 10, 10)
    );
    g.setColor(new Color(42, 67, 58));
    g2.drawString(message, this.getX()+20, this.getY()+42);
    Sleep.milliseconds(30);
  }
}

