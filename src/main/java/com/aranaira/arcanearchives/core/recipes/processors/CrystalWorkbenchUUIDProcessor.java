package com.aranaira.arcanearchives.core.recipes.processors;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import java.util.List;

public class CrystalWorkbenchUUIDProcessor extends CrystalWorkbenchProcessor {
  public CrystalWorkbenchUUIDProcessor() {
    super();
  }

  @Override
  public ItemStack processOutput(ItemStack result, List<IngredientStack> ingredients, List<ItemStack> incoming, CrystalWorkbenchCrafting crafter) {
    CompoundNBT tag = result.getOrCreateTag();
    if (!tag.hasUUID(Identifiers.networkId)) {
      if (crafter.getTileId() == null) {
        throw new IllegalArgumentException("tile id for crafter tile is null: " + crafter.getBlockEntity());
      }
      tag.putUUID(Identifiers.networkId, crafter.getTileId());
    }
    return null;
  }
}
