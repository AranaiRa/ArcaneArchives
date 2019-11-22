package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.ArcaneArchives;
import epicsquid.mysticallib.util.ObjUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

  public static void init(FMLClientSetupEvent event) {
    ObjUtils.loadModel(new ResourceLocation(ArcaneArchives.MODID, "block/radiant_resonator.obj"));
    ObjUtils.loadTexture(new ResourceLocation(ArcaneArchives.MODID, "block/block_arcanearchives_master"));
  }
}
