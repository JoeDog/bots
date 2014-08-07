package org.joedog.bots.view.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;

import org.joedog.bots.view.Style;

public class Retry extends JInternalFrame {
  private static final int width    = 235;
  private static final int height   = 145;
  private static Retry  _instance    = null;
  private static Object mutex       = new Object();

  private Retry () {
    super (
      "Game Over",
      true, //resizable
      true, //closable
      true, //maximizable
      true  //iconifiable
    );
    this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
    this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    System.out.println("CONSTRUCT");
  }

  public void display(int x, int y) {
    this.display(new Dimension(x, y));
  }

  public void display(Dimension dim) {
    this.setSize(this.width, this.height);
    this.setLocation((dim.width - this.width)/2, ((dim.height - this.height)/2)-(int)(this.height/3));
    this.setVisible(true);
  }

  public synchronized static Retry getInstance() {
    if (_instance == null) {
      synchronized(mutex) {
        if (_instance == null) {
          _instance = new Retry();
        }
      }
    }
    return _instance;
  }

  @Override
  public void paint(Graphics g) {
    if (g instanceof Graphics2D) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      Style.dialogWindow(g2, 0, 0, this.width, this.height);
      Style.dialogMessage(g2, "GAME OVER", this.width, this.height);
    } else {
      super.paint(g);
    }
  }

} 
