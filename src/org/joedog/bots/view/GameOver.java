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
  private static int   w = 0;
  private static int   h = 0;
  private static boolean clicked      = false;
  private static final String message = new String("GAME OVER");

  public GameOver(int x, int y) {
    super(x, y); // midpoint we'll adjust them below
  } 

  public void size(Graphics g) {
    if (this.getWidth() == 0 || this.getHeight() == 0) {
      Graphics2D g2  = (Graphics2D) g;
      g2.setFont(new Font("Helvetica", Font.BOLD, 24));
      FontMetrics fm = g2.getFontMetrics();
      Rectangle2D r  = fm.getStringBounds(message, g2);
      this.setX((this.getX() - (int) r.getWidth()) / 2);
      this.setY((this.getY() - (int) r.getHeight()) / 2 + fm.getAscent());
      this.setWidth((int)r.getWidth());
      this.setHeight((int)r.getHeight());
    }
  }

  @Override 
  public void click() {
    for (int i = 0; i < 5; i++) {
      this.move(RandomUtils.range(0, 5), RandomUtils.range(0, 5));
      System.out.println("MOVE!!");
      Sleep.sleep(1);
    }
    System.out.println("CLICKY CLICK!!");
    this.clicked = true; 
  }

  @Override
  public void draw(Graphics g) {
    if (this.clicked) {
      return;
    }
    this.size(g);
    Graphics2D g2  = (Graphics2D) g;
    g2.setFont(new Font("Helvetica", Font.BOLD, 24));
    System.out.println("GameOver object X: "+this.getX()+", Y: "+this.getY());
    g2.setColor(new Color(194, 194, 194));
    g2.fill(new RoundRectangle2D.Double(
      this.getX()-20, this.getY()-(this.getHeight()+14), this.getWidth()+40, this.getHeight()+40, 10, 10)
    );
    g2.setColor(new Color(32, 32, 32));
    g2.draw(new RoundRectangle2D.Double(
      this.getX()-20, this.getY()-(this.getHeight()+14), this.getWidth()+40, this.getHeight()+40, 10, 10)
    );
    g.setColor(new Color(42, 67, 58));
    g2.drawString(message, this.getX(), this.getY());
  }
}

