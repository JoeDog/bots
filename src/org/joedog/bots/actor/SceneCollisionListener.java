package org.joedog.bots.actor;

import java.util.EventListener;

import org.joedog.bots.model.Location;

public abstract interface SceneCollisionListener extends EventListener {
  public abstract int collide(Actor actor, Location location);
}

