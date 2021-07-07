package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.processors.IngredientProcessor;
import com.aranaira.arcanearchives.api.crafting.processors.registry.ProcessorRegistries;
import com.aranaira.arcanearchives.api.crafting.processors.registry.ProcessorRegistry;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.recipes.processors.ContainerProcessor;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Registries {
  public static class Processor {
    public static final ResourceLocation WORKBENCH_ID = new ResourceLocation(ArcaneArchives.MODID, "workbench");
    public static final ProcessorRegistry<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> WORKBENCH = new ProcessorRegistry<>(WORKBENCH_ID);

    public static void initDefaultProcessors() {
      ContainerProcessor containerProcessor = new ContainerProcessor(new ResourceLocation(ArcaneArchives.MODID, "container_processor"));
      WORKBENCH.register(containerProcessor);
    }

    @Nullable
    public static ProcessorRegistry<?, ?, ?> getProcessorRegistry(@Nonnull ResourceLocation rl) {
      if (Processor.WORKBENCH_ID.equals(rl)) {
        return Processor.WORKBENCH;
      }

      return null;
    }

    @Nullable
    public static IngredientProcessor<?, ?, ?> getIngredientProcessor(@Nonnull ResourceLocation registry, @Nonnull ResourceLocation id) {
      ProcessorRegistry<?, ?, ?> reg = getProcessorRegistry(registry);
      if (reg == null) {
        return null;
      }

      return reg.getValue(id);
    }

    @Nullable
    public static IngredientProcessor<?, ?, ?> deserializeIngredientProcessor(@Nonnull JsonObject object) {
      JsonElement reg = object.get("registry");
      ResourceLocation regRl, idRl;
      if (!reg.isJsonNull()) {
        regRl = new ResourceLocation(reg.getAsString());
      } else {
        return null;
      }
      JsonElement id = object.get("name");
      if (!id.isJsonNull()) {
        idRl = new ResourceLocation(id.getAsString());
      } else {
        return null;
      }

      return getIngredientProcessor(regRl, idRl);
    }
  }

  public static void initRegistries() {
    ProcessorRegistries.register(Processor.WORKBENCH);
  }

  public static void initDefaults() {
    Processor.initDefaultProcessors();
  }
}
