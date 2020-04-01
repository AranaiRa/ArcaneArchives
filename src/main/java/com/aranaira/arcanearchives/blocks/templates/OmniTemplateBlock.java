package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class OmniTemplateBlock extends FacingTemplateBlock implements IFacingBlock {
  public static PropertyDirection FACING = BlockDirectional.FACING;

  public OmniTemplateBlock(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), EnumFacing.NORTH));
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return this.getDefaultState().withProperty(getFacingProperty(), EnumFacing.getDirectionFromEntityLiving(pos, placer));
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.setBlockState(pos, state.withProperty(getFacingProperty(), EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
  }

  @Override
  public PropertyDirection getFacingProperty() {
    return FACING;
  }
}
