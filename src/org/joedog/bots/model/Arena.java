package org.joedog.bots.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.awt.Point;
import java.lang.Thread;

import org.joedog.bots.view.*;
import org.joedog.bots.actor.*;
import org.joedog.bots.model.Location;
import org.joedog.util.RandomUtils;
import org.joedog.util.Sleep;

public final class Arena implements ActorCollisionListener, SceneCollisionListener {
  public static final  int VERTICAL   = 0;
  public static final  int HORIZONTAL = 1;

  private Bully  bully              = null;
  private static Arena INSTANCE     = new Arena();
  private static final Object lock  = new Object();
  private ActorFactory factory      = new ActorFactoryImpl();
  private static List<Actor> scene; 
  private int    cols;
  private int    rows;
  private int    cellsize;
  private int    width;
  private int    height;
  private int    turns = 5;
  private Point  p1 = new Point(); // 0,0
  private Point  p2 = new Point(); // 0,0
  private ArenaRunner runner;
  private boolean     running = true;
  private boolean     ready   = false;
  private int         checks  = 0; // XXX: see evaluate. this can't remain here...

  private Arena() {
    if (INSTANCE != null) {
      throw new IllegalStateException("Already instantiated");
    }
    this.bully    = new Bully();
    this.width    = Config.WIDTH;
    this.height   = Config.HEIGHT;
    this.cellsize = 32;
    this.cols     = this.width  / this.cellsize;
    this.rows     = this.height / this.cellsize;
    if (this.scene == null) {
      this.scene  = Collections.synchronizedList(new ArrayList<Actor>());
    }
    this.createScene();
    synchronized (scene) {
      this.bully.addActorCollisionListener(this);
      this.bully.addSceneCollisionListener(this);
      this.scene.remove(this.getActor(new Location(5, 15)));
      this.bully.setStartingLocation(5, 15);
      this.addActor(this.bully, bully.getLocation());
      this.bully.setArena(this);
    }
    this.bully.addCollisionActors(this.getActors());
    this.runner = new ArenaRunner();
    this.runner.start();
  }

  public synchronized void reset() {
    this.removeAll();
    this.createScene();
    synchronized (scene) {
      this.scene.remove(this.getActor(new Location(5, 15)));
      this.bully.setStartingLocation(5, 15);
      this.addActor(this.bully, bully.getLocation());
      this.bully.setArena(this);
    }
    this.turns = 5;
  }

  public boolean isReady() {
    return this.ready;
  }

