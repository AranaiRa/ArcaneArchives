package com.aranaira.arcanearchives.core.recipes.inventory;

import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;

public class CrystalWorkbenchCrafting extends ArcaneCrafting<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> {
  public CrystalWorkbenchCrafting(CrystalWorkbenchContainer container, CrystalWorkbenchTile tile, CrystalWorkbenchInventory handler) {
    super(container, tile, handler);
  }
}
