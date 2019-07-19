package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.util.types.ITileList;
import com.aranaira.arcanearchives.util.types.IteRef;
import com.aranaira.arcanearchives.util.types.TileListIterable;
import com.google.common.collect.Iterators;

import java.util.function.Predicate;

public class TileUtils {
	public static TileListIterable filterValid (ITileList tiles) {
		tiles.invalidate();
		return new TileListIterable(Iterators.filter(tiles.iterator(), (f) -> f != null && f.isValid()));
	}

	public static TileListIterable filterAssignableClass (ITileList tiles, Class<?> clazz) {
		tiles.invalidate();
		return new TileListIterable(Iterators.filter(tiles.iterator(), (f) -> f != null && f.isValid() && clazz.isAssignableFrom(f.clazz)));
	}

	public static TileListIterable filter (ITileList tiles, Predicate<IteRef> predicate) {
		tiles.invalidate();
		return new TileListIterable(Iterators.filter(tiles.iterator(), predicate::test));
	}
}
