package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.tiles.TestTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TestBlock extends Block {
  public TestBlock() {
    super(Material.GROUND);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TestTile();
  }
}
