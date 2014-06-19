package org.joedog.bots.actor;


public class Boney extends Actor {

  public Boney () {
    this.type     = BONEY;
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
    System.out.println("Boney collides");
    return 0;
  }

  public String toString() {
    return "Boney ("+type+")";
  }
}
