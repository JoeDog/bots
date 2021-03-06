package org.joedog.util;

public final class Debug {
   
  public final static void print(String s) {
    if (Debug.debug() == true) {
      System.out.println(s);
    }
  }

  public static boolean debug() {
    String debug = (String)System.getProperty("pinochle.debug");
    if (debug != null && debug.equals("true")) {
      return true;
    }
    return false;
  }
}
