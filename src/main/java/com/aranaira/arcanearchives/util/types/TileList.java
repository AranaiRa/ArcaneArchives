package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.google.common.collect.Iterators;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class TileList extends ReferenceList<IteRef> {
	public TileList (List<IteRef> reference) {
		super(reference);
	}

	public TileListIterable filterValid () {
		invalidate();
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isValid()));
	}

	public TileListIterable filterAssignableClass (Class<?> clazz) {
		invalidate();
		return new TileListIterable(Iterators.filter(iterator(), (f) -> f != null && f.isValid() && clazz.isAssignableFrom(f.clazz)));
	}

	public TileListIterable filter (Predicate<IteRef> predicate) {
		invalidate();
		return new TileListIterable(Iterators.filter(iterator(), predicate::test));
	}

	private void invalidate () {
		removeIf(ite -> ite.tile != null && ite.getTile() != null && ite.getTile().isInvalid());
	}

	public boolean containsUUID (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	public IteRef getReference (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return ref;
			}
		}

		return null;
	}

	@Nullable
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

	public void updateUUID (UUID oldId, UUID newId) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(oldId)) {
				ref.uuid = newId;
				break;
			}
		}
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

	public void refresh (World world) {
		if (world.isRemote) return;

		int dim = world.provider.getDimension();
		this.forEach((ref) -> ref.refreshTile(world, dim));
	}

	public void refresh (ImmanenceTileEntity tile) {
		if (tile.getWorld().isRemote) return;

		int dim = tile.getWorld().provider.getDimension();
		IteRef ref = getReference(tile.getUuid());
		ref.updateTile(tile);
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
