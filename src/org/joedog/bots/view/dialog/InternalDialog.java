package org.joedog.bots.view.dialog;

import java.awt.ActiveEvent;
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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.joedog.bots.view.Style;
import org.joedog.util.Sleep;

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
  private   DPanel  dialogPanel; // = new DPanel();
  private   JPanel  buttonPanel; 

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
    this.setLayer(JLayeredPane.MODAL_LAYER);
    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.setOpaque(false);
    this.message = message;
    this.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
    this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    this.dialogPanel = new DPanel();
    this.getContentPane().add(dialogPanel, BorderLayout.CENTER);
    this.addButtons(buttons);
  }

  public void addButtons(JButton buttons[]) {
    if (buttonPanel != null)
      this.dialogPanel.remove(buttonPanel);
    buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)) {
      @Override
      public void paintComponent(Graphics g) { 
        if (g instanceof Graphics2D) {
          Graphics2D g2 = (Graphics2D) g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
          super.paint(g);
        }
      }
    };
    this.dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
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
    System.out.println("Add buttons");
    this.setVisible(true);
    this.validate();
    startModal();
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
  public void paintComponent(Graphics g) {
    if (g instanceof Graphics2D) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    } else {
      super.paint(g);
    }
  } 

  /**
   * see: http://www.javakey.net/4-java-gui/686df4a3d194cade.htm
   */
  private synchronized void startModal() {
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
        while (this.isVisible()) {
          AWTEvent event  = theQueue.getNextEvent();
          Object   source = event.getSource();
          if (event instanceof ActiveEvent) {
            System.out.println("ActiveEvent!");
            ((ActiveEvent) event).dispatch();
          } else if (source instanceof Component) {
            System.out.println("COMPONENT!!!!");
            ((Component) source).dispatchEvent(event);
          } else if (source instanceof MenuComponent) {
            ((MenuComponent) source).dispatchEvent(event);
          } else {
            System.err.println("Unable to dispatch: " + event);
          }
        }
      } else {
        while (this.isVisible()) {
          wait();
        }
      }
    } catch(InterruptedException e) {e.printStackTrace();}
  }

  private void stopModal() {
    this.dispose();
    synchronized(this) {
      notifyAll();
    }
  }

  private class DPanel extends JPanel {
    DPanel() {
      this.setLayout(new BorderLayout());
      this.setOpaque(false);
    }
    
    public void paintComponent(Graphics g) {
      if (g instanceof Graphics2D) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Style.dialogWindow(g2, 0, 0, this.getWidth(), this.getHeight());
        Style.dialogMessage(g2, "GAME OVER", this.getWidth(), this.getHeight());
      } else {
        super.paintComponent(g);
      }
    } 
  }
} 
