package com.aranaira.arcanearchives.inventory.handlers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class ExtendedItemStackHandler extends ItemStackHandler {

	public ExtendedItemStackHandler() {
		this(1);
	}

	public ExtendedItemStackHandler(int size) {
		super(size);
	}

	public ExtendedItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	@Override
    public int getSlotLimit(int slot) {
        return 64 * 8;
    }
	
	@Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack)  {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize() * 8);
    }
	
	@Override
	public void onContentsChanged(int slot) {

    }
	
	@Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
            	short realCount = (short) Math.min(Short.MAX_VALUE, stacks.get(i).getCount());
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks.get(i).writeToNBT(itemTag);
                itemTag.setShort("ExtendedCount", realCount);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.size());
        return nbt;
    }
	
	@Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size());
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < stacks.size()) {
            	if (itemTags.hasKey("StackList", Constants.NBT.TAG_LIST)) { // migrate from old ExtendedItemStack system
        			ItemStack stack = ItemStack.EMPTY;
            		NBTTagList stackTagList = itemTags.getTagList("StackList", Constants.NBT.TAG_COMPOUND);
        			for (int j = 0; j < stackTagList.tagCount(); j++) {
        				NBTTagCompound itemTag = stackTagList.getCompoundTagAt(j);
        				ItemStack temp = new ItemStack(itemTag);
        				if (!temp.isEmpty()) {
        					if (stack.isEmpty()) stack = temp;
        					else stack.grow(temp.getCount());
        				}
        			}
        			if (!stack.isEmpty()) {
        				int count = stack.getCount();
            			count = Math.min(count, getStackLimit(slot, stack));
            			stack.setCount(count);
            			
            			stacks.set(slot, stack);
        			}
        		} else {
        			ItemStack stack = new ItemStack(itemTags);
        			if (itemTags.hasKey("ExtendedCount", Constants.NBT.TAG_SHORT)) {
        				stack.setCount(itemTags.getShort("ExtendedCount"));
        			}
        			stacks.set(slot, stack);
        		}
            }
        }
        onLoad();
    }
	
	public int calcRedstone() {
	    int numStacks = 0;
	    float f = 0F;
	
	    for (int slot = 0; slot < this.getSlots(); slot++) {
	        ItemStack stack = this.getStackInSlot(slot);
	
	        if (!stack.isEmpty()) {
	            f += (float) stack.getCount() / (float) this.getStackLimit(slot, stack);
	            numStacks++;
	        }
	    }
	
	    f /= this.getSlots();
	    return MathHelper.floor(f * 14F) + (numStacks > 0 ? 1 : 0);
    }

}
