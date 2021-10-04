package com.aranaira.arcanearchives.client.impl;

import com.aranaira.arcanearchives.api.crafting.registry.IRecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.item.crafting.RecipeManager;

import javax.annotation.Nullable;

public class ClientRecipeAccessor implements IRecipeManagerAccessor {
  @Nullable
  @Override
  public RecipeManager getManager() {
    ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return null;
    }

    return connection.getRecipeManager();
  }
}
