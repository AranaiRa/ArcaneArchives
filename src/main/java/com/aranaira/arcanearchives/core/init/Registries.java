package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.processors.registry.ProcessorRegistries;
import com.aranaira.arcanearchives.api.crafting.processors.registry.ProcessorRegistry;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.recipes.processors.WorkbenchContainerProcessor;
import com.aranaira.arcanearchives.core.recipes.processors.WorkbenchOutputProcessor;
import com.aranaira.arcanearchives.core.recipes.processors.WorkbenchUUIDProcessor;
import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Registries {
  public static class Processor {
    public static class Input {

      public static final ResourceLocation WORKBENCH_ID = new ResourceLocation(ArcaneArchives.MODID, "workbench_input");
      public static final ProcessorRegistry<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> WORKBENCH = new ProcessorRegistry<>(WORKBENCH_ID);

      public static void initDefaultProcessors() {
        WorkbenchContainerProcessor containerProcessor = new WorkbenchContainerProcessor(new ResourceLocation(ArcaneArchives.MODID, "container_processor"));
        WORKBENCH.register(containerProcessor);
      }
    }

    public static class Output {
      public static final ResourceLocation WORKBENCH_ID = new ResourceLocation(ArcaneArchives.MODID, "workbench_output");
      public static final ProcessorRegistry<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchTile> WORKBENCH = new ProcessorRegistry<>(WORKBENCH_ID);

      public static void initDefaultProcessors () {
        WorkbenchOutputProcessor outputProcessor = new WorkbenchUUIDProcessor(new ResourceLocation(ArcaneArchives.MODID, "uuid_processor"));
        WORKBENCH.register(outputProcessor);
      }
    }

    public static void initDefaultProcessors() {
      Input.initDefaultProcessors();
      Output.initDefaultProcessors();
    }

    @Nullable
    public static ProcessorRegistry<?, ?, ?> getProcessorRegistry(@Nonnull ResourceLocation rl) {
      if (Registries.Processor.Input.WORKBENCH_ID.equals(rl)) {
        return Registries.Processor.Input.WORKBENCH;
      } else if (Registries.Processor.Output.WORKBENCH_ID.equals(rl)) {
        return Registries.Processor.Output.WORKBENCH;
      }

      return null;
    }

    @Nullable
    public static com.aranaira.arcanearchives.api.crafting.processors.Processor<?, ?, ?> getProcessor(@Nonnull ResourceLocation registry, @Nonnull ResourceLocation id) {
      ProcessorRegistry<?, ?, ?> reg = getProcessorRegistry(registry);
      if (reg == null) {
        return null;
      }

      return reg.getValue(id);
    }

    @Nullable
    public static com.aranaira.arcanearchives.api.crafting.processors.Processor<?, ?, ?> deserializeProcessor(@Nonnull JsonObject object) {
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

      return getProcessor(regRl, idRl);
    }
  }

  public static void initRegistries() {
    ProcessorRegistries.register(Registries.Processor.Input.WORKBENCH);
    ProcessorRegistries.register(Registries.Processor.Output.WORKBENCH);
  }

  public static void initDefaults() {
    Registries.Processor.initDefaultProcessors();
  }
}
