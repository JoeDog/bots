package org.joedog.bots.actor;

import java.util.ArrayList;

import org.joedog.bots.model.Arena;
import org.joedog.bots.model.Location;

public abstract class Actor implements Cloneable, ActorCollisionListener, SceneCollisionListener {
  public static final  int BULLY = 0;
  public static final  int BLANK = 1;
  public static final  int BLOCK = 2;
  public static final  int ENEMY = 3;
  public static final  int CHEWY = 4;
  public static final  int BONEY = 5;
  public static final  int TREAT = 6;
  public static final  int BEDDY = 7;
  public static final  int LEASH = 8;
  public static final  int TRACK = 9;

  protected int      type;
  protected int      action;
  protected Arena    arena;
  protected double   heading; 
  protected boolean  moveable   = false;
  protected Location location   = new Location(0, 0);
  protected Location wherefrom  = new Location(0, 0);
  protected Location birthplace = new Location(0, 0);
  protected ArrayList<Actor>    collisionActors = new ArrayList();
  protected ArrayList<Location> collisionScenes = new ArrayList();
  protected ActorCollisionListener actorCollisionListener  = null;
  protected SceneCollisionListener sceneCollisionListener  = null;

  public Actor() {
    this.setup();
  }

  public abstract void act();

  public abstract void hide();

  public abstract void react();

  public void setup() {
    this.actorCollisionListener = this;
    this.sceneCollisionListener = this;     
  }

  public void move() {
    Location location = this.location.getNeighboringLocation(Location.SOUTH);
    if (this.arena.atBorder(location)) {
      return;
    }
    if (this.arena.occupied(location)) {
      return;
    }
    System.out.println("moving....");
    setLocation(location);
  }

  public void setArena(Arena arena) {
    this.arena = arena;
  }

  public void setHeading(double heading) {
    this.heading = heading;
  }

  public double getHeading() {
    return this.heading;
  }

  public void setStartingLocation(Location location) {
    this.wherefrom  = location.clone();
    this.birthplace = location.clone();
    this.location   = location.clone();
  }

  public void setStartingLocation(int x, int y) {
    this.wherefrom.setX(x);
    this.wherefrom.setY(y);
    this.birthplace.setX(x);
    this.birthplace.setY(y);
    this.location.setX(x);
    this.location.setY(y);
  }

  public void setAction(int action) {
    this.action = action;
  }

  public void setLocation(Location location) {
    this.wherefrom = this.location.clone();
    this.location.setX(location.getX());
    this.location.setY(location.getY());
  }

  public void setLocation(int x, int y) {
    this.wherefrom = this.location.clone();
    this.location.setX(x);
    this.location.setY(y);
  }

  public int getAction() {
    return this.action;
  }

  public int getType() {
    return this.type;
  }

  public Location getLocation() {
    return this.location;
  }

  public int getX() {
    return this.location.getX();
  }

  public int getY() {
    return this.location.getY();
  }

  public void setX(int x) {
    this.location.setX(x);
  }

  public void setY(int y) {
    this.location.setY(y);
  }

  public int collide(Actor a1, Actor a2) {
    System.out.println("ACTOR collide");
    return 0;
  }

  public int clobber(Actor a1, Actor a2) {
    System.out.println("ACTOR clobber");
    return 0;
  }

  public int collide(Actor actor, Location location) {
    return 0;
  }

  public synchronized void addCollisionActor(Actor partner) {
    this.collisionActors.add(partner);
  }

  public synchronized void addCollisionActors(ArrayList<Actor> partners) {
    for (Actor a : partners) {
      this.collisionActors.add(a);
    }
  }

  public synchronized ArrayList<Actor> getCollisionActors() {
    return this.collisionActors;
  }

  public synchronized void addCollisionScene(Location location) {
    this.collisionScenes.add(location);
  }

  public synchronized ArrayList<Location> getCollisionScenes() {
    return this.collisionScenes;  
  }

  public void notifyCollider(Actor partner) {
    System.out.print("I was in an accident!");
    if (this.actorCollisionListener != null) {
      this.actorCollisionListener.collide(this, partner);
    }
  }

  protected void notifyCollisionScene(Location location) {
    if (this.sceneCollisionListener != null) {
      //this.tileSimCount = this.tileCollisionListener.collide(this, location);
    }
  }
  
  public synchronized void addActorCollisionListener(ActorCollisionListener listener) {
    this.actorCollisionListener = listener;
  }
  
  public synchronized void addSceneCollisionListener(SceneCollisionListener listener) {
    this.sceneCollisionListener = listener;
  }

  public Actor clone() {
    Actor copy = null;
    
    try {  
      copy = (Actor)super.clone();
    } catch (CloneNotSupportedException e) {}
    return copy;
  }
}
