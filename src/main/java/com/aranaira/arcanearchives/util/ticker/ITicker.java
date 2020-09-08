package com.aranaira.arcanearchives.util.ticker;

public interface ITicker<V, T> {
  boolean expired ();
  boolean decaying ();
  boolean invalid (V type);
  boolean run (V type);
  boolean decay (V type);
  T getInternal ();
}
