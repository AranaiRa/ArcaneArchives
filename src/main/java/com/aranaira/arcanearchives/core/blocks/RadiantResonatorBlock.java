package com.aranaira.arcanearchives.core.blocks;

import com.aranaira.arcanearchives.core.blocks.entities.RadiantResonatorBlockEntity;
import com.aranaira.arcanearchives.core.init.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class RadiantResonatorBlock extends Block {
  public RadiantResonatorBlock(Properties p_i48440_1_) {
    super(p_i48440_1_);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new RadiantResonatorBlockEntity(ModBlockEntities.RADIANT_RESONATOR.get());
  }
}
