package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.interfaces.INetworkBlock;
import com.aranaira.arcanearchives.blocks.templates.HorizontalSingleAccessorTemplateBlock;
import com.aranaira.arcanearchives.tileentities.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class CrystalWorkbenchBlock extends HorizontalSingleAccessorTemplateBlock implements INetworkBlock {
  public CrystalWorkbenchBlock(Material material) {
    super(material);
  }

  @Override
  public void breakBlock(World world, BlockPos pos, BlockState state) {
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
  public boolean causesSuffocation(BlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(BlockState state) {
    return false;
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

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
    if (state.getBlock() == this && state.getValue(ACCESSOR)) {
      BlockPos origin = findBody(state, world, new BlockPos(hitX, hitY, hitZ));
      if (!origin.equals(pos)) {
        BlockState originState = world.getBlockState(origin);
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
  public boolean hasTileEntity(BlockState state) {
    return !state.getValue(ACCESSOR);
  }

  @Override
  @Nullable
  public TileEntity createTileEntity(World world, BlockState state) {
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