  public static Arena getInstance() {
    if (INSTANCE == null) {
      for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
        System.out.println(ste);
      }
    }
    return INSTANCE; 
  }

  private void act() {
    synchronized (this.scene) {
      for (Actor a : this.scene) {
        a.act();
      }
    }
  }

  public boolean takeTurn(int ox, int oy, int nx, int ny) {
    boolean success = swap(ox, oy, nx, ny);
    if (success) {
      this.setTurns(this.getTurns() - 1);
    }
    return success;
  }

  public void setTurns(int turns) {
    this.turns = turns;
  }

  public int getTurns() {
    return (this.turns < 1) ? 0 : this.turns; // don't want to send -1
  }

  private boolean swap(int ox, int oy, int nx, int ny) {
    Location orig = this.getLocation(ox, oy);
    Location dest = this.getLocation(nx, ny);
    Actor    star = this.getActor(orig);
    Actor    fill = this.getActor(dest);
    int     dir[] = {Location.EAST, Location.SOUTH, Location.WEST, Location.NORTH};

    for (int i = 0; i < dir.length; i++) {
      Location temp = dest.getAdjacentLocation(dir[i], 1, 0.3);    
      if (swappable(star.getType(), temp, dir[i])) {
        star.setLocation(dest);
        fill.setLocation(orig);
        return true;
      }
    } 
    /**
     * Check adjacent split matches
     *      x
     * x => 0
     *      x
     */
    Location east = dest.getAdjacentLocation(Location.EAST, 1, 0.3);
    Location west = dest.getAdjacentLocation(Location.WEST, 1, 0.3);
    if (this.getActor(east) != null && this.getActor(west) != null) {
      if (this.getActor(east).getType() == star.getType() && this.getActor(west).getType() == star.getType()) {
        star.setLocation(dest);
        fill.setLocation(orig);
        turns--;
        return true;
      }
    }
    Location north = dest.getAdjacentLocation(Location.NORTH, 1, 0.3);
    Location south = dest.getAdjacentLocation(Location.SOUTH, 1, 0.3);
    if (this.getActor(north) != null && this.getActor(south) != null) {
      if (this.getActor(north).getType() == star.getType() && this.getActor(south).getType() == star.getType()) {
        star.setLocation(dest);
        fill.setLocation(orig);
        turns--;
        return true;
      }
    }
    return false;
  }

  private boolean swappable(int type, Location next, int heading) {
    Actor actor = this.getActor(next);
    int x =  0;
    int t = -1;
    do {
      t = this.nextType(next, heading, x); 
      x++;
    } while (type == t);
    return (x > 2) ? true : false;
  }

  public void evaluate() {
    for (int x = 0; x < this.rows; x++) {
      for (int y = 0; y < this.cols; y++) {
        Actor actor = this.getActor(new Location(y, x));
        int res = 0;
        if (actor != null) {
          res = hsearch(actor.getType(), x, y);
        }
        if (res >= 3) {
          for (int i = y; i < (y+res); i++) {
            Actor a = this.getActor(new Location(i, x));
            if (a != null) {
              if (a.getType() == Actor.TREAT) {
                if (! this.ready) { 
                  this.removeActor(a);
                } else {
                  Actor b = factory.getActor(Actor.TRACK);
                  b.addActorCollisionListener(this);
                  this.replaceActor(b, new Location(i, x));
                }
              } else if (! a.isCrushable()) {
                ;
              } else {
                this.removeActor(a);
              }
            }
          }
        }
      }
    }
    for (int x = 0; x < this.rows; x++) {
      for (int y = 0; y < this.cols; y++) {
        Actor actor = this.getActor(new Location(y, x));
        int res = 0;
        if (actor != null) {
          res = vsearch(actor.getType(), x, y);
        }
        if (res >= 3) {
          for (int i = x; i < (x+res); i++) {
            Actor a = this.getActor(new Location(y, i));
            if (a != null) {
              if (a.getType() == Actor.TREAT) {
                if (! this.ready) { 
                  this.removeActor(a);
                } else {
                  Actor b = factory.getActor(Actor.TRACK);
                  b.addActorCollisionListener(this);
                  this.replaceActor(b, new Location(y, i));
                }
              } else if (! a.isCrushable()) {
                ;
              } else {
                this.removeActor(a);
              }
            }
          }
        }
      }
    }
    // XXX: need a real ready test....
    checks++;
    if (checks >= 100) this.ready = true;
  }

  public void clear() {
    for (Actor a : scene)
      a.getLocation().highlight(false);
    p1.move(0,0);
    p2.move(0,0);
  }

  private int hsearch(int type, int row, int col) {
    int count = 1;
    for (int y = col+1; y < this.cols; y++) {
      Actor actor = this.getActor(new Location(y, row));
      if (actor == null) {return count;}
      int   role  = actor.getType();
      if (type == role) {
        count ++;
      } else {
        return count;
      }
    }
    return count;
  }

  private int vsearch(int type, int row, int col) {
    int count = 1;
    for (int y = row+1; y < this.rows; y++) {
      Actor actor = this.getActor(new Location(col, y));
      if (actor == null) {return count;}
      int   role  = actor.getType();
      if (type == role) {
        count ++;
      } else {
        return count;
      }
    }
    return count;
  }

  private int nextType(Location start, int heading, int distance) {
    Actor player  = this.getActor(start);
    Location next = start.getAdjacentLocation(heading, distance, 0.3);
    Actor actor   = this.getActor(next);
    if (actor == null) {
      return -1; 
    } else {
      return actor.getType(); 
    } 
  }


  public Bully getBully() {
    return bully;
  }

  public ArrayList<Location> getLocations() {
    ArrayList <Location> list = new ArrayList<Location>();
    synchronized (scene) {
      for (Actor a : scene) 
        list.add(a.getLocation());
    }
    return list; 
  }

  public ArrayList<Actor> getActors() {
    ArrayList<Actor> list = new ArrayList<Actor>();
    synchronized (scene) {
      for (Actor a : scene)
        list.add(a);
    }
    return list;
  }

  public ArrayList<Actor> getActors(Class clazz) {
    ArrayList<Actor> list = new ArrayList<Actor>();
    synchronized (scene) {
      for (Actor a : scene)
        if (a.getClass() == clazz)
          list.add(a);
    }
    return list;
  }

  public List<Chewy> getChewies() {
    List<Chewy> chewies   = new ArrayList<Chewy>();
    for (Actor actor : scene) {
      if (actor.getType() == Actor.CHEWY) {
        chewies.add((Chewy)actor);
      }
    }
    return chewies;
  }

  public List<Enemy> getEnemies() {
    List<Enemy> enemies   = new ArrayList<Enemy>();
    for (Actor actor : scene) {
      if (actor.getType() == Actor.ENEMY) {
        enemies.add((Enemy)actor);
      }
    }
    return enemies;
  }

  public List<Block> getBlocks() {
    List<Block> blocks   = new ArrayList<Block>();
    for (Actor actor : scene) {
      if (actor.getType() == Actor.BLOCK) {
        blocks.add((Block)actor);
      }
    }
    return blocks;
  }

  public int getCols() {
    return this.cols;
  }

  public int getColumns() {
    return this.cols;
  }

  public int getRows() {
    return this.rows;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public Location getLocation(int x, int y) {
    int xpos = (int)(x / this.cellsize);
    int ypos = (int)(y / this.cellsize);
    return new Location(xpos, ypos);  
  }

  public synchronized boolean occupied(Location location) {
    if (! inGrid(location)) {
      return true;
    }

    synchronized (scene) {
      for (Actor actor : scene) {
        if (actor == null) {
          return false;
        }
        if (actor.getLocation().equals(location)) {
          return true;
        }
      }
    }
    return false;
  }

  public Actor getActor(Location location) {
    synchronized (scene) {
      for (Actor actor : scene) {
        if (! inGrid(location)) {
          return null;
        }
        if (actor.getLocation().equals(location)) {
          return actor;
        }
      }
    }
    return null;
  }

  /**
   * Returns a reference to an actor at coordinates x, y
   * <p>
   * @param  int   x coordinate
   * @param  int   y coordinate
   * @return actor.Actor
   */
  public Actor getActor(int x, int y) {
    Location location = this.getLocation(x, y);
    return this.getActor(location);
  }

  public int collide(Actor a1, Actor a2) {
    if (a1 instanceof Bully && a2 instanceof Chewy) {
      a2.hide();
      return 1;
    }
    if (a1 instanceof Bully && a2 instanceof Enemy) {
      //System.out.println("A Bully hits an Enemy coming through the rye");
    }
    return 0;
  }

  public int clobber(Actor a1, Actor a2) {
    this.scene.remove(a2);
    return 1;
  }

  public int collide(Actor actor, Location location) {
    //Monitor.wakeUp(); 
    return 0;
  }

  public void detectCollisions(Actor actor, ArrayList<Actor> partners) {
    ArrayList<Actor> tmp;
    synchronized (actor) {
      tmp = new ArrayList(partners);
    }
    for (Actor a : tmp) {
      if (inCollision(actor, a)) {
        actor.notifyCollider(a);
        this.collide(actor, a);
      }
    }
  }

  private boolean inCollision(Actor a1, Actor a2) {
    if (a1.equals(a2)) return false;
    if (a1.getLocation().equals(a2.getLocation())) {
      return true;
    }
    return false;
  }

  public boolean occupied(int x, int y) {
    return occupied(new Location(x, y));
  }

  public boolean inGrid(Location location) {
    boolean isHorz = (location.getX() >= 0 && location.getX() < this.getCols());
    boolean isVert = (location.getY() >= 0 && location.getY() < this.getRows());
    return isHorz && isVert;
  }

  public boolean atBorder(Location location) {
    boolean isHorz = (location.getX() < 0) || (location.getX() == this.getCols());
    boolean isVert = (location.getY() < 0) || (location.getY() == this.getRows());
    return (isHorz) || (isVert);
  }

  public boolean inGrid(int x, int y) {
    return inGrid(new Location(x, y));
  }

  public void addActor(Actor actor, Location location) {
    this.addActor(actor, location, Location.EAST);
  }

  public void addActor(Actor actor, Location location, int heading) {
    synchronized (scene) {
      this.scene.remove(actor); // just one copy, please
      actor.setArena(this);
      actor.setX(location.getX());
      actor.setY(location.getY());
      actor.setHeading(heading);
      this.scene.add(actor);
    } 
  }

  /** 
   * Keep for iterator reference 
  public void reclaim() {
    synchronized (shapes) {
      for (Iterator<Shape> iter = shapes.iterator(); iter.hasNext(); ) {
        Shape shape = iter.next();
        if (shape.reclaim() == true) {
          iter.remove();
        }
      }
    }
  }
  */

  public void removeAll() {
    for (int x = 0; x < this.rows; x++) {
      for (int y = 0; y < this.cols; y++) {
        Actor actor = this.getActor(new Location(y, x));
        removeActor(actor);
      } 
    }
  }

  public void removeActor(Actor actor) {
    synchronized (scene) {
      this.scene.remove(actor); 
    }
  }

  public void removeActor(Location location) {
    synchronized (scene) {
      Actor actor = this.getActor(location);
      this.scene.remove(actor); 
    }
  }

  public void replaceActor(Actor newActor, Location location) {
    synchronized (this) {
      Actor oldActor = this.getActor(location);
      int   index    = this.scene.indexOf(oldActor);
      newActor.setArena(this);
      newActor.setX(location.getX());
      newActor.setY(location.getY());
      this.scene.set(index, newActor);
    }
  }

  public void printActor(int x, int y) {
    Location area = this.getLocation(x, y);
    Actor a = this.getActor(area);
    if (a != null) {
      System.out.println(a.toString()+" at location "+area.toString());
    } else {
      System.out.println("Null actor at ["+x+","+y+"]");
    }
  }

  public Point p1() {
    return p1;
  }

 public Point p2() {
    return p2;
  }

  public String toString() {
    String str = "";

    for (int x = 0; x < this.rows; x++) {
      for (int y = 0; y < this.cols; y++) {
        Actor actor = this.getActor(new Location(y, x));
        String a = (actor==null) ? " nul " : actor.toString();
        str += "|"+a; //+"["+x+","+y+"]";
      } str += "|\n";
    }
    return str;
  }

  private void entrance() {
    // We add each
    for (int x = 0; x < this.cols; x++) {
      if (! occupied(x, 0)) {
        int num = RandomUtils.range(4, 7);
        Actor actor = factory.getActor(num);
        actor.addActorCollisionListener(this);
        this.addActor(actor, new Location(x, 0));
      }
    }
  }

  private void createScene() {
    for (int x = 0; x < this.cols; x++) {
      for (int y = 0; y < this.rows; y++) {
        if (! occupied(x, y)) {
          int num = RandomUtils.range(4, 7);
          Actor actor = factory.getActor(num);
          actor.addActorCollisionListener(this);
          this.addActor(actor, new Location(x, y));
        }  
      }
    }
  }

  /**
   * convenience method for debugging movement
   */
  private String headingAsString(int heading) {
    switch (heading) {
      case Location.EAST:       return "EAST";
      case Location.SOUTHEAST:  return "SOUTHEAST";
      case Location.SOUTH:      return "SOUTH";
      case Location.SOUTHWEST:  return "SOUTHWEST";
      case Location.WEST:       return "WEST";
      case Location.NORTHWEST:  return "NORTHWEST";
      case Location.NORTH:      return "NORTH";
      case Location.NORTHEAST:  return "NORTHEAST";
      default: return "WTF?";
    }
  }

  private String actorAsString(int type) {
    switch (type) {
      case Actor.BULLY: return "Bully (0)";
      case Actor.BLANK: return "Blank (1)";
      case Actor.BLOCK: return "Block (2)";
      case Actor.ENEMY: return "Enemy (3)";
      case Actor.CHEWY: return "Chewy (4)";
      case Actor.BONEY: return "Boney (5)";
      case Actor.TREAT: return "Treat (6)";
      case Actor.BEDDY: return "Beddy (7)";
      case Actor.LEASH: return "Leash (8)";
      case Actor.TRACK: return "Track (9)";
      default: return "WTF?";
    }
  }

  private class ArenaRunner extends Thread {
    public void run() {
      while (running) {
        entrance();
        evaluate();
        act();
        Sleep.milliseconds(30);
      }
    }
  }
}
