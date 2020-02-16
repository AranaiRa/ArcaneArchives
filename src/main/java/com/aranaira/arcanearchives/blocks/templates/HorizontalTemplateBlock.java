package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO: Horizontal versus omnidirectional
@SuppressWarnings("deprecation")
public abstract class HorizontalTemplateBlock extends TemplateBlock implements IFacing {
  public HorizontalTemplateBlock(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty(getFacingProperty(), EnumFacing.NORTH));
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(getFacingProperty(), EnumFacing.byIndex(meta & 7));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(getFacingProperty()).getIndex();
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    return getDefaultState().withProperty(getFacingProperty(), EnumFacing.fromAngle(placer.rotationYaw).getOpposite());
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, getFacingProperty());
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty(getFacingProperty(), rot.rotate(state.getValue(getFacingProperty())));
  }

  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation(state.getValue(getFacingProperty())));
  }

/*  @Override
  public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
    AATileEntity tile = WorldUtil.getTileEntity(AATileEntity.class, world, pos);
    boolean result = super.rotateBlock(world, pos, axis);
    if (tile != null) {
      tile.validate();
      world.setTileEntity(pos, tile);
    }
    return result;
  }*/


}
