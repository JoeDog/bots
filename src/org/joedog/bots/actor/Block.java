package org.joedog.bots.actor;

public class Block extends Actor {

  public Block () {
    this.type     = BLOCK;
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
    System.out.println("Block collide");
    return 0;
  }
  
  public String toString() {
    return "Block ("+type+")";
  }
}
