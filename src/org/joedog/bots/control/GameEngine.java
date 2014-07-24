package org.joedog.bots.control;

import java.awt.Event;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;

import org.joedog.bots.model.Arena;
import org.joedog.bots.model.Location;
import org.joedog.bots.actor.Bully;
import org.joedog.bots.view.Renderer;
import org.joedog.bots.view.SimpleArenaRenderer;
import org.joedog.util.Sleep;

public class GameEngine {
  private Thread      thread;
  private Arena       arena;
  private Renderer    renderer;
  private ArenaMaster controller;
  private Graphics    g = null;
  private int x = 0;
  private int y = 0;
  private AtomicBoolean hiatus = new AtomicBoolean(false);


  public GameEngine() {
    this.arena      = Arena.getInstance();
    this.renderer   = new SimpleArenaRenderer(arena);
    this.controller = new ArenaMaster(arena);
  }

  public void clear() {
    this.arena.clear();
  }

  public void slide(int x, int y) {
    System.out.println("Old X: "+this.x+", Old Y: "+this.y+", New X: "+x+", New Y: "+y);
    this.arena.clear();
    if (this.arena.swap(this.x, this.y, x, y)) {
      System.out.println("WILL SWAP!");
    } else {
      System.out.println("NO swap for you!"); 
    }
  }

  public void select(int x, int y) {
    this.x = x;
    this.y = y;
    this.arena.clear();
    if (x < 32 && y < 32) {
      arena.printActor(x, y);
      System.out.println(arena.toString());
    } else {
      arena.printActor(x, y);
    }
  }

  public void update(float deltaTime) {
    controller.update(deltaTime);
  }

  public void setGraphics(Graphics g) {
    if (this.g == null) {
     this.g = g;
    }
  }

  public void render() {
    if (this.g != null) {
      renderer.render(this.g);
    }
  }

  public void render(Graphics g) {
    this.setGraphics(g);
    renderer.render(g);
  }

  public void addThread(Thread thread) {
    this.thread = thread;
  }

  public void pause(boolean b) {
    hiatus.set(b);
  }

  public boolean isPaused() {
    return hiatus.get();
  }
}
