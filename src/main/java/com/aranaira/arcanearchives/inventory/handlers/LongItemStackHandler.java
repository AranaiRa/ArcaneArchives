package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.types.SuperItemStack;
import com.aranaira.arcanearchives.types.lists.NonNullArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
	protected NonNullArrayList<SuperItemStack> stacks = new NonNullArrayList<>(() -> SuperItemStack.EMPTY);
	protected int size = 0;

	public LongItemStackHandler () {
	}

	public LongItemStackHandler (int size) {
		setSize(size);
	}

	public LongItemStackHandler (NonNullList<ItemStack> stacks) {
		this.stacks.clear();
		this.setSize(stacks.size());
		stacks.forEach(o -> this.stacks.add(new SuperItemStack(o, o.getCount())));
	}

	public void setSize (int size) {
		this.stacks.ensureCapacity(size);
		this.size = size;
	}

	@Override
	public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
		validateSlotIndex(slot);
		this.stacks.set(slot, new SuperItemStack(stack, stack.getCount()));
		onContentsChanged(slot);
	}

	@Override
	public int getSlots () {
		return stacks.size();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot (int slot) {
		validateSlotIndex(slot);
		return this.stacks.get(slot).getStack();
	}

	@Override
	@Nonnull
	public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		validateSlotIndex(slot);

		SuperItemStack existing = this.stacks.get(slot);

		long limit = Long.MAX_VALUE;

		if (!existing.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing.getStack())) {
				return stack;
			}

			limit -= existing.getCount();
		}

		if (limit <= 0) {
			return stack;
		}

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate) {
			if (existing.isEmpty()) {
				this.stacks.set(slot, new SuperItemStack(stack, stack.getCount());
			} else {
				existing.setCount(
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	@Nonnull
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}

		validateSlotIndex(slot);

		SuperItemStack existing = this.stacks.get(slot);

		if (existing.isEmpty()) {
			return ItemStack.EMPTY;
		}

		int toExtract = Math.min(amount, existing.getStack().getMaxStackSize());

		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				this.stacks.set(slot, SuperItemStack.EMPTY);
				onContentsChanged(slot);
			}
			return existing;
		} else {
			if (!simulate) {
				this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onContentsChanged(slot);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit (int slot) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
		return true;
	}

	@Override
	public NBTTagCompound serializeNBT () {
		NBTTagList nbtTagList = new NBTTagList();
		for (int i = 0; i < stacks.size(); i++) {
			if (!stacks.get(i).isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				stacks.get(i).writeToNBT(itemTag);
				nbtTagList.appendTag(itemTag);
			}
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Items", nbtTagList);
		nbt.setInteger("Size", stacks.size());
		return nbt;
	}

	@Override
	public void deserializeNBT (NBTTagCompound nbt) {
		setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size());
		NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
			int slot = itemTags.getInteger("Slot");

			if (slot >= 0 && slot < stacks.size()) {
				stacks.set(slot, SuperItemStack.fromNBT(itemTags));
			}
		}
		onLoad();
	}

	protected void validateSlotIndex (int slot) {
		if (slot < 0 || slot >= size) {
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
		}
	}

	protected void onLoad () {

	}

	protected void onContentsChanged (int slot) {

	}
}
