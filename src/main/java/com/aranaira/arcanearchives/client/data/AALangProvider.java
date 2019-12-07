package com.aranaira.arcanearchives.client.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ModBlocks;
import epicsquid.mysticallib.client.data.DeferredLanguageProvider;
import net.minecraft.data.DataGenerator;

public class AALangProvider extends DeferredLanguageProvider {
  public AALangProvider(DataGenerator gen) {
    super(gen, ArcaneArchives.MODID);
  }

  @Override
  protected void addTranslations() {
    addBlock(ModBlocks.RESONATOR);
    addBlock(ModBlocks.QUARTZ_CLUSTER);

    addItemGroup(ArcaneArchives.ITEM_GROUP, "Arcane Archives");
  }
}
