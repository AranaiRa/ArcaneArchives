package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.data.storage.ClientDataStorage;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class NetworkItemTemplate extends ItemTemplate {
  // TODO: Maybe invalidate?
  @Nullable
  public static UUID getNetworkId (ItemStack stack) {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(stack);
    if (tag.hasUniqueId(Tags.networkId)) {
      return tag.getUniqueId(Tags.networkId);
    }

    return null;
  }

  public static boolean setNetworkId (ItemStack stack, UUID uuid) {
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(stack);
    if (tag.hasUniqueId(Tags.networkId)) {
      return false;
    }

    tag.setUniqueId(Tags.networkId, uuid);
    return true;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    UUID network = getNetworkId(stack);
    if (network == null) {
      return;
    }

    tooltip.add("");
    tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.network.name.network", ClientDataStorage.getStringFor(network)));
  }
}
