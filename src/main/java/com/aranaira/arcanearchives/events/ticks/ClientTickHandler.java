package com.aranaira.arcanearchives.events.ticks;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ClientTickHandler extends TickHandler {
  private static ClientTickHandler INSTANCE = new ClientTickHandler();

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
      INSTANCE.handleTicks();
    }
  }

  public static void addRunnable(Runnable runnable) {
    INSTANCE.addRunnableToQueue(runnable, 0);
  }

  public static void addRunnable(Runnable runnable, int delay) {
    INSTANCE.addRunnableToQueue(runnable, delay);
  }
}
