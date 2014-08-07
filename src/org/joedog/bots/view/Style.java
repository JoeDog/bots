package org.joedog.bots.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public final class Style {
 
  public static void dialogWindow(Graphics g, int x, int y, int w, int h) {
    Graphics2D g2  = (Graphics2D) g;

    // make transparent so we can round it
    g2.setPaint(new Color(0.0f, 0.0f, 0.0f, 0.0f));
    g2.fillRect(x, y, w, h);

    // fill the dialog window with color
    GradientPaint fill = new GradientPaint(
      (float) (w * 0.25), (float) (h * 0.25), new Color(207,231,250),
      (float) (w * 0.75), (float) (h * 0.75), new Color(99,147,193)
    );
    g2.setPaint(fill);
    g2.fill(new RoundRectangle2D.Double(x, y, w, h, 10, 10));

    // add a border
    g2.setColor(new Color(32, 32, 32));
    g2.draw(new RoundRectangle2D.Double(x, y, w-1, h-1, 10, 10));
  } 

  public static void dialogMessage(Graphics g, String message, int w, int h) {
    Graphics2D g2  = (Graphics2D) g;

    g2.setColor(new Color(32, 32, 32));
    g2.setFont(new Font("Helvetica", Font.BOLD, 24));
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D r  = fm.getStringBounds(message, g2);
    int x = (w - ((int)r.getWidth()) / 2); 
    int y = (h - ((int)r.getHeight()) / 2) - (int)(h/10);
    System.out.println("X: "+x+" Y: "+y+" W: "+w+" H: "+h); 
    g2.drawString(message, x, y);
  }
}
