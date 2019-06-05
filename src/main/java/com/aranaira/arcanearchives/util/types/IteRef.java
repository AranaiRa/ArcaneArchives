package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.ManifestTileEntity;
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

	public IteRef (BlockPos pos, int dimension, UUID tileID, Class<? extends ImmanenceTileEntity> clazz) {
		this.pos = pos;
		this.dimension = dimension;
		this.clazz = clazz;
		this.uuid = tileID;
	}

	public IteRef (ImmanenceTileEntity tile) {
		this.pos = tile.getPos();
		this.dimension = tile.dimension;
		this.clazz = tile.getClass();
		this.uuid = tile.uuid;
		this.tile = new WeakReference<>(tile);
	}

	public ImmanenceTileEntity getWorldTile (World world) {
		if (world.provider.getDimension() != dimension && !world.isRemote) {
			return getServerTile();
		}

		if (tile == null || tile.get() == null) {
			tile = new WeakReference<>(WorldUtil.getTileEntity(clazz, world, pos));
		}

		return tile.get();
	}

	public ImmanenceTileEntity getServerTile () {
		if (tile == null || tile.get() == null) {
			tile = new WeakReference<>(WorldUtil.getTileEntity(clazz, dimension, pos));
		}

		return tile.get();
	}

	public ManifestTileEntity getManifestServerTile () {
		if (ManifestTileEntity.class.isAssignableFrom(this.clazz)) {
			return (ManifestTileEntity) getServerTile();
		} else {
			return null;
		}
	}

	@Nullable
	public ImmanenceTileEntity getTile () {
		if (tile == null) {
			return null;
		}

		return tile.get();
	}

	public void updateTile (ImmanenceTileEntity tile) {
		if (this.isValid() || tile.getPos() != this.pos || tile.dimension != this.dimension || !this.clazz.equals(tile.getClass()) || !this.uuid.equals(tile.uuid)) {
			return;
		}

		this.tile = new WeakReference<>(tile);
	}

	public boolean isValid () {
		return tile != null && tile.get() != null && tile.get().getWorld().isBlockLoaded(pos);
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
