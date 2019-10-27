package com.aranaira.arcanearchives.types;

import hellfirepvp.astralsorcery.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SuperItemStack {
	public static final SuperItemStack EMPTY = new SuperItemStack(ItemStack.EMPTY, 0);

	private ItemStack stack;
	private long count;

	public SuperItemStack (ItemStack stack, long count) {
		this.stack = stack;
		this.count = count;
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

	public boolean isEmpty () {
		return count == 0 && stack.isEmpty();
	}

	public NBTTagCompound writeToNBT (NBTTagCompound tag) {
		tag.setInteger("i", RecipeItemHelper.pack(stack));
		tag.setLong("c", count);
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
	}

	public static SuperItemStack fromNBT (NBTTagCompound tag) {
		SuperItemStack result = new SuperItemStack(ItemStack.EMPTY, 0);
		result.readFromNBT(tag);
		return result;
	}

	public void writeToBuf (ByteBuf buf) {
		buf.writeInt(RecipeItemHelper.pack(this.stack));
		buf.writeLong(this.count);
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

	public static SuperItemStack fromBuf (ByteBuf buf) {
		SuperItemStack result = new SuperItemStack(ItemStack.EMPTY, 0);
		result.readFromBuf(buf);
		return result;
	}
}
