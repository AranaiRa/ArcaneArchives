package com.aranaira.arcanearchives.core.blocks;

import com.aranaira.arcanearchives.api.RelativeSide;
import com.aranaira.arcanearchives.core.blocks.templates.SingleAccessorBlock;
import com.aranaira.arcanearchives.core.init.ModblockEntities;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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
    return new CrystalWorkbenchBlockEntity(ModblockEntities.CRYSTAL_WORKBENCH.get());
  }

  @Override
  public ActionResultType blockActivate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray, BlockPos origin) {
    if (world.isClientSide) {
      return ActionResultType.SUCCESS;
    } else {
      // TODO: Tile entity library?
      TileEntity te = world.getBlockEntity(pos);
      if (te instanceof CrystalWorkbenchBlockEntity) {
        player.openMenu((CrystalWorkbenchBlockEntity) te);
      }
      return ActionResultType.CONSUME;
    }
  }
}
