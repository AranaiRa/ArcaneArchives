package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.UUID;

public class NetworkItemUtil {
  @Nullable
  public static UUID getNetworkId(ItemStack stack) {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(stack);
    if (tag.hasUniqueId(Tags.networkId)) {
      return tag.getUniqueId(Tags.networkId);
    }

    return null;
  }

  public static boolean setNetworkId(ItemStack stack, UUID uuid) {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(stack);
    if (tag.hasUniqueId(Tags.networkId)) {
      return false;
    }

    tag.setUniqueId(Tags.networkId, uuid);
    return true;
  }
}
