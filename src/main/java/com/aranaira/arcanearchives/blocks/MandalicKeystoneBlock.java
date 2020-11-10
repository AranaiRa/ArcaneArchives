package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tileentities.MandalicKeystoneTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class MandalicKeystoneBlock extends TemplateBlock {

  public static final String name = "mandalic_keystone";

  public MandalicKeystoneBlock(Block.Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new MandalicKeystoneTile();
  }
}
