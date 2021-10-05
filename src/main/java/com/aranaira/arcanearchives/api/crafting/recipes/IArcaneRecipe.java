package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.container.IPartitionedPlayerContainer;
import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.inventory.ArcaneItemHandler;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.Map;

public interface IArcaneRecipe<H extends ArcaneItemHandler, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IArcaneArchivesTile, W extends ArcaneCrafting<H, C, T>> extends IRecipe<W> {
  List<Processor<W>> getProcessors ();

  void addProcessor (Processor<W> processor);

  @Override
  default boolean isSpecial() {
    return true;
  }
}
