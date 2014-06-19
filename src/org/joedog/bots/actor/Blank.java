package org.joedog.bots.actor;

public class Blank extends Actor {

  public Blank () {
    this.type     = BLANK;
    this.moveable = false;
  }

  public void act() {
    move(); 
  }

  public void react() {}

  public void init() {

  }

  public void hide() {

  }

  public int collide(Actor a1, Actor a2) {
    System.out.println("Blank collide");
    return 0;
  }
  
  public String toString() {
    return "Blank ("+type+")";
  }
}
