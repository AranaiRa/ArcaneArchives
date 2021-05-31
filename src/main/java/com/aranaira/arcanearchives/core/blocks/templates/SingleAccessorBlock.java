package com.aranaira.arcanearchives.core.blocks.templates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public abstract class SingleAccessorBlock extends Block {
  public static DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  public static BooleanProperty ACCESSOR = BooleanProperty.create("accessor");

  private final Direction offset;

  public SingleAccessorBlock(Properties properties, Direction offset) {
    super(properties);
    this.offset = offset;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(FACING, ACCESSOR);
  }
}
