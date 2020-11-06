package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.TankMode;
import net.minecraft.block.*;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.logging.log4j.core.config.Order;

public class DispenseAmphora implements IDispenseItemBehavior {
	private static final DispenseAmphora INSTANCE = new DispenseAmphora();

	public static DispenseAmphora getInstance () {
		return INSTANCE;
	}

	private DispenseAmphora () {
	}

	private void success (IBlockSource source) {
		this.playDispenseSound(source);
		this.spawnDispenseParticles(source, source.getBlockState().getValue(DispenserBlock.FACING));
	}

	@Override
	public ItemStack dispense (IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
		BlockPos target = source.getBlockPos().offset(facing);
		BlockState targetState = world.getBlockState(target);
		Block targetBlock = targetState.getBlock();

		AmphoraUtil util = new AmphoraUtil(stack);
		TankMode originalMode = util.getMode();

		if (targetState.getBlock().isReplaceable(world, target) && !(targetBlock instanceof BlockLiquid) && !(targetBlock instanceof IFluidBlock)) {
			// Try dispensing
			IFluidHandler cap = util.getCapability();
			if (cap == null) {
				return stack;
			}

			FluidStack output = util.getFluidStack(cap);
			if (output == null) {
				return stack;
			}

			util.setMode(TankMode.DRAIN);
			FluidActionResult result = FluidUtil.tryPlaceFluid(null, world, target, stack, output);
			util.setMode(originalMode);
			if (result.isSuccess()) {
				success(source);
				return result.getResult();
			}

			return stack; //super.dispenseStack(source, stack);
		} else {
			util.setMode(TankMode.FILL);
			FluidActionResult actionResult = FluidUtil.tryPickUpFluid(stack, null, world, target, facing.getOpposite());
			util.setMode(originalMode);
			success(source);
			if (actionResult.isSuccess()) {
				return actionResult.getResult();
			} else {
				return stack;
			}
		}
	}

	*
 * Play the dispense sound from the specified block.
 * <p>
 * Order clients to display dispense particles from the specified block and facing.
 * <p>
 * Order clients to display dispense particles from the specified block and facing.

	protected void playDispenseSound (IBlockSource source) {
		source.getWorld().playEvent(1000, source.getBlockPos(), 0);
	}

	*
 * Order clients to display dispense particles from the specified block and facing.

	protected void spawnDispenseParticles (IBlockSource source, Direction facingIn) {
		source.getWorld().playEvent(2000, source.getBlockPos(), this.getWorldEventDataFrom(facingIn));
	}

	private int getWorldEventDataFrom (Direction facingIn) {
		return facingIn.getXOffset() + 1 + (facingIn.getZOffset() + 1) * 3;
	}
}
