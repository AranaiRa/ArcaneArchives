package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class HorizontalTemplateBlock extends FacingTemplateBlock implements IFacingBlock {
  public static DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public HorizontalTemplateBlock(Block.Properties properties) {
    super(properties);
    setDefaultState(getDefaultState().with(getFacingProperty(), Direction.NORTH));
  }

/*  @Override
  public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
    return getDefaultState().with(getFacingProperty(), Direction.fromAngle(placer.rotationYaw).getOpposite());
  }*/

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    worldIn.setBlockState(pos, state.with(getFacingProperty(), Direction.fromAngle(placer.rotationYaw).getOpposite()));
  }

  @Override
  public DirectionProperty getFacingProperty() {
    return FACING;
  }
}
