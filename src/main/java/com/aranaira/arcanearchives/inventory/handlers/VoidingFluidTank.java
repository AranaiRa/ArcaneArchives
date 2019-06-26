package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class VoidingFluidTank extends FluidTank {
	private boolean voiding = false;

	public VoidingFluidTank (int capacity) {
		super(capacity);
	}

	public VoidingFluidTank (@Nullable FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
	}

	public VoidingFluidTank (Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}

	public boolean isVoiding () {
		return voiding;
	}

	public void setVoiding (boolean voiding) {
		this.voiding = voiding;
	}

	@Override
	public int fillInternal (FluidStack resource, boolean doFill) {
		if (voiding && resource != null && fluid.isFluidEqual(resource)) {
			int result = resource.amount;
			super.fillInternal(resource, doFill);
			resource.amount = 0;
			return result;
		}
		return super.fillInternal(resource, doFill);
	}

	@Override
	public boolean canFill () {
		if (voiding) {
			return true;
		}
		return super.canFill();
	}
}
