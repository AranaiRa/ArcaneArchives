package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class BrazierFire extends BlockTemplate implements IHasModel {

  public BrazierFire() {
    super("brazier_of_hoarding_fire", Material.FIRE);
    setLightLevel(16 / 16f);
    setHardness(3f);
  }

  @Override
  public boolean hasOBJModel() {
    return true;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(BlockState state) {
    return false;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
