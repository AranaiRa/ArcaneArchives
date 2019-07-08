package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.TankMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nonnull;

public class DispenseAmphora extends BehaviorDefaultDispenseItem {
	private static final DispenseAmphora INSTANCE = new DispenseAmphora();

	public static DispenseAmphora getInstance () {
		return INSTANCE;
	}

	private DispenseAmphora () {
	}

	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	@Override
	@Nonnull
	public ItemStack dispenseStack (@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
		World world = source.getWorld();
		EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
		BlockPos target = source.getBlockPos().offset(facing);
		IBlockState targetState = world.getBlockState(target);
		Block targetBlock = targetState.getBlock();

		AmphoraUtil util = new AmphoraUtil(stack);
		TankMode originalMode = util.getMode();

		if (targetState.getBlock().isReplaceable(world, target) && !(targetBlock instanceof BlockLiquid) && !(targetBlock instanceof IFluidBlock)) {
			// Try dispensing
			FluidStack output = util.getFluidStack(util.getCapability());
			if (output == null) {
				return stack;
				//return super.dispenseStack(source, stack);
			}

			util.setMode(TankMode.DRAIN);
			FluidActionResult result = FluidUtil.tryPlaceFluid(null, world, target, stack, output);
			util.setMode(originalMode);
			if (result.isSuccess()) {
				return result.getResult();
			}

			return stack; //super.dispenseStack(source, stack);
		} else {
			util.setMode(TankMode.FILL);
			FluidActionResult actionResult = FluidUtil.tryPickUpFluid(stack, null, world, target, facing.getOpposite());
			util.setMode(originalMode);
			ItemStack resultStack = actionResult.getResult();
			//if (!actionResult.isSuccess() || resultStack.isEmpty()) {
			//	return super.dispenseStack(source, stack);
			//}
			return resultStack;
		}
	}
}
