package com.aranaira.arcanearchives.events.ticks;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public abstract class TickHandler {
  private final Object writeLock = new Object();
  private final Object listLock = new Object();
  private boolean listTicking = false;

  private Set<Entry> queue = new LinkedHashSet<>();
  private Set<Entry> waitlist = new LinkedHashSet<>();

  public void handleTicks() {
    Set<Entry> queueCopy = new LinkedHashSet<>();
    Set<Entry> removed = new HashSet<>();
    synchronized (listLock) {
      listTicking = true;
      queueCopy.clear();
      queueCopy.addAll(queue);
      listTicking = false;
    }
    synchronized (writeLock) {
      for (Entry entry : queueCopy) {
        if (entry.delay-- <= 0) {
          entry.runnable.run();
          removed.add(entry);
        }
      }
    }
    synchronized (listLock) {
      listTicking = true;
      queue.removeAll(removed);
      queue.addAll(waitlist);
      listTicking = false;
      waitlist.clear();
    }
  }

  protected void addRunnableToQueue(Runnable runnable) {
    addRunnableToQueue(runnable, 0);
  }

  protected void addRunnableToQueue(Runnable runnable, int delay) {
    synchronized (listLock) {
      if (listTicking) {
        waitlist.add(new Entry(runnable, delay));
      } else {
        queue.add(new Entry(runnable, delay));
      }
    }
  }

  private class Entry {
    public Runnable runnable;
    public int delay;

    public Entry(Runnable runnable, int delay) {
      this.runnable = runnable;
      this.delay = delay;
    }
  }
}
