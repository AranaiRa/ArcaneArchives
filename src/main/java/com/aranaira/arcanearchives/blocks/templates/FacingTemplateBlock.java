package com.aranaira.arcanearchives.blocks.templates;

import com.aranaira.arcanearchives.blocks.interfaces.IFacingBlock;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;

@SuppressWarnings({"deprecation", "NullableProblems", "WeakerAccess", "unused", "unchecked"})
public abstract class FacingTemplateBlock extends TemplateBlock implements IFacingBlock {
  public FacingTemplateBlock(Block.Properties properties) {
    super(properties);
  }

  public FacingTemplateBlock setDefaultFacing(Direction facing) {
    if (!getFacingProperty().getAllowedValues().contains(facing)) {
      throw new IllegalArgumentException("Invalid facing: cannot be contained within property " + getFacingProperty().toString());
    }

    setDefaultState(getDefaultState().with(getFacingProperty(), facing));
    return this;
  }

/*  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, getFacingProperty());
  }*/

/*  @Override
  public BlockState withRotation(BlockState state, Rotation rot) {
    return state.with(getFacingProperty(), rot.rotate(state.get(getFacingProperty())));
  }

  @Override
  public BlockState withMirror(BlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation(state.get(getFacingProperty())));
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
  }*/
}
