package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.handlers.AAItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

@SuppressWarnings("unused") // TODO
public class LargeItemStackSlot extends SlotItemHandler
{
	private AAItemStackHandler handler;

	public LargeItemStackSlot(AAItemStackHandler itemHandler, int index, int xPosition, int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.handler = itemHandler;
	}

	@Override
	@Nonnull
	public ItemStack getStack () {
		return this.handler.getStackInSlot(this.getSlotIndex());
	}

	@Override
	public int getSlotStackLimit()
	{
		return AAItemStackHandler.MAX_STACK_SIZE;
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack)
	{
		return this.handler.getItemStackLimit(stack);
	}
}
