package com.aranaira.arcanearchives.events.ticks;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ServerTickHandler extends TickHandler {
  public static ServerTickHandler INSTANCE = new ServerTickHandler();

  @SubscribeEvent
  public static void onServerTick(TickEvent.ServerTickEvent event) {
    if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
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
