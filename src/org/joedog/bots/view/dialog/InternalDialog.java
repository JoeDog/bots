package org.joedog.bots.view.dialog;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuComponent;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.joedog.bots.view.Style;

public class InternalDialog extends JInternalFrame /*implements Dialog*/ {
  public final static String CLOSED = "CLOSED";
  public final static Object CANCEL = null;
  public final static Object OKAY   = "OKAY";

  protected static String message     = "";
  protected static final int width    = 235;
  protected static final int height   = 145;
  protected Object value              = OKAY;
  public    JButton okayButton   = new JButton("OKAY");
  public    JButton cancelButton = new JButton("Cancel");
  private   JPanel  buttonPanel  = new JPanel();


  public InternalDialog (String message) {
    super (
      "",
      true, //resizable
      true, //closable
      true, //maximizable
      true  //iconifiable
    );
    okayButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okayClicked();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelClicked();
      }
    });
    build(message, new JButton[] {okayButton, cancelButton});
    //buttonPanel.setLayout(new FlowLayout());
  }

  public InternalDialog(String message, JButton[] buttons) {
    super (
      "",
      true, //resizable
      true, //closable
      true, //maximizable
      true  //iconifiable
    );
    build(message, buttons);
  }

  private void build(String message, JButton[] buttons) {
    this.message = message;
    System.out.println("BUILDING: "+message);
    this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
    this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    this.setLayout(new BorderLayout());
    buttonPanel.setLayout(new FlowLayout());
    this.add(buttonPanel, BorderLayout.SOUTH);
    for (int i = 0; i < buttons.length; i++) {
      buttonPanel.add(buttons[i]);
    }
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  public Object display() {
    Container parent = null;
    do {
      parent = this.getParent();
    } while (parent == null);
    return this.display(new Dimension(parent.getWidth(), parent.getHeight()));
  }

  public Object display(int x, int y) {
    return this.display(new Dimension(x, y));
  }

  public Object display(Dimension dim) {
    this.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent pce) {
        String name = pce.getPropertyName();
        if (name.equals(CLOSED)) {
          stopModal();
        }
      }
    });
    this.setSize(this.width, this.height);
    this.setLocation((dim.width - this.width)/2, ((dim.height - this.height)/2)-(int)(this.height/3));
    this.setVisible(true);
    System.out.println("GOING MODAL!");
    //startModal();
    return this.value;
  }

  public void okayClicked() {
    setValue(OKAY);
    close();
  }

  public void cancelClicked() {
    setValue(CANCEL);
    close();
  }

  public void close() {
    firePropertyChange(CLOSED, null, Boolean.TRUE);
    try {
      this.setVisible(false);
      this.setClosed(true);
    } catch (PropertyVetoException pve) {}
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

  /**
   * see: http://www.javakey.net/4-java-gui/686df4a3d194cade.htm
   */
  private void startModal() {
    synchronized (this) {
      if (this.isVisible() && !this.isShowing()) {
        Container parent = this.getParent();
        while (parent != null) {
          if (parent.isVisible() == false) {
            parent.setVisible(true);
          }
          parent = parent.getParent();
        }
      }
      try {
        if (SwingUtilities.isEventDispatchThread()) {
          EventQueue theQueue = getToolkit().getSystemEventQueue();
          System.out.println("IS EVENT DISPATCH");
          while (isVisible()) {
            AWTEvent event = theQueue.getNextEvent();
            Object src = event.getSource();
            if (src instanceof Component) {
              ((Component)src).dispatchEvent(event);
            } else if (src instanceof MenuComponent) {
              ((MenuComponent)src).dispatchEvent(event);
            } else {
              System.err.println("ERROR: unable to dispatch: "+event);
            }
            System.out.println("the way ay ting is the hardest part..."); 
            //wait();
          }
        } else {
          while (isVisible()) {
            System.out.println("waiting on a friend....");
            wait();
          }
        }
      } catch(InterruptedException e) {e.printStackTrace();}
    }
  }

  private void stopModal() {
    this.dispose();
    synchronized(this) {
      notifyAll();
    }
  }
} 
