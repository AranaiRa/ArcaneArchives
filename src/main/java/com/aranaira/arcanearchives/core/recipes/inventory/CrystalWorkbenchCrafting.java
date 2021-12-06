package com.aranaira.arcanearchives.core.recipes.inventory;

import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.util.ArcaneRecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CrystalWorkbenchCrafting extends ArcaneCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchBlockEntity> {
  public CrystalWorkbenchCrafting(CrystalWorkbenchContainer container, CrystalWorkbenchBlockEntity tile, CrystalWorkbenchInventory handler) {
    super(container, tile, handler);
  }

  public List<IngredientInfo> getIngredientInfo(CrystalWorkbenchRecipe recipe) {
    return ArcaneRecipeUtil.getIngredientInfo(recipe, this);
  }

  @Override
  public ItemStack removeItem(int index, int count) {
    return super.removeItem(index, count);
  }

  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return super.removeItemNoUpdate(index);
  }

  @Override
  public void setItem(int index, ItemStack stack) {
    super.setItem(index, stack);
  }
}
