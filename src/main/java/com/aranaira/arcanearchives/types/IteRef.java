package com.aranaira.arcanearchives.types;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class IteRef {
	public UUID uuid;
	public BlockPos pos;
	public int dimension;
	public WeakReference<ImmanenceTileEntity> tile;
	public Class<? extends ImmanenceTileEntity> clazz;

	public IteRef (ImmanenceTileEntity tile) {
		this.pos = tile.getPos();
		this.dimension = tile.dimension;
		this.clazz = tile.getClass();
		this.uuid = tile.uuid;
		this.tile = new WeakReference<>(tile);
	}

	@Nullable
	public ImmanenceTileEntity getTile () {
		if (tile == null) {
			return null;
		}

		return tile.get();
	}

	public void refreshTile (World world, int dimension) {
		if (world.isRemote) return;

		if (this.dimension != dimension) return;

		if (this.tile == null || this.tile.get() == null) {
			if (world.isBlockLoaded(this.pos)) {
				this.tile = new WeakReference<>(WorldUtil.getTileEntity(clazz, world, pos));
			}
		}
	}

	public void updateTile (ImmanenceTileEntity tile) {
		this.tile = new WeakReference<>(tile);
	}

	public boolean isValid () {
		if (tile == null) return false;
		if (tile.get() == null) return false;
		if (tile.get().isInvalid()) {
			return false;
		}
		if (!tile.get().getWorld().isBlockLoaded(pos)) {
			return false;
		}
		return true;
	}

	public boolean isActive () {
		if (tile == null) {
			return false;
		}

		ImmanenceTileEntity ite = tile.get();

		if (ite == null) {
			return false;
		}

		return ite.isActive();
	}

	/*public int networkPriority() {
		if(tile == null || tile.get() == null) return -999999999;

		//noinspection ConstantConditions
		return tile.get().networkPriority;
	}*/
}
