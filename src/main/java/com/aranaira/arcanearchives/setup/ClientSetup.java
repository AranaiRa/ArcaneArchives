package com.aranaira.arcanearchives.setup;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

  public static void init(FMLClientSetupEvent event) {
    OBJLoader.INSTANCE.addDomain(ArcaneArchives.MODID);
  }
}
