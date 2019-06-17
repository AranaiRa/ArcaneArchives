package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.util.ItemUtilities;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class TroveItemHandler implements IItemHandler, INBTSerializable<NBTTagCompound> {
	private static int COUNT_MULTIPLIER_PER_UPGRADE = 1;
	private static int BASE_COUNT = 64 * 64;
	public static int MAX_UPGRADES = 9;
	public static Item UPGRADE_ITEM = ItemRegistry.COMPONENT_MATERIALINTERFACE;
	private int upgrades = 0;
	private int count = 0;
	private ItemStack reference = ItemStack.EMPTY;
	private final Runnable updater;

	public TroveItemHandler (Runnable updater) {
		this.updater = updater;
	}

	private void update () {
		this.updater.run();
	}

	@Override
	public int getSlots () {
		return 2;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot (int slot) {
		if (slot == 0) {
			ItemStack result = reference.copy();
			result.setCount(Math.min(this.count, result.getMaxStackSize()));

			return result;
		} else {
			return ItemStack.EMPTY;
		}
	}

	public int getUpgrades () {
		return upgrades;
	}

	public int getMaxCount () {
		return BASE_COUNT + BASE_COUNT * (upgrades * COUNT_MULTIPLIER_PER_UPGRADE);
	}

	public int getUpgradeCount () {
		return BASE_COUNT * COUNT_MULTIPLIER_PER_UPGRADE;
	}

	@Nonnull
	@Override
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (ItemUtilities.areStacksEqualIgnoreSize(reference, stack)) {
			int thisCount = stack.getCount();
			int diff = 0;

			if (count + thisCount > getMaxCount()) {
				diff = (count + thisCount) - getMaxCount();
			}

			if (simulate) {
				if (diff == 0) {
					return ItemStack.EMPTY;
				}
				ItemStack result = stack.copy();
				result.setCount(diff);
				return result;
			}

			if (diff != 0) {
				count += thisCount - diff;
				ItemStack result = stack.copy();
				result.setCount(diff);
				update();
				return result;
			} else {
				count += stack.getCount();
				update();
				return ItemStack.EMPTY;
			}
		} else {
			return stack;
		}
	}

	public ItemStack getItemCurrent () {
		if (count == 0) {
			return ItemStack.EMPTY;
		}

		return this.reference;
	}

	@Nonnull
	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		if (amount > reference.getMaxStackSize()) {
			amount = reference.getMaxStackSize();
		}

		if (this.count < amount) {
			amount = this.count;
		}

		ItemStack result = getStackInSlot(0);

		if (amount < result.getCount()) {
			result.setCount(amount);
		}
		if (simulate) {
			return result;
		}

		this.count -= amount;
		update();

		return result;
	}

	@Override
	public int getSlotLimit (int slot) {
		return reference.getMaxStackSize();
	}

	@Override
	public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		return ItemUtilities.areStacksEqualIgnoreSize(reference, stack);
	}

	public boolean upgrade () {
		if (upgrades + 1 <= MAX_UPGRADES) {
			upgrades += 1;
			update();
			return true;
		}

		return false;
	}

	public boolean downgrade () {
		if (upgrades - 1 > 0) {
			int new_max = getMaxCount() - getUpgradeCount();
			if (getCount() >= new_max) {
				return false;
			} else {
				upgrades -= 1;
				update();

				return true;
			}
		}

		return false;
	}

	public int getCount () {
		return count;
	}

	public ItemStack getItem () {
		return this.reference;
	}

	public int getPacked () {
		return RecipeItemHelper.pack(this.reference);
	}

	public void setItem (ItemStack reference) {
		this.reference = reference.copy();
		this.reference.setCount(1);
		update();
	}

	public boolean isEmpty () {
		return count == 0;
	}

	@Override
	public NBTTagCompound serializeNBT () {
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger(Tags.COUNT, this.count);
		result.setTag(Tags.REFERENCE, this.reference.serializeNBT());
		result.setInteger(Tags.UPGRADES, this.upgrades);
		return result;
	}

	@Override
	public void deserializeNBT (NBTTagCompound nbt) {
		this.count = nbt.getInteger(Tags.COUNT);
		this.reference = new ItemStack(nbt.getCompoundTag(Tags.REFERENCE));
		this.upgrades = nbt.getInteger(Tags.UPGRADES);
	}

	public static class Tags {
		public static final String COUNT = "COUNT";
		public static final String REFERENCE = "REFERENCE";
		public static final String UPGRADES = "UPGRADES";

		public Tags () {
		}
	}
}
