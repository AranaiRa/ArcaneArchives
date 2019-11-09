package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.DataHelper;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class NetworksHandler {
  @SubscribeEvent
  public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    DataHelper.clearClientCache();
  }
}
