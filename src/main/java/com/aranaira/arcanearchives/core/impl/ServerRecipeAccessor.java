package com.aranaira.arcanearchives.core.impl;

import com.aranaira.arcanearchives.api.crafting.registry.IRecipeManagerAccessor;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ServerRecipeAccessor implements IRecipeManagerAccessor {
  @Override
  public RecipeManager getManager() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server == null) {
      return null;
    }

    return server.getRecipeManager();
  }
}
