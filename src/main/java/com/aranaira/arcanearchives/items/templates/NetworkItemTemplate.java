package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.UUID;

public class NetworkItemTemplate extends ItemTemplate {
  // TODO: Maybe invalidate?
  @Nullable
  public UUID getNetworkId (ItemStack stack) {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(stack);
    if (tag.hasUniqueId(Tags.networkId)) {
      return tag.getUniqueId(Tags.networkId);
    }

    return null;
  }

  public boolean setNetworkId (ItemStack stack, UUID uuid) {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(stack);
    if (tag.hasUniqueId(Tags.networkId)) {
      return false;
    }

    tag.setUniqueId(Tags.networkId, uuid);
    return true;
  }
}
