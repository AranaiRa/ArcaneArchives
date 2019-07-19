package com.aranaira.arcanearchives.util.types;

import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class CombinedTileList implements ITileList {
	private final List<ITileList> tileLists;

	public CombinedTileList (List<ITileList> references) {
		this.tileLists = references;
	}

	@Override
	public void invalidate () {
		tileLists.forEach(ITileList::invalidate);
	}

	@Override
	public void removeRef (IteRef ref) {
		this.tileLists.forEach(r -> r.removeRef(ref));
	}

	@Override
	public void refresh (World world) {
		if (world.isRemote) {
			return;
		}

		this.tileLists.forEach(t -> t.refresh(world));
	}

	@Override
	public TileListIterable iterable () {
		return new TileListIterable(iterator());
	}

	@Override
	public int getSize () {
		int size = 0;
		for (ITileList t : this.tileLists) {
			size += t.getSize();
		}
		return size;
	}

	@Override
	public Iterator<IteRef> iterator () {
		return new CombinedTileListIterator(this.tileLists.stream().map(ITileList::iterator).collect(Collectors.toList()));
	}

	public static class CombinedTileListIterator implements Iterator<IteRef> {
		private final Queue<Iterator<IteRef>> queue;
		private Iterator<IteRef> currentIterator;

		public CombinedTileListIterator (List<Iterator<IteRef>> iterators) {
			this.queue = new ArrayDeque<>(iterators);
			this.currentIterator = queue.remove();
		}

		@Override
		public boolean hasNext () {
			if (this.currentIterator.hasNext()) {
				return true;
			} else if (!queue.isEmpty()) {
				this.currentIterator = queue.remove();
				return this.currentIterator.hasNext();
			} else {
				return false;
			}
		}

		@Override
		public IteRef next () {
			return this.currentIterator.next();
		}
	}
}
