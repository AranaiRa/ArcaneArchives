package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MakeshiftResonatorBlock extends TemplateBlock {
  public static BooleanProperty FILLED = BooleanProperty.create("filled");

  public MakeshiftResonatorBlock(Block.Properties properties) {
    super(properties);
    setDefaultState(getDefaultState().with(FILLED, false));
  }


  @Override
  public ActionResultType onBlockActivated(BlockState state, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
    //if (!state.get(FILLED)) {
/*      if (!playerIn.capabilities.isCreativeMode) {
        ItemStack stack = playerIn.getHeldItem(hand);
        IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (cap != null) {
          if (cap.getTankProperties().length > 0) {
            FluidStack contents = cap.getTankProperties()[0].getContents();
            if (contents != null) {
              if (contents.getFluid() == FluidRegistry.WATER) {
                if (contents.amount == 1000) {
                  if (worldIn.isRemote) {
                    return true;
                  }
                  cap.drain(1000, true);
                  playerIn.setHeldItem(hand, stack.getItem().getContainerItem(stack));
                  worldIn.setBlockState(pos, state.with(FILLED, true));
                  return true;
                } else if (contents.amount > 1000) {
                  if (!worldIn.isRemote) {
                    cap.drain(1000, true);
                    worldIn.setBlockState(pos, state.with(FILLED, true));
                  }

                  return true;
                }
              }
            }
          }
        }
      } else {
        worldIn.setBlockState(pos, state.with(FILLED, true));
        return true;
      }
    }*/
    return super.onBlockActivated(state, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
  }

  // TODO: Fix
  //@Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.GOLD + I18n.format(""));
  }

  // TODO: Move to Registry
/*  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }*/

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

/*  @Override
  public TileEntity createTileEntity(World world, BlockState state) {
    return new MakeshiftResonatorTile();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FILLED);
  }*/

  @Override
  public void fillWithRain(World worldIn, BlockPos pos) {
    BlockState state = worldIn.getBlockState(pos);
    if (state.getBlock() != this || state.get(FILLED)) {
      return;
    }
    worldIn.setBlockState(pos, state.with(FILLED, true));
  }

/*  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    if (face == Direction.UP) {
      return BlockFaceShape.BOWL;
    } else {
      return face == Direction.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
  }*/
}
