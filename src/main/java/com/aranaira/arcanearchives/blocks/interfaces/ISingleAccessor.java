package com.aranaira.arcanearchives.blocks.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISingleAccessor extends IAccessor, IFacingBlock {
  Rotation getAccessorRotation();

  Rotation getBodyRotation();

  default EnumFacing findFacing(IBlockState state, World world, BlockPos origin) {
    if (state.getPropertyKeys().contains(getFacingProperty())) {
      return state.getValue(getFacingProperty());
    }

    return EnumFacing.NORTH;
  }

  default BlockPos findAccessor(IBlockState state, World world, BlockPos origin) {
    if (state.getValue(getAccessorProperty())) {
      return origin;
    }

    EnumFacing facing = findFacing(state, world, origin);
    EnumFacing offset = getAccessorRotation().rotate(facing);
    return origin.offset(offset);
  }

  default BlockPos findBody(IBlockState state, World world, BlockPos origin) {
    if (!state.getValue(getAccessorProperty())) {
      return origin;
    }

    EnumFacing facing = findFacing(state, world, origin);
    EnumFacing offset = getBodyRotation().rotate(facing);
    return origin.offset(offset);
  }

  default boolean createAccessor(IBlockState state, World world, BlockPos origin) {
    if (state.getValue(getAccessorProperty())) {
      return true;
    }

    BlockPos accessor = findAccessor(state, world, origin);
    IBlockState accessorState = world.getBlockState(accessor);
    if (world.isAirBlock(accessor) || accessorState.getBlock().isReplaceable(world, accessor)) {
      world.setBlockState(accessor, state.withProperty(getAccessorProperty(), true), 3);
      return true;
    } else {
      return false;
    }
  }
}
