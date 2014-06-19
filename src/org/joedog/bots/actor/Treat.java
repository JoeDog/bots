package org.joedog.bots.actor;


public class Treat extends Actor {

  public Treat () {
    this.type     = TREAT;
    this.moveable = false;
  }

  public void act() {
    move();
  }

  public void init() {

  }

  public void react() {}

  public void hide() {
    this.setX(100);
    this.setY(100);
  }

  public int collide(Actor a1, Actor a2) {
    System.out.println("Treat collides");
    return 0;
  }

  public String toString() {
    return "Treat ("+type+")";
  }
}
