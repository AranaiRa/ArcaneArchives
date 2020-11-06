package com.aranaira.arcanearchives.api.cwb;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public interface ICrystalWorkbenchRecipe extends IArcaneArchivesRecipe {
  int getIndex();

  void setIndex(int index);

  // TODO
  @Override
  default NonNullList<ItemStack> getRemainingIngredients(WorkbenchCrafting inventory, @Nullable PlayerEntity player) {
    NonNullList<ItemStack> ret = NonNullList.withSize(inventory.getInventory().getSlots(), ItemStack.EMPTY);
    for (int i = 0; i < ret.size(); i++) {
      ItemStack item = inventory.getInventory().getStackInSlot(i);
      for (IngredientTransformer transformer : getIngredientTransformers()) {
        ItemStack result = transformer.apply(inventory, player, item);
        if (result == null) {
          inventory.getInventory().setStackInSlot(i, ItemStack.EMPTY);
        } else {
          item = result;
        }
      }
      ret.set(i, item);
    }
    return ret;
  }

}
