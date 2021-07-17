package com.aranaira.arcanearchives.api.crafting.processors;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ICrafter;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public interface IIngredientProcessor<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> extends IOutputProcessor<H, C, T> {
  @Override
  default ItemStack process(ItemStack result, List<Ingredient> ingredients, List<ItemStack> incoming, List<List<ItemStack>> outgoing, ICrafter<H, C, T> crafter) {
    if (ingredients.size() != incoming.size()) {
      throw new IllegalArgumentException("size of ingredients must match incoming itemstacks: " + ingredients.size() + " ingredients versus " + incoming.size() + " items");
    }

    outgoing = new ArrayList<>();

    for (int i = 0; i < ingredients.size(); i++) {
      List<ItemStack> thisOutgoing = new ArrayList<>();
      process(result, ingredients.get(i), incoming.get(i), thisOutgoing, crafter);
      outgoing.add(thisOutgoing);
    }

    return result;
  }

  ItemStack process (ItemStack result, Ingredient ingredient, ItemStack stack, List<ItemStack> processed, ICrafter<H, C, T> crafter);
}
