package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class TroveItemHandler implements IItemHandler, INBTSerializable<NBTTagCompound>
{
	private int count = 0;
	private ItemStack reference = ItemStack.EMPTY;

	public TroveItemHandler()
	{
	}

	@Override
	public int getSlots()
	{
		return 1;
	}

	public int getCount()
	{
		return count;
	}

	public ItemStack getItem()
	{
		return this.reference;
	}

	public void setItem(ItemStack reference)
	{
		this.reference = reference.copy();
		this.reference.setCount(1);
	}

	public boolean isEmpty()
	{
		return count == 0;
	}

	public int insert(int amount)
	{
		this.count += amount;
		return this.count;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		ItemStack result = reference.copy();
		result.setCount(Math.min(this.count, result.getMaxStackSize()));

		return result;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if(amount > reference.getMaxStackSize()) amount = reference.getMaxStackSize();

		if(this.count < amount) amount = this.count;

		ItemStack result = getStackInSlot(0);

		if(amount < result.getCount()) result.setCount(amount);
		if(simulate) return result;

		this.count -= amount;

		return result;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return reference.getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger(Tags.COUNT, this.count);
		result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
		return result;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.count = nbt.getInteger(Tags.COUNT);
		this.reference = new ItemStack(nbt.getCompoundTag(Tags.REFERENCE));
	}

	public static class Tags
	{
		public static final String COUNT = "COUNT";
		public static final String REFERENCE = "REFERENCE";
		public static final String SLOT = "SLOT";

		public Tags()
		{
		}
	}
}
