package org.joedog.bots.actor;


public class Chewy extends Actor {

  public Chewy () {
    this.type     = CHEWY;
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
    System.out.println("Chewy collides");
    return 0;
  }

  public String toString() {
    return "Chewy ("+type+")";
  }
}
