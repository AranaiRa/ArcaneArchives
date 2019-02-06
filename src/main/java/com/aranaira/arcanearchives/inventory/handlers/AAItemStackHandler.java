package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class AAItemStackHandler extends ItemStackHandler
{
	public static int MAX_STACK_SIZE = 64 * ConfigHandler.ConfigValues.iRadiantChestMultiplier;

	public AAItemStackHandler(int i)
	{
		super(i);
	}

	// TODO: This does not actually look at the slot
	@Override
	public int getSlotLimit(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		return getItemStackLimit(stack);
	}

	public int getItemStackLimit(@Nonnull ItemStack stack)
	{
		if(stack.isEmpty() || stack.getMaxStackSize() == 64)
		{
			return 64 * ConfigHandler.ConfigValues.iRadiantChestMultiplier;
		}
		if(!stack.isEmpty() && stack.getMaxStackSize() != 1)
		{
			return stack.getMaxStackSize() * ConfigHandler.ConfigValues.iRadiantChestMultiplier;
		}

		return 1;
	}

	@Override
	protected int getStackLimit(int slot, @Nonnull ItemStack stack)
	{
		return getSlotLimit(slot);
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return super.extractItem(slot, amount, simulate);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side == Side.CLIENT) {
			int i = 1;
		}

		super.setStackInSlot(slot, stack);
	}
}
