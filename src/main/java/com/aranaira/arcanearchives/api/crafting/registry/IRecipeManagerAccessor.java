package com.aranaira.arcanearchives.api.crafting.registry;

import net.minecraft.item.crafting.RecipeManager;

import javax.annotation.Nullable;

public interface IRecipeManagerAccessor {
  @Nullable
  RecipeManager getManager();
}
