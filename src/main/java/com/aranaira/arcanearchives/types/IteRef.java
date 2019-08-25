package com.aranaira.arcanearchives.types;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.UUID;

public class IteRef {
	public UUID uuid;
	public BlockPos pos;
	public int dimension;
	public int networkPriority;
	public Class<? extends ImmanenceTileEntity> clazz;

	public IteRef (ImmanenceTileEntity tile) {
		this.pos = tile.getPos();
		this.dimension = tile.dimension;
		this.clazz = tile.getClass();
		this.uuid = tile.uuid;
	}

	public boolean shouldCull () {
		return getTile() == null;
	}

	private World getWorld () {
		return DimensionManager.getWorld(dimension);
	}

	@Nullable
	public ImmanenceTileEntity getTile () {
		World world = getWorld();
		if (!world.isBlockLoaded(pos)) {
			return null;
		}

		return WorldUtil.getTileEntity(clazz, world, pos);
	}

	public int priority () {
		return networkPriority;
	}
}
