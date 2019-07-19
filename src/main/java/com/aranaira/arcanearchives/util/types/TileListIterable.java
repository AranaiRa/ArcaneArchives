package com.aranaira.arcanearchives.util.types;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class TileListIterable implements Iterable<IteRef> {
	protected Iterator<IteRef> iter;

	public TileListIterable () {
	}

	public TileListIterable (Iterator<IteRef> iter) {
		this.iter = iter;
	}

	@Override
	@Nonnull
	public Iterator<IteRef> iterator () {
		return this.iter;
	}

	public void setIterator(Iterator<IteRef> iterator) {
		this.iter = iterator;
	}
}
