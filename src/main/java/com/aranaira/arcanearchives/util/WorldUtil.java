package com.aranaira.arcanearchives.util;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class WorldUtil {
	@Nullable
	public static <T> T getTileEntity (Class<T> clazz, int dimension, BlockPos pos) {
		return getTileEntity(clazz, dimension, pos, false);
	}

	@Nullable
	public static <T> T getTileEntity (Class<T> clazz, int dimension, BlockPos pos, boolean forceChunkLoad) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server == null) {
			return null;
		}

		World world = DimensionManager.getWorld(dimension);

		if (world == null) {
			return null;
		}

		return getTileEntity(clazz, world, pos, forceChunkLoad);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getTileEntity (Class<T> clazz, IBlockAccess world, BlockPos pos, boolean forceChunkLoad) {
		if (world == null || pos == null) {
			return null;
		}

		if (world instanceof World) {
			if (!((World) world).isBlockLoaded(pos) && !forceChunkLoad) {
				return null;
			}
		}

		TileEntity te = world.getTileEntity(pos);

		if (te == null) {
			return null;
		}

		if (clazz.isInstance(te)) {
			return (T) te;
		}

		return null;
	}

	@Nullable
	public static <T> T getTileEntity (Class<T> clazz, IBlockAccess world, BlockPos pos) {
		return getTileEntity(clazz, world, pos, false);
	}

	public static boolean isChunkLoaded (World world, BlockPos pos) {
		return world.isBlockLoaded(pos);
	}

	public static void spawnInventoryInWorld (World world, double x, double y, double z, IItemHandler inventory) {
		spawnInventoryInWorld(world, new BlockPos(x, y, z), inventory);
	}

	public static void spawnInventoryInWorld (World world, BlockPos pos, IItemHandler inventory) {
		if (inventory != null && !world.isRemote) {
			for (int i = 0; i < inventory.getSlots(); i++) {
				if (!inventory.getStackInSlot(i).isEmpty()) {
					Block.spawnAsEntity(world, pos, inventory.getStackInSlot(i));
				}
			}
		}
	}
}
