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

@SuppressWarnings("deprecation")
public abstract class SingleAccessorBlock extends Block {
  public static DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  public static BooleanProperty ACCESSOR = BooleanProperty.create("accessor");

  private final RelativeSide offset;

  public SingleAccessorBlock(Properties properties, RelativeSide offset) {
    super(properties);
    this.offset = offset;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(FACING, ACCESSOR);
  }

  public BlockPos calculateOrigin (BlockPos position, BlockState state) {
    if (!state.get(ACCESSOR)) {
      return position;
    }

    Direction facing = state.get(FACING);
    return position.offset(offset.getDirection(facing));
  }

  public BlockPos calculateAccessor (BlockPos position, BlockState state) {
    if (state.get(ACCESSOR)) {
      return position;
    }

    Direction facing = state.get(FACING);
    return position.offset(offset.getDirection(facing).getOpposite());
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    Direction facing = context.getPlayer() == null ? Direction.NORTH : Direction.fromAngle(context.getPlayer().rotationYaw).getOpposite();
    return getDefaultState().with(FACING, facing);
  }

  @Override
  public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
    if (!worldIn.isRemote) {
      if (state.get(ACCESSOR)) {
        worldIn.destroyBlock(calculateOrigin(pos, state), true);
        player.addStat(Stats.BLOCK_MINED.get(this));
        player.addExhaustion(0.005F);
        return;
      } else {
        worldIn.destroyBlock(calculateAccessor(pos, state), false);
      }
    }
    super.harvestBlock(worldIn, player, pos, state, te, stack);
  }

  @Override
  public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (state.get(ACCESSOR) || worldIn.isRemote) {
      return;
    }

    BlockPos accessor = calculateAccessor(pos, state);
    worldIn.setBlockState(accessor, state.with(ACCESSOR, true));
    super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    if (!worldIn.isRemote) {
      BlockPos check;
      if (state.get(ACCESSOR)) {
        check = calculateOrigin(pos, state);
      } else {
        check = calculateAccessor(pos, state);
      }
      BlockState other = worldIn.getBlockState(check);
      if (other.getBlock() != this) {
        worldIn.destroyBlock(pos, !state.get(ACCESSOR));
      }
    }
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() == this) {
      return;
    }
    if (state.get(ACCESSOR)) {
      worldIn.destroyBlock(calculateOrigin(pos, state), true);
    } else {
      worldIn.destroyBlock(calculateAccessor(pos, state), false);
    }
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    BlockPos actual = pos;
    if (state.get(ACCESSOR)) {
      actual = calculateOrigin(pos, state);
    }
    return blockActivate(state, worldIn, actual, player, handIn, hit, pos);
  }

  public abstract ActionResultType blockActivate (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray, BlockPos origin);
}
