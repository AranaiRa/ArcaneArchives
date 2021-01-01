package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import noobanidus.libs.noobutil.util.VoxelUtil;

@SuppressWarnings("deprecation")
public class MakeshiftResonatorBlock extends Block {
  public static BooleanProperty FILLED = BooleanProperty.create("filled");
  public static final VoxelShape SHAPE = VoxelUtil.multiOr(new double[]{1, 12, 1, 15, 16, 15}, new double[]{4, 0, 4, 12, 12, 12});

  public MakeshiftResonatorBlock(Properties properties) {
    super(properties);
    this.setDefaultState(this.getDefaultState().with(FILLED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public void fillWithRain(World world, BlockPos pos) {
    BlockState stateAt = world.getBlockState(pos).with(FILLED, true);
    if (!stateAt.get(FILLED)) {
      world.setBlockState(pos, stateAt.with(FILLED, true));
    }
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
    if (state.get(FILLED)) {
      return ActionResultType.PASS;
    }

    if (player.isCreative()) {
      world.setBlockState(pos, state.with(FILLED, true));
      return ActionResultType.SUCCESS;
    }

    ItemStack heldItem = player.getHeldItem(hand);
    //noinspection ConstantConditions
    IFluidHandlerItem cap = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
    //noinspection ConstantConditions
    if (cap != null) {
      int tanks = cap.getTanks();
      for (int i = 0; i < tanks; i++) {
        FluidStack fluid = cap.getFluidInTank(i);
        if (fluid != FluidStack.EMPTY && fluid.getFluid() == Fluids.WATER) {
          if (fluid.getAmount() >= 1000) {
            boolean replace = false;
            if (fluid.getAmount() == 1000) {
              replace = true;
            }
            cap.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            if (replace) {
              player.setHeldItem(hand, cap.getContainer());
            }
            world.setBlockState(pos, state.with(FILLED, true));
            return ActionResultType.SUCCESS;
          }
        }
      }
    }

    return super.onBlockActivated(state, world, pos, player, hand, ray);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FILLED);
  }
}
