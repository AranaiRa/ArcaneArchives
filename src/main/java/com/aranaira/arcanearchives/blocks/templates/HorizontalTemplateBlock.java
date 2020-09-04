package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class HorizontalTemplateBlock extends FacingTemplateBlock implements IFacingBlock {
  public static PropertyDirection FACING = BlockHorizontal.FACING;

  public HorizontalTemplateBlock(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), EnumFacing.NORTH));
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    return getDefaultState().withProperty(getFacingProperty(), EnumFacing.fromAngle(placer.rotationYaw).getOpposite());
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    worldIn.setBlockState(pos, state.withProperty(getFacingProperty(), EnumFacing.fromAngle(placer.rotationYaw).getOpposite()));
  }

  @Override
  public PropertyDirection getFacingProperty() {
    return FACING;
  }
}
