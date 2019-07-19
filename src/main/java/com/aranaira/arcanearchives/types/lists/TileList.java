package com.aranaira.arcanearchives.types.lists;

import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import net.minecraft.world.World;

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

	@Override
	public int getSize () {
		return this.size();
	}
}
