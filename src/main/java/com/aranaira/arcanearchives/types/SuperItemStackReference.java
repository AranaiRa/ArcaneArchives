package com.aranaira.arcanearchives.types;

import hellfirepvp.astralsorcery.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SuperItemStackReference {
	private ItemStack stack;
	private long count;
	private int slot;
	private int device;

	public SuperItemStackReference (ItemStack stack, long count, int slot, int device) {
		this.stack = stack;
		this.count = count;
		this.slot = slot;
		this.device = device;
	}

	public ItemStack getStack () {
		return stack;
	}

	public long getCount () {
		return count;
	}

	public void setCount (long count) {
		this.count = count;
	}

	public int getSlot () {
		return slot;
	}

	public void setSlot (int slot) {
		this.slot = slot;
	}

	public int getDevice () {
		return device;
	}

	public void setDevice (int device) {
		this.device = device;
	}

	public NBTTagCompound writeToNBT () {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("i", RecipeItemHelper.pack(stack));
		tag.setLong("c", count);
		tag.setInteger("d", device);
		tag.setInteger("s", slot);
		if (stack.hasTagCompound()) {
			NBTTagCompound stackTag = stack.getTagCompound();
			if (stackTag != null && !stackTag.isEmpty()) {
				tag.setTag("t", stackTag);
			}
		}
		NBTTagCompound stackStack = stack.writeToNBT(new NBTTagCompound());
		if (stackStack.hasKey("ForgeCaps")) {
			tag.setTag("f", stackStack.getTag("ForgeCaps"));
		}

		return tag;
	}

	public void readFromNBT (NBTTagCompound tag) {
		ItemStack temp = RecipeItemHelper.unpack(tag.getInteger("i"));
		temp.setCount(1);
		NBTTagCompound incoming = temp.writeToNBT(new NBTTagCompound());
		if (tag.hasKey("f")) {
			incoming.setTag("ForgeCaps", tag.getTag("f"));
		}
		if (tag.hasKey("t")) {
			incoming.setTag("tag", tag.getTag("t"));
		}

		this.stack = new ItemStack(incoming);
		this.count = tag.getLong("c");
		this.slot = tag.getInteger("s");
		this.device = tag.getInteger("d");
	}

	public static SuperItemStackReference fromNBT (NBTTagCompound tag) {
		SuperItemStackReference result = new SuperItemStackReference(ItemStack.EMPTY, 0, 0, 0);
		result.readFromNBT(tag);
		return result;
	}

	public void writeToBuf (ByteBuf buf) {
		buf.writeInt(RecipeItemHelper.pack(this.stack));
		buf.writeLong(this.count);
		buf.writeInt(this.slot);
		buf.writeInt(this.device);
		NBTTagCompound tag = this.stack.getTagCompound();
		if (stack.hasTagCompound() && tag != null) {
			buf.writeBoolean(true);
			ByteBufUtils.writeNBTTag(buf, tag);
		} else {
			buf.writeBoolean(false);
		}
		NBTTagCompound stackStack = stack.writeToNBT(new NBTTagCompound());
		if (stackStack.hasKey("ForgeCaps")) {
			buf.writeBoolean(true);
			ByteBufUtils.writeNBTTag(buf, stackStack.getCompoundTag("ForgeCaps"));
		} else {
			buf.writeBoolean(false);
		}
	}

	public void readFromBuf (ByteBuf buf) {
		ItemStack temp = RecipeItemHelper.unpack(buf.readInt());
		temp.setCount(1);
		this.count = buf.readLong();
		this.slot = buf.readInt();
		this.device = buf.readInt();
		NBTTagCompound tag = null;
		NBTTagCompound caps = null;
		if (buf.readBoolean()) {
			tag = ByteBufUtils.readNBTTag(buf);
		}
		if (buf.readBoolean()) {
			caps = ByteBufUtils.readNBTTag(buf);
		}

		if (caps != null && tag != null) {
			NBTTagCompound stackStack = temp.writeToNBT(new NBTTagCompound());
			stackStack.setTag("tag", tag);
			stackStack.setTag("ForgeCaps", caps);
			this.stack = new ItemStack(stackStack);
		} else if (tag != null) {
			temp.setTagCompound(tag);
			this.stack = temp;
		}
	}

	public static SuperItemStackReference fromBuf (ByteBuf buf) {
		SuperItemStackReference result = new SuperItemStackReference(ItemStack.EMPTY, 0, 0, 0);
		result.readFromBuf(buf);
		return result;
	}
}
