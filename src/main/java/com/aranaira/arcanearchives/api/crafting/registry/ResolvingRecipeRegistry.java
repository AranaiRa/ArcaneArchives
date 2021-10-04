package com.aranaira.arcanearchives.api.crafting.registry;

import com.aranaira.arcanearchives.api.crafting.ArcaneCrafting;
import com.aranaira.arcanearchives.api.crafting.recipes.IArcaneRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.function.BiPredicate;

public class ResolvingRecipeRegistry<W extends ArcaneCrafting<?, ?, ?>, R extends IArcaneRecipe<?, ?, ?, W>> {
  private final ResourceLocation id;
  private final IRecipeType<R> type;
  private final Class<R> recipeClass;
  private final BiPredicate<W, World> matcher;

  public ResolvingRecipeRegistry (IRecipeType<R> type, Class<R> recipeClass, BiPredicate<W, World> matcher) {
    this.id = new ResourceLocation(type.toString());
    this.type = type;
    this.recipeClass = recipeClass;
    this.matcher = matcher;
  }



}
