package com.aranaira.arcanearchives.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IngredientTransformer {

  @Nullable
  ItemStack apply(WorkbenchCrafting inventory, @Nullable EntityPlayer player, ItemStack item);

  class DefaultForgeContainer implements IngredientTransformer {
    @Nullable
    @Override
    public ItemStack apply(WorkbenchCrafting inventory, @Nullable EntityPlayer player, ItemStack item) {
      return ForgeHooks.getContainerItem(item);
    }
  }
}
