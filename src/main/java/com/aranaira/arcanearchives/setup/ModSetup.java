package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.client.data.AABlockStateProvider;
import com.aranaira.arcanearchives.client.data.AAItemModelProvider;
import com.aranaira.arcanearchives.client.data.AALangProvider;
import com.aranaira.arcanearchives.data.AALootTableProvider;
import com.aranaira.arcanearchives.data.AARecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class ModSetup {
  public ModSetup() {
  }

  public void init(FMLCommonSetupEvent event) {
  }

  public void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    if (event.includeClient()) {
      gen.addProvider(new AABlockStateProvider(gen, event.getExistingFileHelper()));
      gen.addProvider(new AAItemModelProvider(gen, event.getExistingFileHelper()));
      gen.addProvider(new AALangProvider(gen));
    }
    if (event.includeServer()) {
      gen.addProvider(new AALootTableProvider(gen));
      gen.addProvider(new AARecipeProvider(gen));
    }
  }
}
