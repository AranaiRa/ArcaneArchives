package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.tileentities.MandalicKeystoneTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MandalicKeystoneBlock extends TemplateBlock {

  public static final String name = "mandalic_keystone";

  public MandalicKeystoneBlock(Material material) {
    super(material);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new MandalicKeystoneTile();
  }
}
