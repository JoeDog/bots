package org.joedog.bots.actor;

import org.joedog.bots.model.Location;

public class Bully extends Actor {
  private boolean thrust   = false;
  private int     pawdir   = Location.RIGHT;
  private float   speed    = 10f;
  private float   rotation = 0f;
  private int     bonies   = 0;

  public Bully() {
    super();
    this.type     = BULLY;
    this.moveable = true;
  }

  @Override
  public void setHeading(double heading) {
    this.heading = heading;
    if (this.heading == Location.EAST) {
      this.pawdir = Location.RIGHT;
    }
    if (this.heading == Location.WEST) {
      this.pawdir = Location.LEFT;
    } 
  }

  public void move(float x, float y) {
    move();
  }

  public void move(int x, int y) {
    move();
  }

  @Override
  public void move() {
    Location location = this.location.getNeighboringLocation(this.heading);
    if (this.arena.atBorder(location)) {
      return;
    }
    if (this.arena.occupied(location)) {
      Actor a = this.arena.getActor(location);
      if (a != null && a instanceof Block) {
        return;
      }
      if (a != null && a instanceof Enemy) {
        if (this.heading == Location.EAST || this.heading == Location.WEST) {
          a.react();
          this.actorCollisionListener.clobber(this, a);
        }
      }
    }
    setLocation(location);
  }

  public void react() {
    System.out.println("Arf!");
  }
  
  public void paw() {
    this.setThrust(true);
    Location target = this.location.getNeighboringLocation(this.heading);
    if (this.arena.atBorder(target)) {
      System.out.println("Punched the wall");
      return;
    } 
    if (this.arena.occupied(target)) {
      System.out.println("Pow!");
      Actor a = this.arena.getActor(target);
      if (a != null && a instanceof Enemy) {
        a.react();
      } 
    } else {
      System.out.println("Swing and a miss!");
    }
  }

  public void setThrust(boolean thrust) {
    this.thrust = thrust;
  }

  public boolean getThrust() {
    return this.thrust; 
  }

  public int getThrustDirection() {
    return this.pawdir;
  }

  public int getBonies() {
    return this.bonies;
  }

  public void act() {

  }

  public void init() {

  }

  public void hide() {

  }
  
  @Override
  public void notifyCollider(Actor partner) {
    if (this.actorCollisionListener != null) {
      this.bonies += this.actorCollisionListener.collide(this, partner);
    }
  }

  public int collide(Actor a1, Actor a2) {
    return 0;
  }
  
  public String toString() {
    return "Bully ("+type+")";
  }
}
