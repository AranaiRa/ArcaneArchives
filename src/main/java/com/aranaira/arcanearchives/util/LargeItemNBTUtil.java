package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LargeItemNBTUtil
{
	public static NBTTagCompound writeToNBT(NBTTagCompound nbt, ItemStack item)
	{
		item.writeToNBT(nbt);
		nbt.setShort("Count", (short) item.getCount());

		return nbt;
	}

	public static ItemStack readFromNBT(NBTTagCompound compound)
	{
		ItemStack item = new ItemStack(compound);

		item.setCount(compound.getShort("Count"));

		return item;
	}
}
