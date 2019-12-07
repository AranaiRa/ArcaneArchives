package com.aranaira.arcanearchives.blocks;

import epicsquid.mysticallib.util.VoxelUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RadiantResonatorBlock extends Block {
  public static final VoxelShape SHAPE = VoxelUtil.multiOr(Block.makeCuboidShape(4, 6, 3,12, 7, 4), Block.makeCuboidShape(4, 6, 12,12, 7, 13), Block.makeCuboidShape(12, 6, 4,13, 7, 12), Block.makeCuboidShape(3, 6, 4,4, 7, 12), Block.makeCuboidShape(4, 11, 4,12, 12, 12), Block.makeCuboidShape(0, 14, 0,16, 16, 16), Block.makeCuboidShape(2, 13, 2,14, 14, 14), Block.makeCuboidShape(3.5, 12, 3.5,13.5, 13, 12.5), Block.makeCuboidShape(6, 8, 6,10, 11, 10), Block.makeCuboidShape(11.5, 10, 3.5,12.5, 12, 4.5), Block.makeCuboidShape(11.5, 7, 2.5,13.5, 10, 4.5), Block.makeCuboidShape(12.5, 3, 1.5,14.5, 7, 3.5), Block.makeCuboidShape(12.5, 1, 0.5,15.5, 3, 3.5), Block.makeCuboidShape(11.5, 10, 11.5,12.5, 12, 12.5), Block.makeCuboidShape(11.5, 7, 11.5,13.5, 10, 13.5), Block.makeCuboidShape(12.5, 3, 12.5,14.5, 7, 14.5), Block.makeCuboidShape(12.5, 1, 12.5,15.5, 3, 15.5), Block.makeCuboidShape(3.5, 10, 11.5,4.5, 12, 12.5), Block.makeCuboidShape(2.5, 7, 11.5,4.5, 10, 13.5), Block.makeCuboidShape(1.5, 3, 12.5,3.5, 7, 14.5), Block.makeCuboidShape(0.5, 1, 12.5,3.5, 3, 15.5), Block.makeCuboidShape(3.5, 10, 3.5,4.5, 12, 4.5), Block.makeCuboidShape(2.5, 7, 2.5,4.5, 10, 4.5), Block.makeCuboidShape(1.5, 3, 1.5,3.5, 7, 3.5), Block.makeCuboidShape(0.5, 1, 0.5,3.5, 3, 3.5));

  public RadiantResonatorBlock(Properties properties) {
    super(properties);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1.0f;
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
