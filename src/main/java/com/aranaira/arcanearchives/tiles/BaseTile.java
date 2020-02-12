package com.aranaira.arcanearchives.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public abstract class BaseTile extends TileEntity {
  public void stateUpdate() {
    if (world == null || world.isRemote) {
      return;
    }

    IBlockState state = world.getBlockState(getPos());
    world.notifyBlockUpdate(getPos(), state, state, 8);
  }
}
