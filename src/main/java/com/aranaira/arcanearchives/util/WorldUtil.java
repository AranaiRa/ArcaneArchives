package com.aranaira.arcanearchives.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WorldUtil
{
	@Nullable
	public static <T> T getTileEntity(Class<T> clazz, IBlockAccess world, BlockPos pos) {
		return getTileEntity(clazz, world, pos, false);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getTileEntity(Class<T> clazz, IBlockAccess world, BlockPos pos, boolean forceChunkLoad)
	{
		if(world == null || pos == null) {
			return null;
		}

		if(world instanceof World)
		{
			if(!((World) world).isBlockLoaded(pos) && !forceChunkLoad) {
				return null;
			}
		}

		TileEntity te = world.getTileEntity(pos);

		if(te == null)
		{
			return null;
		}

		if(clazz.isInstance(te))
		{
			return (T) te;
		}

		return null;
	}

	public static boolean isChunkLoaded(World world, BlockPos pos)
	{
		return world.isBlockLoaded(pos);
	}
}
