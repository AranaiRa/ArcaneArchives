package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import noobanidus.libs.noobutil.util.VoxelUtil;

public class MakeshiftResonatorBlock extends Block {
  public static final VoxelShape SHAPE = VoxelUtil.multiOr(new double[]{1, 12, 1, 15, 16, 15}, new double[]{4, 0, 4, 12, 12, 12});

  public MakeshiftResonatorBlock(Properties p_i48440_1_) {
    super(p_i48440_1_);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
    return SHAPE;
  }
}
