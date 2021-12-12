package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.api.crafting.registry.IRecipeManagerAccessor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;

import java.util.Collections;
import java.util.List;

public abstract class ArcaneArchivesAPI {
  public static final String MODID = "arcanearchives";
  public static final String MOD_IDENTIFIER = "ArcaneArchives";

  public static ArcaneArchivesAPI INSTANCE;

  public static ArcaneArchivesAPI getInstance () {
    return INSTANCE;
  }

  public static boolean isPresent () {
    return INSTANCE != null;
  }

  public abstract IRecipeManagerAccessor getRecipeAccessor ();

  public RecipeManager getRecipeManager () {
    return getRecipeAccessor().getManager();
  }
}
