package com.aranaira.arcanearchives.block;

import com.aranaira.arcanearchives.block.entity.RadiantResonatorBlockEntity;
import com.aranaira.arcanearchives.init.ModBlockEntities;
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
  public TileEntity createTileEntity(BlockState state, IBlockReader level) {
    return new RadiantResonatorBlockEntity(ModBlockEntities.RADIANT_RESONATOR.get());
  }
}
