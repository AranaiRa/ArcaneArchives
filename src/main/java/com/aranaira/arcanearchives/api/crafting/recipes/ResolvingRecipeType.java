package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class ResolvingRecipeType<C extends IInventory, T extends IRecipe<C>> extends noobanidus.libs.noobutil.recipe.ResolvingRecipeType<C, T> {

  public ResolvingRecipeType(Supplier<IRecipeType<T>> type, Comparator<? super T> comparator) {
    super(type, comparator);
  }

  @Override
  protected List<T> getRecipesList() {
    return ArcaneArchivesAPI.getInstance().getRecipeManager().getAllRecipesFor(type.get());
  }
}
