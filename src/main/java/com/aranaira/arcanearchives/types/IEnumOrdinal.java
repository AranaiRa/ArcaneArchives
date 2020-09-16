package com.aranaira.arcanearchives.types;

import javax.annotation.Nullable;

public interface IEnumOrdinal<T extends IEnumOrdinal> {
  int index ();

  default T byOrdinal(T[] base, int ordinal, @Nullable T defaultEntry) {
    for (T entry : base) {
      if (entry.index() == ordinal) {
        return entry;
      }
    }

    return defaultEntry;
  }
}
