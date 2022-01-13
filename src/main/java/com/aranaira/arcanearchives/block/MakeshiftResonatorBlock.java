package com.aranaira.arcanearchives.block;

import com.aranaira.arcanearchives.init.ModBlockEntities;
import com.aranaira.arcanearchives.block.entity.MakeshiftResonatorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
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

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class MakeshiftResonatorBlock extends Block {
  public static BooleanProperty FILLED = BooleanProperty.create("filled");
  public static final VoxelShape SHAPE = VoxelUtil.multiOr(new double[]{1, 12, 1, 15, 16, 15}, new double[]{4, 0, 4, 12, 12, 12});

  public MakeshiftResonatorBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.defaultBlockState().setValue(FILLED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public void handleRain(World world, BlockPos pos) {
    if (!world.isClientSide) {
      BlockState stateAt = world.getBlockState(pos).setValue(FILLED, true);
      if (!stateAt.getValue(FILLED)) {
        world.setBlockAndUpdate(pos, stateAt.setValue(FILLED, true));
      }
      TileEntity te = world.getBlockEntity(pos);
      if (te instanceof MakeshiftResonatorBlockEntity) {
        ((MakeshiftResonatorBlockEntity) te).setFilled();
      }
    }
  }

  @Override
  public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
    if (state.getValue(FILLED)) {
      return ActionResultType.PASS;
    }

    if (player.isCreative()) {
      world.setBlockAndUpdate(pos, state.setValue(FILLED, true));
      TileEntity te = world.getBlockEntity(pos);
      if (te instanceof MakeshiftResonatorBlockEntity) {
        ((MakeshiftResonatorBlockEntity) te).setFilled();
      }
      return ActionResultType.SUCCESS;
    }

    ItemStack heldItem = player.getItemInHand(hand);
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
              player.setItemInHand(hand, cap.getContainer());
            }
            world.setBlockAndUpdate(pos, state.setValue(FILLED, true));
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof MakeshiftResonatorBlockEntity) {
              ((MakeshiftResonatorBlockEntity) te).setFilled();
            }
            return ActionResultType.SUCCESS;
          }
        }
      }
    }

    return super.use(state, world, pos, player, hand, ray);
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FILLED);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new MakeshiftResonatorBlockEntity(ModBlockEntities.MAKESHIFT_RESONATOR.get());
  }
}
