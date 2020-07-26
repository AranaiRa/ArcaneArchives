package com.aranaira.arcanearchives.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

public class WorldUtil {
  @Nullable
  public static <T> T getTileEntity(Class<T> clazz, int dimension, BlockPos pos) {
    return getTileEntity(clazz, dimension, pos, false);
  }

  @Nullable
  public static <T> T getTileEntity(Class<T> clazz, int dimension, BlockPos pos, boolean forceChunkLoad) {
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
  public static <T> T getTileEntity(Class<T> clazz, IBlockAccess world, BlockPos pos, boolean forceChunkLoad) {
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
  public static <T> T getTileEntity(Class<T> clazz, IBlockAccess world, BlockPos pos) {
    return getTileEntity(clazz, world, pos, false);
  }

  public static int distanceSq(BlockPos pos1, BlockPos pos2) {
    int d1 = pos1.getX() - pos2.getX();
    int d2 = pos1.getY() - pos2.getY();
    int d3 = pos1.getZ() - pos2.getZ();
    return Math.abs(d1 * d1 + d2 * d2 + d3 * d3);
  }

  public static int distanceSqNoVertical(BlockPos pos1, BlockPos pos2) {
    int d1 = pos1.getX() - pos2.getX();
    int d2 = pos2.getZ() - pos2.getZ();
    return Math.abs(d1 * d1 + d2 * d2);
  }
}
