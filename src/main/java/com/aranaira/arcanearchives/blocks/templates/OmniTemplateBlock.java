package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"deprecation", "WeakerAccess", "unused", "NullableProblems"})
public class OmniTemplateBlock extends FacingTemplateBlock implements IFacingBlock {
  public static PropertyDirection FACING = DirectionalBlock.FACING;

  public OmniTemplateBlock(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), Direction.NORTH));
  }

  @Override
  public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
    return this.getDefaultState().withProperty(getFacingProperty(), Direction.getDirectionFromEntityLiving(pos, placer));
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    worldIn.setBlockState(pos, state.withProperty(getFacingProperty(), Direction.getDirectionFromEntityLiving(pos, placer)), 2);
  }

  @Override
  public PropertyDirection getFacingProperty() {
    return FACING;
  }
}
