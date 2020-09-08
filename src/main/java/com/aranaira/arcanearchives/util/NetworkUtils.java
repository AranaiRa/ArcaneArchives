package com.aranaira.arcanearchives.util;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.io.IOException;

public class NetworkUtils {
	public static void writeExtendedItemStack (PacketBuffer buf, ItemStack stack) {
		if (stack.isEmpty()) {
			buf.writeShort(-1);
		} else {
			buf.writeShort(Item.getIdFromItem(stack.getItem()));
			buf.writeInt(stack.getCount());
			buf.writeShort(stack.getMetadata());
			NBTTagCompound nbttagcompound = null;

			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbttagcompound = stack.getItem().getNBTShareTag(stack);
			}

			buf.writeCompoundTag(nbttagcompound);
		}
	}

	public static void writeExtendedItemStack (ByteBuf buf, ItemStack stack) {
		if (stack.isEmpty()) {
			buf.writeShort(-1);
		} else {
			buf.writeShort(Item.getIdFromItem(stack.getItem()));
			buf.writeInt(stack.getCount());
			buf.writeShort(stack.getMetadata());
			NBTTagCompound nbttagcompound = null;

			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbttagcompound = stack.getItem().getNBTShareTag(stack);
			}

			if (nbttagcompound != null) {
				ByteBufUtils.writeTag(buf, nbttagcompound);
			}
		}
	}

	public static void writeExtendedItemStackFromClientToServer (ByteBuf buf, ItemStack stack) {
		if (stack.isEmpty()) {
			buf.writeShort(-1);
		} else {
			buf.writeShort(Item.getIdFromItem(stack.getItem()));
			buf.writeInt(stack.getCount());
			buf.writeShort(stack.getMetadata());
			NBTTagCompound nbttagcompound = null;

			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbttagcompound = stack.getTagCompound();
			}

			if (nbttagcompound != null) {
				ByteBufUtils.writeTag(buf, nbttagcompound);
			}
		}
	}

	public static void writeExtendedItemStackFromClientToServer (PacketBuffer buf, ItemStack stack) {
		if (stack.isEmpty()) {
			buf.writeShort(-1);
		} else {
			buf.writeShort(Item.getIdFromItem(stack.getItem()));
			buf.writeInt(stack.getCount());
			buf.writeShort(stack.getMetadata());
			NBTTagCompound nbttagcompound = null;

			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				nbttagcompound = stack.getTagCompound();
			}

			buf.writeCompoundTag(nbttagcompound);
		}
	}

	public static ItemStack readExtendedItemStack (ByteBuf buf) throws IOException {
		int i = buf.readShort();

		if (i < 0) {
			return ItemStack.EMPTY;
		} else {
			int j = buf.readInt();
			int k = buf.readShort();
			ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
			NBTTagCompound tag = ByteBufUtils.readTag(buf);
			if (tag != null) {
				itemstack.setTagCompound(tag);
			}
			return itemstack;
		}
	}

	public static ItemStack readExtendedItemStack (PacketBuffer buf) throws IOException {
		int i = buf.readShort();

		if (i < 0) {
			return ItemStack.EMPTY;
		} else {
			int j = buf.readInt();
			int k = buf.readShort();
			ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
			itemstack.setTagCompound(buf.readCompoundTag());
			return itemstack;
		}
	}

}
