package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.ItemComparison;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class TroveItemHandler implements IItemHandler, INBTSerializable<NBTTagCompound>
{
	private static int COUNT_MULTIPLIER_PER_UPGRADE = 1;
	private static int BASE_COUNT = 64 * 64;
	public static int MAX_UPGRADES = 10;
	private int upgrades = 0;
	private int count = 0;
	private ItemStack reference = ItemStack.EMPTY;

	public TroveItemHandler()
	{
	}

	@Override
	public int getSlots()
	{
		return 2;
	}

	public boolean upgrade () {
		if (upgrades + 1 <= MAX_UPGRADES)
		{
			upgrades += 1;
			return true;
		}

		return false;
	}

	public int getUpgrades () {
		return upgrades;
	}

	public int getMaxCount () {
		return BASE_COUNT + BASE_COUNT * (upgrades * COUNT_MULTIPLIER_PER_UPGRADE);
	}

	public int getCount()
	{
		return count;
	}

	public ItemStack getItemCurrent () {
		if (count == 0) return ItemStack.EMPTY;

		return this.reference;
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
		if (slot == 0)
		{
			ItemStack result = reference.copy();
			result.setCount(Math.min(this.count, result.getMaxStackSize()));

			return result;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if (ItemComparison.areStacksEqualIgnoreSize(reference, stack)) {
			int thisCount = stack.getCount();
			int diff = 0;

			if (count + thisCount > getMaxCount()) {
				diff = (count + thisCount) - getMaxCount();
			}

			if (simulate) {
				if (diff == 0) return ItemStack.EMPTY;
				ItemStack result = stack.copy();
				result.setCount(diff);
				return result;
			}

			if (diff != 0) {
				count += thisCount - diff;
				ItemStack result = stack.copy();
				result.setCount(diff);
				return result;
			} else
			{
				count += stack.getCount();
				return ItemStack.EMPTY;
			}
		} else
		{
			return stack;
		}
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
		return ItemComparison.areStacksEqualIgnoreSize(reference, stack);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger(Tags.COUNT, this.count);
		result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
		result.setInteger(Tags.UPGRADES, this.upgrades);
		return result;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.count = nbt.getInteger(Tags.COUNT);
		this.reference = new ItemStack(nbt.getCompoundTag(Tags.REFERENCE));
		this.upgrades = nbt.getInteger(Tags.UPGRADES);
	}

	public static class Tags
	{
		public static final String COUNT = "COUNT";
		public static final String REFERENCE = "REFERENCE";
		public static final String UPGRADES = "UPGRADES";

		public Tags()
		{
		}
	}
}