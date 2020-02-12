package com.aranaira.arcanearchives.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class ByteUtils {
  public static void writeUUID(ByteBuf buf, UUID uuid) {
    buf.writeLong(uuid.getMostSignificantBits());
    buf.writeLong(uuid.getLeastSignificantBits());
  }

  public static UUID readUUID(ByteBuf buf) {
    long most = buf.readLong();
    long least = buf.readLong();
    UUID result = new UUID(most, least);
    return result;
  }

  public static void writeOptionalUTF8(ByteBuf buf, String string) {
    if (string.isEmpty()) {
      buf.writeBoolean(false);
    } else {
      buf.writeBoolean(true);
      ByteBufUtils.writeUTF8String(buf, string);
    }
  }

  public static String readOptionalUTF8(ByteBuf buf) {
    boolean exists = buf.readBoolean();
    if (exists) {
      return ByteBufUtils.readUTF8String(buf);
    } else {
      return "";
    }
  }

  public static void writeIRecipe(ByteBuf buf, IRecipe recipe) {
    buf.writeInt(CraftingManager.REGISTRY.getIDForObject(recipe));
  }

  public static IRecipe readIRecipe(ByteBuf buf) {
    return CraftingManager.REGISTRY.getObjectById(buf.readInt());
  }
}
