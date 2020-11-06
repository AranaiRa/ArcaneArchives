package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"deprecation", "NullableProblems", "WeakerAccess", "unused", "unchecked"})
public abstract class FacingTemplateBlock extends TemplateBlock implements IFacingBlock {
  public FacingTemplateBlock(Material materialIn) {
    super(materialIn);
  }

  public FacingTemplateBlock setDefaultFacing(Direction facing) {
    if (!getFacingProperty().getAllowedValues().contains(facing)) {
      throw new IllegalArgumentException("Invalid facing: cannot be contained within property " + getFacingProperty().toString());
    }

    setDefaultState(getDefaultState().withProperty(getFacingProperty(), facing));
    return this;
  }

  @Override
  public BlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(getFacingProperty(), Direction.byIndex(meta & 7));
  }

  @Override
  public int getMetaFromState(BlockState state) {
    return state.getValue(getFacingProperty()).getIndex();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, getFacingProperty());
  }

  @Override
  public BlockState withRotation(BlockState state, Rotation rot) {
    return state.withProperty(getFacingProperty(), rot.rotate(state.getValue(getFacingProperty())));
  }

  @Override
  public BlockState withMirror(BlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation(state.getValue(getFacingProperty())));
  }

  @Override
  public boolean rotateBlock(World world, BlockPos pos, Direction axis) {
    TileEntity tile = world.getTileEntity(pos);
    boolean result = super.rotateBlock(world, pos, axis);
    if (tile != null) {
      tile.validate();
      world.setTileEntity(pos, tile);
    }
    return result;
  }
}
