package org.joedog.bots.actor;

import java.util.EventListener;

public abstract interface ActorCollisionListener extends EventListener {
  public abstract int collide(Actor a1, Actor a2);
  public abstract int clobber(Actor a1, Actor a2);
}


