package org.joedog.bots.actor;


public class Leash extends Actor {

  public Leash () {
    this.type     = LEASH;
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
    System.out.println("Leash collides");
    return 0;
  }

  public String toString() {
    return "Leash ("+type+")";
  }
}
