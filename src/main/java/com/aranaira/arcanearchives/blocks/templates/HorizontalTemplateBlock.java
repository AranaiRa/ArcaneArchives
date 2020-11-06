package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class HorizontalTemplateBlock extends FacingTemplateBlock implements IFacingBlock {
  public static PropertyDirection FACING = HorizontalBlock.FACING;

  public HorizontalTemplateBlock(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), Direction.NORTH));
  }

  @Override
  public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
    return getDefaultState().withProperty(getFacingProperty(), Direction.fromAngle(placer.rotationYaw).getOpposite());
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    worldIn.setBlockState(pos, state.withProperty(getFacingProperty(), Direction.fromAngle(placer.rotationYaw).getOpposite()));
  }

  @Override
  public PropertyDirection getFacingProperty() {
    return FACING;
  }
}
