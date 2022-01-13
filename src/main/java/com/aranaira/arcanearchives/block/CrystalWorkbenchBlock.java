package com.aranaira.arcanearchives.block;

import com.aranaira.arcanearchives.api.RelativeSide;
import com.aranaira.arcanearchives.block.template.SingleAccessorBlock;
import com.aranaira.arcanearchives.init.ModBlockEntities;
import com.aranaira.arcanearchives.block.entity.CrystalWorkbenchBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CrystalWorkbenchBlock extends SingleAccessorBlock {
  public CrystalWorkbenchBlock(Properties properties) {
    super(properties, RelativeSide.RIGHT);
    this.registerDefaultState(this.defaultBlockState().setValue(ACCESSOR, false).setValue(FACING, Direction.NORTH));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return !state.getValue(ACCESSOR);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    if (state.getValue(ACCESSOR)) {
      return null;
    }
    return new CrystalWorkbenchBlockEntity(ModBlockEntities.CRYSTAL_WORKBENCH.get());
  }

  @Override
  public ActionResultType blockActivate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray, BlockPos origin) {
    if (world.isClientSide) {
      return ActionResultType.SUCCESS;
    } else {
      // TODO: Tile entity library?
      TileEntity te = world.getBlockEntity(pos);
      if (te instanceof CrystalWorkbenchBlockEntity) {
        NetworkHooks.openGui((ServerPlayerEntity) player, (CrystalWorkbenchBlockEntity) te, pos);
      }
      return ActionResultType.CONSUME;
    }
  }
}
