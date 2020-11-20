/*package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.ISingleAccessor;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;

public abstract class HorizontalSingleAccessorTemplateBlock extends HorizontalTemplateBlock implements ISingleAccessor {
  public HorizontalSingleAccessorTemplateBlock(Block.Properties properties) {
    super(properties);
    setDefaultState(getDefaultState().with(getFacingProperty(), Direction.NORTH).with(getAccessorProperty(), false));
  }

*//*  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, getFacingProperty(), getAccessorProperty());
  }*//*

*//*  @Override
  public void breakBlock(World world, BlockPos pos, BlockState state) {
    super.breakBlock(world, pos, state);
    if (state.get(getAccessorProperty())) {
      BlockPos origin = findBody(state, world, pos);
      if (origin == pos) {
        return;
      }
      BlockState originState = world.getBlockState(origin);
      if (originState.getBlock() == state.getBlock()) {
        world.destroyBlock(origin, true);
      }
    } else {
      BlockPos accessor = findAccessor(state, world, pos);
      if (accessor == pos) {
        return;
      }
      BlockState accessorState = world.getBlockState(accessor);
      if (accessorState.getBlock() == state.getBlock()) {
        world.destroyBlock(accessor, false);
      }
    } // TODO: Damage particles???
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, BlockState state) {
    if (state.get(getAccessorProperty())) {
      return;
    }

    BlockPos accessor = findAccessor(state, world, pos);
    if (accessor == pos) {
      return;
    }

    if (!createAccessor(state, world, pos)) {
      ArcaneArchives.logger.error("Unable to create accessor for " + state.getBlock().getRegistryName().toString() + " at " + accessor.toString() + " (origin: " + pos.toString() + ")");
    }
  }*//*
}*/
