package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.util.types.SlotIterable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface LargeSlotSerialization
{
	default NBTTagCompound largeSerializeHandler(IItemHandlerModifiable largeHandler)
	{
		NBTTagCompound ret = new NBTTagCompound();
		ret.setInteger("count", largeHandler.getSlots());
		NBTTagList res = new NBTTagList();
		SlotIterable iter = new SlotIterable(largeHandler);
		for (ItemStack stack : iter) {
			if (stack.isEmpty()) continue;
			NBTTagCompound item = new NBTTagCompound();
			LargeItemNBTUtil.writeToNBT(item, stack);
			item.setInteger("slot", iter.getSlot());
			res.appendTag(item);
		}

		ret.setTag("inventory", res);

		return ret;
	}

	default void largeDeserializeHandler(NBTTagCompound tag, IItemHandlerModifiable largeHandler)
	{
		int count = tag.getInteger("count");
		for (int i = 0; i < count; i++) {
			largeHandler.setStackInSlot(i, ItemStack.EMPTY);
		}

		NBTTagList tags = tag.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
		for (NBTBase base : tags) {
			if (!(base instanceof NBTTagCompound)) continue;

			NBTTagCompound comp = (NBTTagCompound) base;

			int slot = comp.getInteger("slot");
			ItemStack stack = LargeItemNBTUtil.readFromNBT(comp);

			largeHandler.setStackInSlot(slot, stack);
		}
	}
}
