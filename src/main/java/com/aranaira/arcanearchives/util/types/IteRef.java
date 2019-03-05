package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class IteRef
{
	public UUID uuid;
	public BlockPos pos;
	public int dimension;
	public WeakReference<ImmanenceTileEntity> tile;
	public Class<? extends ImmanenceTileEntity> clazz;

	public IteRef(BlockPos pos, int dimension, UUID tileID, Class<? extends ImmanenceTileEntity> clazz)
	{
		this.pos = pos;
		this.dimension = dimension;
		this.clazz = clazz;
		this.uuid = tileID;
	}

	public IteRef(ImmanenceTileEntity tile)
	{
		this.pos = tile.getPos();
		this.dimension = tile.dimension;
		this.clazz = tile.getClass();
		this.uuid = tile.tileID;
		this.tile = new WeakReference<>(tile);
	}

	public ImmanenceTileEntity getWorldTile(World world)
	{
		if(world.provider.getDimension() != dimension) return getServerTile();

		if(tile == null || tile.get() == null)
		{
			tile = new WeakReference<>(WorldUtil.getTileEntity(clazz, world, pos));
		}

		return tile.get();
	}

	public ImmanenceTileEntity getServerTile()
	{
		if(tile == null || tile.get() == null)
		{
			tile = new WeakReference<>(WorldUtil.getTileEntity(clazz, dimension, pos));
		}

		return tile.get();
	}

	public boolean isValid()
	{
		return tile != null && tile.get() != null;
	}

	public boolean isActive()
	{
		if(tile == null) return false;

		ImmanenceTileEntity ite = tile.get();

		if(ite == null) return false;

		return ite.isActive();
	}

	public int networkPriority()
	{
		if(tile == null || tile.get() == null) return -999999999;

		//noinspection ConstantConditions
		return tile.get().networkPriority;
	}
}
