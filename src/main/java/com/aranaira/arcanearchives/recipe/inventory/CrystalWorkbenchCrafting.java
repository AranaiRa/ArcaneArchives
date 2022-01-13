package com.aranaira.arcanearchives.recipe.inventory;

import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.recipe.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.block.entity.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.util.ArcaneRecipeUtil;
import net.minecraft.item.ItemStack;
import noobanidus.libs.noobutil.crafting.Crafting;

import java.util.List;

public class CrystalWorkbenchCrafting extends Crafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchBlockEntity> {
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
