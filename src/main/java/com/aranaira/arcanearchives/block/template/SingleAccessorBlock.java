package com.aranaira.arcanearchives.block.template;

import com.aranaira.arcanearchives.api.RelativeSide;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
  public void playerDestroy(World level, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!level.isClientSide()) {
      if (state.getValue(ACCESSOR)) {
        level.destroyBlock(calculateOrigin(pos, state), true);
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
      } else {
        level.destroyBlock(calculateAccessor(pos, state), false);
      }
    }
  }

  @Override
  public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (state.getValue(ACCESSOR) || level.isClientSide()) {
      return;
    }

    BlockPos accessor = calculateAccessor(pos, state);
    level.setBlockAndUpdate(accessor, state.setValue(ACCESSOR, true));
    super.onPlace(state, level, pos, oldState, isMoving);
  }

  @Override
  public void neighborChanged(BlockState state, World level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
    if (!level.isClientSide()) {
      BlockPos check;
      if (state.getValue(ACCESSOR)) {
        check = calculateOrigin(pos, state);
      } else {
        check = calculateAccessor(pos, state);
      }
      BlockState other = level.getBlockState(check);
      if (other.getBlock() != this) {
        level.destroyBlock(pos, !state.getValue(ACCESSOR));
      }
    }
  }

  @Override
  public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
    if (newState.getBlock() == this) {
      return;
    }
    if (state.getValue(ACCESSOR)) {
      level.destroyBlock(calculateOrigin(pos, state), true);
    } else {
      level.destroyBlock(calculateAccessor(pos, state), false);
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }

  @Override
  public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    BlockPos actual = pos;
    if (state.getValue(ACCESSOR)) {
      actual = calculateOrigin(pos, state);
    }
    return blockActivate(state, level, actual, player, handIn, hit, pos);
  }

  public abstract ActionResultType blockActivate (BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray, BlockPos origin);
}
