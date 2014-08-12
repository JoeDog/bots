package org.joedog.bots.view.dialog;

import javax.swing.JButton;

public interface Dialog {
  public final static String CLOSED = "CLOSED";
  public final static Object CANCEL = null;
  public final static Object OKAY   = "OKAY";

  public Object display(int i, int j);
  public Object getValue();
  public void   setValue(Object value);
  //public void   setButtons(JButton buttons[]);
}

