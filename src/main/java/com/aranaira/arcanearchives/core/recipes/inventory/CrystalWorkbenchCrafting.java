package com.aranaira.arcanearchives.core.recipes.inventory;

import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.core.util.ArcaneRecipeUtil;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.Map;

public class CrystalWorkbenchCrafting extends ArcaneCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> {
  public CrystalWorkbenchCrafting(CrystalWorkbenchContainer container, CrystalWorkbenchTile tile, CrystalWorkbenchInventory handler) {
    super(container, tile, handler);
  }

  public List<IngredientInfo> getIngredientInfo(CrystalWorkbenchRecipe recipe) {
    return ArcaneRecipeUtil.getIngredientInfo(recipe, this);
  }
}
