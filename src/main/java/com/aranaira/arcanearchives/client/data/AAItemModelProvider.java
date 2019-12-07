package com.aranaira.arcanearchives.client.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ModBlocks;
import epicsquid.mysticallib.client.data.DeferredItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class AAItemModelProvider extends DeferredItemModelProvider {
  public AAItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super("Arcane Archives Item Model Generator", generator, ArcaneArchives.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
  }
}
