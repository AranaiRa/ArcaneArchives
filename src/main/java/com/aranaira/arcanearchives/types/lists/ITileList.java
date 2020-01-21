package com.aranaira.arcanearchives.types.lists;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ITileList extends Iterable<IteRef> {
	default boolean containsUUID (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return true;
			}
		}

		return false;
	}

	default void updateReference (ImmanenceTileEntity tile) {
		IteRef ref = getReference(tile.getUuid());
		if (ref != null) {
			ref.pos = tile.getPos();
			ref.dimension = tile.dimension;
		}
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
	default IteRef getReference (BlockPos pos, int dimension) {
		for (IteRef ref : this) {
			if (ref.pos.equals(pos) && ref.dimension == dimension) {
				return ref;
			}
		}

		return null;
	}

	@Nullable
	default ImmanenceTileEntity getByUUID (UUID uuid) {
		for (IteRef ref : this) {
			if (ref.uuid.equals(uuid)) {
				return ref.getTile();
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

	default TileListIterable iterable () {
		return new TileListIterable(iterator());
	}

	int getSize ();

}
