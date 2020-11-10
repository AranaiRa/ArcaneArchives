package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class NBTUtils {
  public static void setRecipe(CompoundNBT tag, String key, IRecipe recipe) {
    if (recipe != null) {
      tag.setInteger(key, CraftingManager.REGISTRY.getIDForObject(recipe));
    }
  }

  @Nullable
  public static IRecipe getRecipe(CompoundNBT tag, String key) {
    if (!tag.hasKey(key)) {
      return null;
    }

    return CraftingManager.REGISTRY.getObjectById(tag.getInteger(key));
  }

  public static int defaultInt(ItemStack stack, String key, int defaultInt) {
    if (!stack.hasTagCompound()) {
      return defaultInt;
    }

    CompoundNBT tag = stack.getTag();
    if (!tag.hasKey(key)) {
      return defaultInt;
    }

    return defaultInt(tag, key, defaultInt);
  }

  public static int defaultInt(CompoundNBT tag, String key, int defaultInt) {
    if (!tag.hasKey(key)) {
      return defaultInt;
    }

    return tag.getInteger(key);
  }
}
