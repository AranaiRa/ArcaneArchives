package com.aranaira.arcanearchives.core.blocks;

import com.aranaira.arcanearchives.core.init.ModTiles;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.core.tiles.RadiantChestTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CrystalWorkbenchBlock extends Block {
  public CrystalWorkbenchBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new CrystalWorkbenchTile(ModTiles.CRYSTAL_WORKBENCH.get());
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (worldIn.isRemote) {
      return ActionResultType.SUCCESS;
    } else {
      // TODO: Tile entity library?
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof CrystalWorkbenchTile) {
        player.openContainer((CrystalWorkbenchTile) te);
      }
      return ActionResultType.CONSUME;
    }
  }


}
