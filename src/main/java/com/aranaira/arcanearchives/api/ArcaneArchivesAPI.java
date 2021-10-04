package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.api.crafting.registry.IRecipeManagerAccessor;
import net.minecraft.item.crafting.RecipeManager;

public abstract class ArcaneArchivesAPI {
  public static final String MODID = "arcanearchives";

  public static ArcaneArchivesAPI INSTANCE;

  public abstract IRecipeManagerAccessor getRecipeAccessor ();

  public RecipeManager getRecipeManager () {
    return getRecipeAccessor().getManager();
  }
}
