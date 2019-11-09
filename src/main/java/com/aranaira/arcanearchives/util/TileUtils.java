package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import com.aranaira.arcanearchives.types.lists.ITileList;
import com.google.common.collect.Iterators;

import java.util.function.Predicate;

public class TileUtils {
  public static TileListIterable filterValid(ITileList tiles) {
    tiles.cull();
    return new TileListIterable(Iterators.filter(tiles.iterator(), (f) -> f != null && !f.shouldCull()));
  }

  public static TileListIterable filterAssignableClass(ITileList tiles, Class<?> clazz) {
    tiles.cull();
    return new TileListIterable(Iterators.filter(tiles.iterator(), (f) -> f != null && !f.shouldCull() && clazz.isAssignableFrom(f.clazz)));
  }

  public static TileListIterable filter(ITileList tiles, Predicate<IteRef> predicate) {
    tiles.cull();
    return new TileListIterable(Iterators.filter(tiles.iterator(), predicate::test));
  }
}
