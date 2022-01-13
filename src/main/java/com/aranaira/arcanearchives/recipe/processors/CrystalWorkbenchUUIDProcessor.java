package com.aranaira.arcanearchives.recipe.processors;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.recipe.inventory.CrystalWorkbenchCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import javax.annotation.Nullable;
import java.util.List;

public class CrystalWorkbenchUUIDProcessor extends CrystalWorkbenchProcessor {
  public CrystalWorkbenchUUIDProcessor() {
    super();
  }

  @Override
  @Nullable
  public ItemStack processOutput(ItemStack result, List<IngredientStack> ingredients, List<ItemStack> incoming, CrystalWorkbenchCrafting crafter) {
    if (crafter.getBlockEntity().getEntityId() == null) {
      throw new IllegalArgumentException("tile id for crafter tile is null: " + crafter.getBlockEntity());
    }

    CompoundNBT tag = new CompoundNBT();
    tag.putUUID(Identifiers.networkId, crafter.getBlockEntity().getEntityId());
    result.addTagElement(Identifiers.BlockEntityTag, tag);
    return null;
  }
}
