package com.aranaira.arcanearchives.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.io.IOException;

public class NetworkUtils {

  public static void writeNBT(ByteBuf buf, @Nullable CompoundNBT nbt) {
    if (nbt == null) {
      buf.writeByte(0);
    } else {
      try {
        CompressedStreamTools.write(nbt, new ByteBufOutputStream(buf));
      } catch (IOException ioexception) {
        throw new EncoderException(ioexception);
      }
    }
  }

  public static CompoundNBT readNBT(ByteBuf buf) {
    int i = buf.readerIndex();
    byte b0 = buf.readByte();

    if (b0 == 0) {
      return null;
    } else {
      buf.readerIndex(i);
      try {
        return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
      } catch (IOException ioexception) {
        throw new EncoderException(ioexception);
      }
    }
  }

  public static void writeExtendedItemStack(PacketBuffer buf, ItemStack stack) {
    if (stack.isEmpty()) {
      buf.writeShort(-1);
    } else {
      buf.writeShort(Item.getIdFromItem(stack.getItem()));
      buf.writeInt(stack.getCount());
      buf.writeShort(stack.getMetadata());
      CompoundNBT nbttagcompound = null;

      if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
        nbttagcompound = stack.getItem().getNBTShareTag(stack);
      }

      buf.writeCompoundTag(nbttagcompound);
    }
  }

  public static void writeExtendedItemStack(ByteBuf buf, ItemStack stack) {
    if (stack.isEmpty()) {
      buf.writeShort(-1);
    } else {
      buf.writeShort(Item.getIdFromItem(stack.getItem()));
      buf.writeInt(stack.getCount());
      buf.writeShort(stack.getMetadata());
      CompoundNBT nbttagcompound = null;

      if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
        nbttagcompound = stack.getItem().getNBTShareTag(stack);
      }

      writeNBT(buf, nbttagcompound);
    }
  }

  public static void writeExtendedItemStackFromClientToServer(ByteBuf buf, ItemStack stack) {
    if (stack.isEmpty()) {
      buf.writeShort(-1);
    } else {
      buf.writeShort(Item.getIdFromItem(stack.getItem()));
      buf.writeInt(stack.getCount());
      buf.writeShort(stack.getMetadata());
      CompoundNBT nbttagcompound = null;

      if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
        nbttagcompound = stack.getTagCompound();
      }

      writeNBT(buf, nbttagcompound);
    }
  }

  public static void writeExtendedItemStackFromClientToServer(PacketBuffer buf, ItemStack stack) {
    if (stack.isEmpty()) {
      buf.writeShort(-1);
    } else {
      buf.writeShort(Item.getIdFromItem(stack.getItem()));
      buf.writeInt(stack.getCount());
      buf.writeShort(stack.getMetadata());
      CompoundNBT nbttagcompound = null;

      if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
        nbttagcompound = stack.getTagCompound();
      }

      buf.writeCompoundTag(nbttagcompound);
    }
  }

  public static ItemStack readExtendedItemStack(ByteBuf buf) throws IOException {
    int i = buf.readShort();

    if (i < 0) {
      return ItemStack.EMPTY;
    } else {
      int j = buf.readInt();
      int k = buf.readShort();
      ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
      itemstack.setTagCompound(readNBT(buf));
      return itemstack;
    }
  }

  public static ItemStack readExtendedItemStack(PacketBuffer buf) throws IOException {
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
