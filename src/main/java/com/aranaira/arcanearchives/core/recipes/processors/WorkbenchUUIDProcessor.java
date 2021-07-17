package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.crafting.ICrafter;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.core.init.Registries;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class WorkbenchUUIDProcessor extends WorkbenchOutputProcessor {
  public WorkbenchUUIDProcessor(ResourceLocation resourceLocation) {
    super(Registries.Processor.Output.WORKBENCH_ID, resourceLocation);
  }

  @Override
  public ItemStack process(ItemStack result, List<Ingredient> ingredients, List<ItemStack> incoming, List<List<ItemStack>> outgoing, ICrafter<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> crafter) {
    CompoundNBT tag = result.getOrCreateTag();
    if (!tag.hasUniqueId(Identifiers.networkId)) {
      if (crafter.getTileId() == null) {
        throw new IllegalArgumentException("tile id for crafter tile is null: " + crafter.getTile());
      }
      tag.putUniqueId(Identifiers.networkId, crafter.getTileId());
    }
    return null;
  }
}
