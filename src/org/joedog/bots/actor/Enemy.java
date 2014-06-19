package org.joedog.bots.actor;

import org.joedog.bots.model.Location;
import org.joedog.util.RandomUtils;

public class Enemy extends Actor {
  
  public Enemy() {
    this.type     = ENEMY;
    this.moveable = true;
  }

  /*
  @Override
  public void move() {
    int dir[] = {Location.WEST, Location.EAST};
    int ind   = RandomUtils.number(2);
    if (this.location.getY() >= this.arena.getRows()-1) {
      setLocation(this.location.getX(), 0);
    } else {
      int dist = 1; //RandomUtils.number(2);
      for (int i = 0; i < dist; i++) {
        Location tmpA = this.location.getAdjacentLocation(this.heading,  1, 0.3);    
        Location tmpB = this.location.getAdjacentLocation(dir[RandomUtils.number(2)], 1, 0.3);    
        Location tmpC = this.location.getAdjacentLocation(dir[RandomUtils.number(2)], 1, 0.3);    
        if (! this.arena.occupied(tmpA)) {
          setLocation(tmpA);
        } else if (! this.arena.occupied(tmpB)) {
          setLocation(tmpB);
          this.heading = Location.SOUTH;
        } else if (! this.arena.occupied(tmpC)) {
          setLocation(tmpC);
          this.heading = Location.SOUTH;
        } else {
          return; 
        } 
      }
    }
  }
  */

  public void act() {
    move();
  }

  public void init() {

  }

  public void hide() {

  }

  public void react() {
    System.out.println("OUCH!");
  }

  public int collide(Actor a1, Actor a2) {
    return 0;
  }

  public String toString() {
    return "Enemy ("+type+")";
  }
}

