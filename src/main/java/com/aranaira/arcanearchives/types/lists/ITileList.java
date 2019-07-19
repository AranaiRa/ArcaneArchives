package com.aranaira.arcanearchives.types.lists;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ITileList extends Iterable<IteRef> {
	void invalidate ();

	default boolean containsUUID (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	default IteRef getReference (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return ref;
			}
		}

		return null;
	}

	@Nullable
	default ImmanenceTileEntity getByUUID (UUID uuid) {
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

	default void updateUUID (UUID oldId, UUID newId) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(oldId)) {
				ref.uuid = newId;
				break;
			}
		}
	}

	default void removeByUUID (UUID uuid) {
		IteRef toRemove = null;

		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				toRemove = ref;
				break;
			}
		}

		if (toRemove != null) {
			this.removeRef(toRemove);
		}
	}

	void removeRef (IteRef ref);

	void refresh (World world);

	default void refresh (ImmanenceTileEntity tile) {
		if (tile.getWorld().isRemote) {
			return;
		}

		IteRef ref = getReference(tile.getUuid());
		ref.updateTile(tile);
	}

	default TileListIterable iterable () {
		return new TileListIterable(iterator());
	}

	int getSize ();

}
