package com.aranaira.arcanearchives.events.ticks;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid= ArcaneArchives.MODID)
public class ServerTickHandler extends TickHandler {
  public static ServerTickHandler INSTANCE = new ServerTickHandler();

  @SubscribeEvent
  public static void onServerTick (TickEvent.ServerTickEvent event) {
    if (event.phase == TickEvent.Phase.END && event.side == Side.SERVER) {
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
