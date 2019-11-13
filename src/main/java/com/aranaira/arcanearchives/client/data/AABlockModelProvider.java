package com.aranaira.arcanearchives.client.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ModBlocks;
import epicsquid.mysticallib.client.data.DeferredItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelProvider;

public class AABlockModelProvider extends BlockModelProvider {
  public AABlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, ArcaneArchives.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    withExistingParent("radiant_resonator", modLoc("block/radiant_resonator.obj"));
  }

  @Override
  public String getName() {
    return "Arcane Archives Block Model Generator";
  }
}
