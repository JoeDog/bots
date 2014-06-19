package org.joedog.bots.model;

import java.util.ArrayList;

/**
 * By Jeffrey Fulmer <jeff@joedog.org>
 * <p>
 * Based on a tutorial by Aegidius Pluess, www.aplu.ch
 * Pluess published his tutorial as "open source" with
 * no accompanying license information. I assume it's 
 * in the public domain
 */
public class Grid  {
  private int cols;
  private int rows;
  private int cellsize;
  private int width;
  private int height;

  public Grid (int cols, int rows, int cellsize) {
    this.cols     = cols;
    this.rows     = rows;
    this.cellsize = cellsize;
    this.width    = this.cols * this.cellsize;
    this.height   = this.rows * this.cellsize;
  }

  public int getColumns() {
    return this.cols;
  }

  public int getRows() {
    return this.rows;
  }
}

