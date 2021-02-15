package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.init.ModTiles;
import com.aranaira.arcanearchives.tiles.RadiantChestTile;
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

public class RadiantChestBlock extends Block {
  public RadiantChestBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new RadiantChestTile(ModTiles.RADIANT_CHEST.get());
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (worldIn.isRemote) {
      return ActionResultType.SUCCESS;
    } else {
      // TODO: Tile entity library?
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof RadiantChestTile) {
        player.openContainer((RadiantChestTile) te);
      }
      return ActionResultType.CONSUME;
    }
  }


}
