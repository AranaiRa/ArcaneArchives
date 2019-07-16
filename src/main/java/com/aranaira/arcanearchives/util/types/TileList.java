package com.aranaira.arcanearchives.util.types;

import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class TileList extends ReferenceList<IteRef> implements ITileList {
	public TileList (List<IteRef> reference) {
		super(reference);
	}

	@Override
	public void invalidate () {
		removeIf(ite -> ite.tile != null && ite.getTile() != null && ite.getTile().isInvalid());
	}

	@Override
	public void removeRef (IteRef ref) {
		this.remove(ref);
	}

	@Override
	public void refresh (World world) {
		if (world.isRemote) {
			return;
		}

		int dim = world.provider.getDimension();
		this.forEach((ref) -> ref.refreshTile(world, dim));
	}

	@Override
	public TileListIterable iterable () {
		return new TileListIterable(iterator());
	}

	public static class TileListIterable extends ReferenceListIterable<IteRef> implements Iterable<IteRef> {
		public TileListIterable (Iterator<IteRef> iter) {
			super(iter);
		}
	}


}
