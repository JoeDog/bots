package org.joedog.util;

public final class Sleep {
  
  public static void sleep(int i) {
    nap(i);
  }

  public static void nap(int i) {
    try {
      Thread.sleep(i);
    } catch (Exception e) {}
  }
}
