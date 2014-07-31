package org.joedog.bots.view;

import java.awt.Point;
import java.awt.Graphics;

public abstract class Shape {
  private int x;
  private int y;
  private int w;
  private int h;

  public Shape() {
    move(0, 0);
  }

  public Shape(int x, int y) {
    move(x, y);
  }

  public int getX() { 
    return this.x; 
  }

  public int getY() { 
    return this.y; 
  }

  public int getWidth() {
    return this.w;
  }

  public int getHeight() {
    return this.h;
  }

  public void setX(int x) { 
    this.x = x; 
  }

  public void setY(int y) { 
    this.y = y; 
  }

  public void setWidth(int w) {
    this.w = w;
  }

  public void setHeight(int h) {
    this.h = h;
  }

  void move(int x, int y) {
    setX(x);
    setY(y);
  }
  
  void moveTo(int dx, int dy) {
    move(getX() + dx, getY() + dy);
  }

  public boolean contains(Point p) {
    return contains((int)p.getX(), (int)p.getY());
  }

  public boolean contains(int x, int y) {
    if (x >= this.getX() && x < this.getX()+this.getWidth()  && 
        y >= this.getY() && y < this.getY()+this.getHeight()) {
      return true;
    } else {
      return false;  
    }
  }

  public abstract void    draw(Graphics g);
  public abstract void    click();
  public abstract boolean reclaim();
}
