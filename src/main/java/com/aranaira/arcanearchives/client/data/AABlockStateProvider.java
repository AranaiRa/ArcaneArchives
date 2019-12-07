package com.aranaira.arcanearchives.client.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ModBlocks;
import epicsquid.mysticallib.client.data.DeferredBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

public class AABlockStateProvider extends DeferredBlockStateProvider {
  public AABlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super("Arcane Archives Block State & Model Generator", gen, ArcaneArchives.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
  }
}
