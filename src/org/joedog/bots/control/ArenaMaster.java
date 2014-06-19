package org.joedog.bots.control;

import org.joedog.bots.model.Arena;
import org.joedog.bots.model.Location;
import org.joedog.bots.actor.Bully;
import org.joedog.bots.actor.Enemy;

import org.joedog.util.RandomUtils;
import org.joedog.util.Sleep;

public class ArenaMaster {
  private static final int unit = 32;
  private Arena arena;
  
  private float targetX, targetY;
  private boolean moving = false;
  
  public ArenaMaster(Arena arena) {
    this.arena = arena;
  }
  
  public void update(float delta) {
    Bully bully = arena.getBully();
    arena.detectCollisions(bully, bully.getCollisionActors()); 
  }

  public boolean onClick(int heading) {
    Bully bully = arena.getBully();
    Location location = bully.getLocation().getAdjacentLocation(heading, 1, 0.3);
    bully.setHeading(bully.getLocation().getDirectionToward(location));
    bully.move();
    arena.detectCollisions(bully, bully.getCollisionActors()); 
    return false;
  }
 
  public boolean onClick(int x, int y) {
    targetX = x / unit;
    targetY = y / unit;
    Bully bully = arena.getBully();
    bully.setHeading(bully.getLocation().getDirectionToward(new Location((int)targetX, (int)targetY)));
    bully.move();
    return false;
  }
}
