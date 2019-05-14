package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

@Deprecated
public class AAItemStackHandler extends ItemStackHandler {
	public AAItemStackHandler (int i) {
		super(i);
	}

	@Override
	public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side == Side.CLIENT) {
			int i = 1;
		}

		super.setStackInSlot(slot, stack);
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	@Nonnull
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}
}
