package org.joedog.bots.actor;


public class Beddy extends Actor {

  public Beddy () {
    this.type     = BEDDY;
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
    System.out.println("Beddy collides");
    return 0;
  }

  public String toString() {
    return "Beddy ("+type+")";
  }
}
