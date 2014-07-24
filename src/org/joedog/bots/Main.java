package org.joedog.bots;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;

import java.awt.Rectangle;

import java.awt.image.BufferedImage;

import org.joedog.bots.control.Game;
import org.joedog.bots.control.GameEngine;
import org.joedog.bots.control.GameThread;
import org.joedog.bots.model.Config;
import org.joedog.bots.model.Location;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class Main extends JPanel implements MouseListener {
  private static final long serialVersionUID = -2666347118493388423L;
  private static GameEngine engine = null;
  private static Canvas     canvas = null;

  public Main(Canvas canvas) {   
    super(new BorderLayout());
    canvas.addMouseListener(this);
    this.addMouseListener(this);
  }

  private static void createAndShowGui(Canvas canvas) {
    final JFrame frame = new JFrame("Bully And The Bots");
    JComponent   panel = new Main(canvas);
    panel.add(canvas, BorderLayout.CENTER);
    canvas.setFocusable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(panel); 
    frame.setPreferredSize(new Dimension(Config.WIDTH+16, Config.HEIGHT+94));
    frame.setSize(Config.WIDTH+16, Config.HEIGHT+94);
    frame.pack();
    frame.setLocation(10, 10);
    frame.setVisible(true);
    Rectangle r = frame.getBounds();
    System.out.println("W: "+r.width+" H: "+r.height);
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

  private static void adjustLAF() throws ClassNotFoundException,
    InstantiationException, IllegalAccessException,
    UnsupportedLookAndFeelException {
    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
      if ("Nimbus".equals(info.getName())) {
        //UIManager.setLookAndFeel(info.getClassName());
        break;
      }
    }
  }

  public static void main(String[] args) {
    if (engine == null) {
      engine = new GameEngine();
    }

    if (canvas == null) {
      canvas = new Canvas();
    }

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      adjustLAF();
    }
    catch (javax.swing.UnsupportedLookAndFeelException e) {
      System.err.println("Bully requires java-1.6 or higher");
    }
    catch (ClassNotFoundException e) {
      System.err.println("Bully requires java-1.6 or higher");
    }
    catch (InstantiationException e) {
    }
    catch (IllegalAccessException e) {
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGui(canvas);
      }
    });

    while (true) {
      Game game = new Game(engine, canvas);
      GameThread thread = new GameThread(game);
      engine.addThread(thread);
      thread.start();
      while (thread.isAlive()) ;
    }
  }
}
