/*package com.aranaira.arcanearchives.blocks.interfaces;

import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public interface INetworkBlock {
  @Nullable
  default UUID getNetworkId(World world, BlockPos pos) {
    NetworkedBaseTile tile = WorldUtil.getTileEntity(NetworkedBaseTile.class, world, pos);
    if (tile != null) {
      return tile.getNetworkId();
    }

    return null;
  }

  default boolean setNetworkId (World world, BlockPos pos, UUID uuid) {
    NetworkedBaseTile tile = WorldUtil.getTileEntity(NetworkedBaseTile.class, world, pos);
    if (tile != null) {
      tile.setNetworkId(uuid);
      return true;
    }

    return false;
  }
}*/
