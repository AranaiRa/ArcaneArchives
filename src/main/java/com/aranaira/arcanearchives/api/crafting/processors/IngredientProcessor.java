package com.aranaira.arcanearchives.api.crafting.processors;

import com.aranaira.arcanearchives.api.IResourceLocation;
import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import com.google.gson.JsonObject;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class IngredientProcessor<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> implements IIngredientProcessor<H, C, T>, IResourceLocation {
  protected ResourceLocation resourceLocation;
  protected ResourceLocation registryLocation;

  public IngredientProcessor(ResourceLocation registry, ResourceLocation resourceLocation) {
    this.setResourceLocation(resourceLocation);
    this.setRegistryLocation(registry);
  }

  @Nullable
  @Override
  public ResourceLocation getResourceLocation() {
    return resourceLocation;
  }

  @Override
  public void setResourceLocation(@Nonnull ResourceLocation rl) {
    this.resourceLocation = rl;
  }

  @Nullable
  public ResourceLocation getRegistryLocation() {
    return registryLocation;
  }

  public void setRegistryLocation(ResourceLocation registryLocation) {
    this.registryLocation = registryLocation;
  }

  @Override
  public JsonObject serialize () {
    JsonObject object = new JsonObject();
    object.addProperty("registry", getRegistryLocation().toString());
    object.addProperty("name", getResourceLocation().toString());
    return object;
  }
}
