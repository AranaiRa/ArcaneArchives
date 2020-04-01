package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.OmniTemplateBlock;
import com.aranaira.arcanearchives.init.ModItems;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tileentities.StoredIdTile;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"NullableProblems"})
public class QuartzCluster extends OmniTemplateBlock {
  public QuartzCluster(Material materialIn) {
    super(materialIn);
    this.storesId = true;
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  protected List<ItemStack> generateItemDrops(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack harvestTool) {
    if (!(te instanceof StoredIdTile)) {
      throw new IllegalStateException("Invalid tile entity found when breaking Quartz Cluster, got " + te.getClass() + " instead of StoredIdTile.");
    }

    StoredIdTile tile = (StoredIdTile) te;
    if (tile.getNetworkId() == null) {
      throw new IllegalStateException("Invalid tile entity found when breaking Quartz Cluster. Has null network ID.");
    }
    ItemStack quartz = new ItemStack(ModItems.RadiantQuartz);
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(quartz);
    tag.setUniqueId(Tags.networkId, tile.getNetworkId());
    return Collections.singletonList(quartz);
  }

  @Override
  protected List<ItemStack> generateSilkTouchDrops(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack harvestTool) {
    return super.generateSilkTouchDrops(world, player, pos, state, te, harvestTool);
  }
}
