package com.aranaira.arcanearchives.api.inventory;

import com.aranaira.arcanearchives.api.crafting.recipes.IArcaneRecipe;
import net.minecraftforge.items.IItemHandlerModifiable;

@FunctionalInterface
public interface IInventoryListener {
  void inventoryChanged (IArcaneInventory inventory, int slot);
}
