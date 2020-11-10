package com.aranaira.arcanearchives.util.ticker;

import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Ticker {
  private static final Object writeLock = new Object();
  private static final Object listLock = new Object();

  private static boolean listTicking = false;

  private static final LinkedHashSet<ITicker<MinecraftServer, NetworkedBaseTile>> waitList = new LinkedHashSet<>();
  private static final LinkedHashSet<ITicker<MinecraftServer, NetworkedBaseTile>> tickList = new LinkedHashSet<>();

  private enum ServerType {UNKNOWN, DEDICATED, INTEGRATED}

  private static ServerType serverType = ServerType.UNKNOWN;

  public static void tick(TickEvent event) {
    if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && event.type == TickEvent.Type.CLIENT) {
      if (serverType == ServerType.DEDICATED || serverType == ServerType.UNKNOWN) {
        synchronized (listLock) {
          listTicking = true;
          tickList.clear();
          listTicking = false;
        }
      }
    } else if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.type == TickEvent.Type.SERVER) {
      // TODO: Check how Lootr did this
/*      if (serverType == ServerType.UNKNOWN) {
        if (FMLCommonHandler.instance().getSide() == LogicalSide.CLIENT) {
          serverType = ServerType.INTEGRATED;
        } else {
          serverType = ServerType.DEDICATED;
        }
      }*/

      Set<ITicker<MinecraftServer, NetworkedBaseTile>> listCopy;
      Set<ITicker<MinecraftServer, NetworkedBaseTile>> removed = new HashSet<>();
      synchronized (listLock) {
        listTicking = true;
        listCopy = new HashSet<>(tickList);
        listTicking = false;
      }

      MinecraftServer type = ServerLifecycleHooks.getCurrentServer();

      synchronized (writeLock) {
        for (ITicker<MinecraftServer, NetworkedBaseTile> ticker : listCopy) {
          if (ticker.expired()) {
            removed.add(ticker);
            continue;
          }
          if (ticker.decaying()) {
            if (ticker.decay(type)) {
              removed.add(ticker);
              continue;
            }
          } else {
            if (ticker.run(type)) {
              removed.add(ticker);
              continue;
            }
          }
          if (ticker.invalid(type)) {
            removed.add(ticker);
          }
        }
      }

      synchronized (listLock) {
        listTicking = true;
        tickList.removeAll(removed);
        tickList.addAll(waitList);
        listTicking = false;
        waitList.clear();
      }
    }
  }

  public static void addTicker(ITicker<MinecraftServer, NetworkedBaseTile> ticker) {
    synchronized (listLock) {
      if (listTicking) {
        waitList.add(ticker);
      } else {
        tickList.add(ticker);
      }
    }
  }
}
