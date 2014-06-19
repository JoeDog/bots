package org.joedog.bots.actor;


public class Track extends Actor {

  public Track () {
    this.type     = TRACK;
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
    System.out.println("Track collides");
    return 0;
  }

  public String toString() {
    return "Track ("+type+")";
  }
}
