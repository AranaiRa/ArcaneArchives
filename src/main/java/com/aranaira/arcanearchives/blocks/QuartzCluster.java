package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.OmniTemplateBlock;
import net.minecraft.block.Block;

@SuppressWarnings({"NullableProblems"})
public class QuartzCluster extends OmniTemplateBlock {
  public QuartzCluster(Block.Properties properties) {
    super(properties);
    this.storesId = true;
  }

/*  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    return true;
  }*/

  // TODO: Implement in registry
/*  @Override
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }*/

/*  @Override
  protected List<ItemStack> generateItemDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack harvestTool) {
    // TODO: This idea sucks. Change my mind.
    if (!(te instanceof StoredIdTile)) {
      throw new IllegalStateException("Invalid tile entity found when breaking Quartz Cluster, got " + te.getClass() + " instead of StoredIdTile.");
    }

    StoredIdTile tile = (StoredIdTile) te;
    if (tile.getNetworkId() == null) {
      throw new IllegalStateException("Invalid tile entity found when breaking Quartz Cluster. Has null network ID.");
    }
    ItemStack quartz = new ItemStack(ModItems.RadiantQuartz);
    CompoundNBT tag = ItemUtils.getOrCreateTagCompound(quartz);
    tag.setUniqueId(Tags.networkId, tile.getNetworkId());
    return Collections.singletonList(quartz);
  }*/

/*  @Override
  protected List<ItemStack> generateSilkTouchDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack harvestTool) {
    return super.generateSilkTouchDrops(world, player, pos, state, te, harvestTool);
  }*/
}
