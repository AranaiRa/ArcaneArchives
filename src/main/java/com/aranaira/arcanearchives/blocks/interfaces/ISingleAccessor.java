package com.aranaira.arcanearchives.blocks.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISingleAccessor extends IAccessor {
  int getAccessorAngle ();

  default EnumFacing findFacing(IBlockState state, World world, BlockPos origin) {
    if (state.getPropertyKeys().contains(IFacing.FACING)) {
      return state.getValue(IFacing.FACING);
    }

    return EnumFacing.NORTH;
  }

  default BlockPos findAccessor(IBlockState state, World world, BlockPos origin) {
    if (state.getValue(getAccessorProperty())) {
      return origin;
    }

    EnumFacing facing = EnumFacing.fromAngle(findFacing(state, world, origin).getHorizontalAngle() - getAccessorAngle());
    return origin.offset(facing);
  }

  default BlockPos findBody(IBlockState state, World world, BlockPos origin) {
    if (!state.getValue(getAccessorProperty())) {
      return origin;
    }

    EnumFacing facing = EnumFacing.fromAngle(findFacing(state, world, origin).getHorizontalAngle() + getAccessorAngle());
    return origin.offset(facing);
  }

  default boolean createAccessor(IBlockState state, World world, BlockPos origin) {
    if (state.getValue(getAccessorProperty())) {
      return true;
    }

    BlockPos accessor = findAccessor(state, world, origin);
    if (world.isAirBlock(accessor)) {
      world.setBlockState(accessor, state.getBlock().getDefaultState().withProperty(getAccessorProperty(), true), 3);
      return true;
    } else {
      return false;
    }
  }
}
