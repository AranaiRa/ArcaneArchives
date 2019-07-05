package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.types.UpgradeType;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class VoidingFluidTank extends FluidTank {
	private boolean voiding = false;
	private OptionalUpgradesHandler optionals = null;

	public VoidingFluidTank (int capacity) {
		super(capacity);
	}

	public void setOptions (OptionalUpgradesHandler optionals) {
		this.optionals = optionals;
	}

	public VoidingFluidTank (@Nullable FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
	}

	public VoidingFluidTank (Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}

	public boolean isVoiding () {
		return optionals.hasUpgrade(UpgradeType.VOID) && getCapacity() == getFluidAmount();
	}

	@Override
	public int fillInternal (FluidStack resource, boolean doFill) {
		if (isVoiding() && resource != null && fluid != null && fluid.isFluidEqual(resource)) {
			int result = resource.amount;
			super.fillInternal(resource, doFill);
			resource.amount = 0;
			return result;
		}
		return super.fillInternal(resource, doFill);
	}

	@Override
	public boolean canFill () {
		if (isVoiding()) {
			return true;
		}
		return super.canFill();
	}
}
