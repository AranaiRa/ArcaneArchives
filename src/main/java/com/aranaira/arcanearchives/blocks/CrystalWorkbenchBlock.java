package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.templates.HorizontalSingleAccessorTemplateBlock;
import com.aranaira.arcanearchives.tiles.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalWorkbenchBlock extends HorizontalSingleAccessorTemplateBlock {
  public CrystalWorkbenchBlock() {
    super(Material.IRON);
    this.setHardness(3f);
    setLightLevel(16f / 16f);
    setHarvestLevel("axe", 0);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.device.gemcutters_table"));
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    if (state.getValue(ACCESSOR)) {
      super.breakBlock(world, pos, state);
      return;
    }

    /*		LineHandler.removeLine(pos, world.provider.getDimension());*/

    TileEntity te = world.getTileEntity(pos);
    if (te instanceof CrystalWorkbenchTile) {
      IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
      ItemUtils.dropInventoryItems(world, pos, inv);
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean causesSuffocation(IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (state.getBlock() == this && state.getValue(ACCESSOR)) {
      BlockPos origin = findBody(state, world, new BlockPos(hitX, hitY, hitZ));
      if (!origin.equals(pos)) {
        IBlockState originState = world.getBlockState(origin);
        return onBlockActivated(world, origin, originState, playerIn, hand, facing, origin.getX() + 0.5f, origin.getY() + 0.5f, origin.getZ() + 0.5f);
      } else {
        return false;
      }
    }

    /*		LineHandler.removeLine(pos, playerIn.dimension);*/

    if (world.isRemote) {
      return true;
    }

    playerIn.openGui(ArcaneArchives.instance, AAGuiHandler.GuiType.CrystalWorkbench.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());

    return true;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return !state.getValue(ACCESSOR);
  }

  @Override
  @Nullable
  public TileEntity createTileEntity(World world, IBlockState state) {
    if (state.getValue(ACCESSOR)) {
      return null;
    }

    return new CrystalWorkbenchTile();
  }

  @Override
  public Rotation getAccessorRotation() {
    return Rotation.COUNTERCLOCKWISE_90;
  }

  @Override
  public Rotation getBodyRotation() {
    return Rotation.CLOCKWISE_90;
  }
  // TODO: Handle tile network ID transfer to itemblock upon destruction/breaking
}
