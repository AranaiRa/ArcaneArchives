package com.aranaira.arcanearchives.core.blocks.templates;

import com.aranaira.arcanearchives.api.RelativeSide;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public abstract class SingleAccessorBlock extends Block {
  public static DirectionProperty FACING = HorizontalBlock.FACING;
  public static BooleanProperty ACCESSOR = BooleanProperty.create("accessor");

  private final RelativeSide offset;

  public SingleAccessorBlock(Properties properties, RelativeSide offset) {
    super(properties);
    this.offset = offset;
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING, ACCESSOR);
  }

  public BlockPos calculateOrigin (BlockPos position, BlockState state) {
    if (!state.getValue(ACCESSOR)) {
      return position;
    }

    Direction facing = state.getValue(FACING);
    return position.relative(offset.getDirection(facing));
  }

  public BlockPos calculateAccessor (BlockPos position, BlockState state) {
    if (state.getValue(ACCESSOR)) {
      return position;
    }

    Direction facing = state.getValue(FACING);
    return position.relative(offset.getDirection(facing).getOpposite());
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    Direction facing = context.getPlayer() == null ? Direction.NORTH : Direction.fromYRot(context.getPlayer().yRot).getOpposite();
    return defaultBlockState().setValue(FACING, facing);
  }

  @Override
  public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!worldIn.isClientSide) {
      if (state.getValue(ACCESSOR)) {
        worldIn.destroyBlock(calculateOrigin(pos, state), true);
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
      } else {
        worldIn.destroyBlock(calculateAccessor(pos, state), false);
      }
      return;
    }
  }

  @Override
  public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (state.getValue(ACCESSOR) || worldIn.isClientSide) {
      return;
    }

    BlockPos accessor = calculateAccessor(pos, state);
    worldIn.setBlockAndUpdate(accessor, state.setValue(ACCESSOR, true));
    super.onPlace(state, worldIn, pos, oldState, isMoving);
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    if (!worldIn.isClientSide) {
      BlockPos check;
      if (state.getValue(ACCESSOR)) {
        check = calculateOrigin(pos, state);
      } else {
        check = calculateAccessor(pos, state);
      }
      BlockState other = worldIn.getBlockState(check);
      if (other.getBlock() != this) {
        worldIn.destroyBlock(pos, !state.getValue(ACCESSOR));
      }
    }
  }

  @Override
  public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (newState.getBlock() == this) {
      return;
    }
    if (state.getValue(ACCESSOR)) {
      worldIn.destroyBlock(calculateOrigin(pos, state), true);
    } else {
      worldIn.destroyBlock(calculateAccessor(pos, state), false);
    }
    super.onRemove(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    BlockPos actual = pos;
    if (state.getValue(ACCESSOR)) {
      actual = calculateOrigin(pos, state);
    }
    return blockActivate(state, worldIn, actual, player, handIn, hit, pos);
  }

  public abstract ActionResultType blockActivate (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray, BlockPos origin);
}
