package com.aranaira.arcanearchives.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import java.io.IOException;

public class LargeItemNBTUtil {
	@Nonnull
	public static NBTTagCompound writeToNBT (ItemStack item) {
		return writeToNBT(new NBTTagCompound(), item);
	}

	@Nonnull
	public static NBTTagCompound writeToNBT (NBTTagCompound nbt, ItemStack item) {
		item.writeToNBT(nbt);
		nbt.setInteger("Count", item.getCount());

		return nbt;
	}

	public static ItemStack readFromNBT (NBTTagCompound compound) {
		ItemStack item = new ItemStack(compound);

		item.setCount(compound.getInteger("Count"));

		return item;
	}

	@Deprecated
	public static void writeToBuf (ByteBuf pbuf, ItemStack stack) {
		PacketBuffer buf = new PacketBuffer(pbuf);

		if (stack.isEmpty()) {
			buf.writeShort(-1);
		} else {
			buf.writeShort(Item.getIdFromItem(stack.getItem()));
			buf.writeShort(stack.getCount());
			buf.writeShort(stack.getMetadata());
			NBTTagCompound nbttagcompound = null;

			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbttagcompound = stack.getItem().getNBTShareTag(stack);
			}

			buf.writeCompoundTag(nbttagcompound);
		}
	}

	@Deprecated
	public static ItemStack readFromBuf (ByteBuf buf2) throws IOException {
		PacketBuffer buf = new PacketBuffer(buf2);

		int i = buf.readShort();

		if (i < 0) {
			return ItemStack.EMPTY;
		} else {
			int j = buf.readShort();
			int k = buf.readShort();
			ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
			itemstack.getItem().readNBTShareTag(itemstack, buf.readCompoundTag());
			return itemstack;
		}
	}
}
