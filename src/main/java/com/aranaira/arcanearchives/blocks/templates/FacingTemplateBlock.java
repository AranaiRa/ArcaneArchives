package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"deprecation", "NullableProblems", "WeakerAccess", "unused"})
public abstract class FacingTemplateBlock extends TemplateBlock implements IFacingBlock {
  public FacingTemplateBlock(Material materialIn) {
    super(materialIn);
  }

  public FacingTemplateBlock setDefaultFacing(EnumFacing facing) {
    if (!getFacingProperty().getAllowedValues().contains(facing)) {
      throw new IllegalArgumentException("Invalid facing: cannot be contained within property " + getFacingProperty().toString());
    }

    setDefaultState(getDefaultState().withProperty(getFacingProperty(), facing));
    return this;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(getFacingProperty(), EnumFacing.byIndex(meta & 7));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(getFacingProperty()).getIndex();
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

  @Override
  public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
    TileEntity tile = world.getTileEntity(pos);
    boolean result = super.rotateBlock(world, pos, axis);
    if (tile != null) {
      tile.validate();
      world.setTileEntity(pos, tile);
    }
    return result;
  }
}
