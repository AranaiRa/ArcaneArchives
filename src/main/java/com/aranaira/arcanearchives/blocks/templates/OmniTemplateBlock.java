package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;

@SuppressWarnings({"deprecation", "WeakerAccess", "unused", "NullableProblems"})
public class OmniTemplateBlock extends FacingTemplateBlock implements IFacingBlock {
  public static DirectionProperty FACING = DirectionalBlock.FACING;

  public OmniTemplateBlock(Block.Properties properties) {
    super(properties);
    setDefaultState(this.getDefaultState().with(getFacingProperty(), Direction.NORTH));
  }

  // TODO:
/*  @Override
  public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
    return this.getDefaultState().with(getFacingProperty(), Direction.getDirectionFromEntityLiving(pos, placer));
  }*/

  // TODO
/*  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    worldIn.setBlockState(pos, state.with(getFacingProperty(), Direction.getDirectionFromEntityLiving(pos, placer)), 2);
  }*/

  @Override
  public DirectionProperty getFacingProperty() {
    return FACING;
  }
}
