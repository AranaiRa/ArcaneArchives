package com.aranaira.arcanearchives.types.lists;

import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;

import java.util.List;
import java.util.Objects;

public class TileList extends ReferenceList<IteRef> implements ITileList {
  public TileList(List<IteRef> reference) {
    super(reference);
  }

  @Override
  public void cull() {
    removeIf(Objects::isNull);
    removeIf(IteRef::shouldCull);
  }

  @Override
  public void removeRef(IteRef ref) {
    this.remove(ref);
  }

  @Override
  public TileListIterable iterable() {
    return new TileListIterable(iterator());
  }

  @Override
  public int getSize() {
    return this.size();
  }
}
