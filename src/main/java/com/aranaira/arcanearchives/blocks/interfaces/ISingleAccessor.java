package com.aranaira.arcanearchives.blocks.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISingleAccessor extends IAccessor, IFacingBlock {
  Rotation getAccessorRotation();

  Rotation getBodyRotation();

  default Direction findFacing(BlockState state, World world, BlockPos origin) {
    if (state.getPropertyKeys().contains(getFacingProperty())) {
      return state.get(getFacingProperty());
    }

    return Direction.NORTH;
  }

  default BlockPos findAccessor(BlockState state, World world, BlockPos origin) {
    if (state.get(getAccessorProperty())) {
      return origin;
    }

    Direction facing = findFacing(state, world, origin);
    Direction offset = getAccessorRotation().rotate(facing);
    return origin.offset(offset);
  }

  default BlockPos findBody(BlockState state, World world, BlockPos origin) {
    if (!state.get(getAccessorProperty())) {
      return origin;
    }

    Direction facing = findFacing(state, world, origin);
    Direction offset = getBodyRotation().rotate(facing);
    return origin.offset(offset);
  }

  default boolean createAccessor(BlockState state, World world, BlockPos origin) {
    if (state.get(getAccessorProperty())) {
      return true;
    }

    BlockPos accessor = findAccessor(state, world, origin);
    BlockState accessorState = world.getBlockState(accessor);
    if (world.isAirBlock(accessor) || accessorState.getBlock().isReplaceable(world, accessor)) {
      world.setBlockState(accessor, state.with(getAccessorProperty(), true), 3);
      return true;
    } else {
      return false;
    }
  }
}
