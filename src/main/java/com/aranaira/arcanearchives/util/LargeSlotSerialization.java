package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.util.types.SlotIterable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandler;

public interface LargeSlotSerialization
{
	default NBTTagList largeSerializeHandler(IItemHandler largeHandler)
	{
		NBTTagList res = new NBTTagList();
		SlotIterable iter = new SlotIterable(largeHandler);
		for (ItemStack stack : iter) {
			if (stack.isEmpty()) continue;
			NBTTagCompound item = new NBTTagCompound();
			LargeItemNBTUtil.writeToNBT(item, stack);
			item.setInteger("slot", iter.getSlot());
			res.appendTag(item);
		}

		return res;
	}

	default void largeDeserializeHandler(NBTTagList tag, IItemHandler largeHandler)
	{
		for (NBTBase base : tag) {
			if (!(base instanceof NBTTagCompound)) continue;

			NBTTagCompound comp = (NBTTagCompound) base;

			int slot = comp.getInteger("slot");
			ItemStack stack = LargeItemNBTUtil.readFromNBT(comp);

			largeHandler.insertItem(slot, stack, false);
		}
	}
}
