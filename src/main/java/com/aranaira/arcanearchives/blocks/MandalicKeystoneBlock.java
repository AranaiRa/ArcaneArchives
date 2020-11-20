package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class MandalicKeystoneBlock extends TemplateBlock {

  public static final String name = "mandalic_keystone";

  public MandalicKeystoneBlock(Block.Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

/*  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new MandalicKeystoneTile();
  }*/
}
