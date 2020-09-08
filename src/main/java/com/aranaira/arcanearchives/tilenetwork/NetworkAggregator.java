package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.immanence.ImmanenceGlobal;
import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import com.aranaira.arcanearchives.util.ticker.Ticker;
import com.aranaira.arcanearchives.util.ticker.TileTicker;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class NetworkAggregator {
  private static boolean loaded = false;

  public static final Map<UUID, Network> storage = new HashMap<>();
  // TODO: Store network IDs

  public static Network byId(UUID networkId) {
    return storage.computeIfAbsent(networkId, (key) -> new Network(networkId));
  }

  private static void rebuildBySet(Set<UUID> networkIds) {
    networkIds.forEach(NetworkAggregator::byId);
  }

  public static Collection<Network> getNetworks() {
    return storage.values();
  }

  public static UUID generateId() {
    UUID uuid = UUID.randomUUID();
    Set<UUID> existing = storage.keySet();
    while (existing.contains(uuid)) {
      uuid = UUID.randomUUID();
    }
    return uuid;
  }

  public static void tileJoin(NetworkedBaseTile tile) {
    Ticker.addTicker(new TileTicker(tile));
  }

  public static void tileLeave(NetworkedBaseTile tile) {
    Ticker.addTicker(new TileTicker(tile, true));
  }

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  public static void onServerTick(TickEvent event) {
    if (!loaded && event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
      rebuildBySet(DataHelper.NetworkReference.getAllNetworks());
      loaded = true;

      int tick = FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter();
      if (tick % ImmanenceGlobal.immanenceTick == 0) {
        ImmanenceGlobal.tickImmanence(tick / ImmanenceGlobal.immanenceTick);
      }
    }

    Ticker.tick(event);
  }
}
