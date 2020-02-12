package com.aranaira.enderio.core.client.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ClientHandler {

  private static int ticksElapsed;

  public static int getTicksElapsed() {
    return ticksElapsed;
  }

  @SubscribeEvent
  public static void onClientTick(@Nonnull ClientTickEvent event) {
    if (event.phase == Phase.END) {
      ticksElapsed++;
    }
  }

  private ClientHandler() {
  }

}
