package com.aranaira.arcanearchives.core.blocks;

import com.aranaira.arcanearchives.api.RelativeSide;
import com.aranaira.arcanearchives.core.blocks.templates.SingleAccessorBlock;
import com.aranaira.arcanearchives.core.init.ModTiles;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.core.tiles.RadiantChestTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
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
    this.setDefaultState(this.getDefaultState().with(ACCESSOR, false).with(FACING, Direction.NORTH));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return !state.get(ACCESSOR);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    if (state.get(ACCESSOR)) {
      return null;
    }
    return new CrystalWorkbenchTile(ModTiles.CRYSTAL_WORKBENCH.get());
  }

  @Override
  public ActionResultType blockActivate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray, BlockPos origin) {
    if (world.isRemote) {
      return ActionResultType.SUCCESS;
    } else {
      // TODO: Tile entity library?
      TileEntity te = world.getTileEntity(pos);
      if (te instanceof CrystalWorkbenchTile) {
        player.openContainer((CrystalWorkbenchTile) te);
      }
      return ActionResultType.CONSUME;
    }
  }
}
