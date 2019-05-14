package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class TileList extends ReferenceList<IteRef> {
	/*public TileList() {
		super(new ArrayList<>());
	}*/

	public TileList (List<IteRef> reference) {
		super(reference);
	}

	public TileListIterable filterValid () {
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isValid()));
	}

	/*public TileListIterable filterActive() {
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isActive()));
	}*/

	/*public TileListIterable filterClass(Class<? extends AATileEntity> clazz) {
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isValid() && f.clazz.equals(clazz)));
	}*/

	public TileListIterable filterAssignableClass (Class<? extends AATileEntity> clazz) {
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isValid() && clazz.isAssignableFrom(f.clazz)));
	}

	public TileListIterable filter (Predicate<IteRef> predicate) {
		return new TileListIterable(Iterators.filter(iterator(), predicate::test));
	}

	/*public TileList sorted(Comparator<IteRef> c) {
		TileList copy = new TileList(new ArrayList<>());
		copy.addAll(this);
		copy.sort(c);
		return copy;
	}*/

	public boolean containsUUID (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return true;
			}
		}

		return false;
	}

	public ImmanenceTileEntity getByUUID (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				if (ref.tile != null) {
					return ref.tile.get();
				}
				return null;
			}
		}

		return null;
	}

	public void removeByUUID (UUID uuid) {
		IteRef toRemove = null;

		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				toRemove = ref;
				break;
			}
		}

		if (toRemove != null) {
			this.remove(toRemove);
		}
	}

	@Override
	public TileListIterable iterable () {
		return new TileListIterable(iterator());
	}

	public class TileListIterable extends ReferenceListIterable<IteRef> {
		TileListIterable (Iterator<IteRef> iter) {
			super(iter);
		}
	}
}
