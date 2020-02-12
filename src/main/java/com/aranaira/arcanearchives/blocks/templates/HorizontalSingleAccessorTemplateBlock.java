package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.interfaces.ISingleAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class HorizontalSingleAccessorTemplateBlock extends HorizontalTemplateBlock implements ISingleAccessor {
  public HorizontalSingleAccessorTemplateBlock(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), EnumFacing.NORTH).withProperty(getAccessorProperty(), false));
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(getFacingProperty(), EnumFacing.byIndex(meta >> 1)).withProperty(getAccessorProperty(), (meta & 1) != 0);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(getFacingProperty()).getIndex() << 1 ^ (state.getValue(getAccessorProperty()) ? 1 : 0);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, getFacingProperty(), getAccessorProperty());
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    super.breakBlock(world, pos, state);
    if (state.getValue(getAccessorProperty())) {
      BlockPos origin = findBody(state, world, pos);
      if (origin == pos) {
        return;
      }
      IBlockState originState = world.getBlockState(origin);
      if (originState.getBlock() == state.getBlock()) {
        world.destroyBlock(origin, true);
      }
    } else {
      BlockPos accessor = findAccessor(state, world, pos);
      if (accessor == pos) {
        return;
      }
      IBlockState accessorState = world.getBlockState(accessor);
      if (accessorState.getBlock() == state.getBlock()) {
        world.destroyBlock(accessor, false);
      }
    } // TODO: Damage particles???
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    if (state.getValue(getAccessorProperty())) {
      return;
    }

    BlockPos accessor = findAccessor(state, world, pos);
    if (accessor == pos) {
      return;
    }

    if (!createAccessor(state, world, pos)) {
      ArcaneArchives.logger.error("Unable to create accessor for " + state.getBlock().getRegistryName().toString() + " at " + accessor.toString() + " (origin: " + pos.toString() + ")");
    }
  }
}
