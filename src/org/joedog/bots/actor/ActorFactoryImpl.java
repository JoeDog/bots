package org.joedog.bots.actor;

public class ActorFactoryImpl implements ActorFactory {

  public ActorFactoryImpl() {
  }

  public Actor getActor(int type) {
    switch (type) {
      case Actor.BULLY:
        return new Bully();
      case Actor.BLANK:
        return new Blank();
      case Actor.BLOCK:
        return new Block();
      case Actor.ENEMY:
        return new Enemy();
      case Actor.CHEWY:
        return new Chewy(); 
      case Actor.BONEY:
        return new Boney();
      case Actor.TREAT:
        return new Treat();
      case Actor.BEDDY:
        return new Beddy();
      case Actor.LEASH:
        return new Leash();
      case Actor.TRACK:
        return new Track();
      default:
        return null;
    }
  }
}
