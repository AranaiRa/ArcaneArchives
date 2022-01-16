package com.aranaira.arcanearchives.block;

import com.aranaira.arcanearchives.init.ModBlockEntities;
import com.aranaira.arcanearchives.block.entity.RadiantChestBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

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
  public TileEntity createTileEntity(BlockState state, IBlockReader level) {
    return new RadiantChestBlockEntity(ModBlockEntities.RADIANT_CHEST.get());
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (level.isClientSide()) {
      return ActionResultType.SUCCESS;
    } else {
      // TODO: Tile entity library?
      TileEntity te = level.getBlockEntity(pos);
      if (te instanceof RadiantChestBlockEntity) {
        NetworkHooks.openGui((ServerPlayerEntity) player, (RadiantChestBlockEntity) te, pos);
      }
      return ActionResultType.CONSUME;
    }
  }
}
