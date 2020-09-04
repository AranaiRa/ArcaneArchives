package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.immanence.ImmanenceGlobal;
import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class NetworkAggregator {
  private static int QUEUE_LIMIT = 50;
  private static boolean loaded = false;

  public static final Map<UUID, Network> storage = new HashMap<>();
  // TODO: Store network IDs

  public static Network byId(UUID networkId) {
    return storage.computeIfAbsent(networkId, (key) -> new Network(networkId));
  }

  private static void rebuildBySet (Set<UUID> networkIds) {
    networkIds.forEach(NetworkAggregator::byId);
  }

  public static Collection<Network> getNetworks() {
    return storage.values();
  }

  public static UUID generateId () {
    UUID uuid = UUID.randomUUID();
    Set<UUID> existing = storage.keySet();
    while (existing.contains(uuid)) {
      uuid = UUID.randomUUID();
    }
    return uuid;
  }

  private static Set<Wrapper> incomingTiles = new HashSet<>();
  private static Set<NetworkedBaseTile> outgoingTiles = new HashSet<>();

  public static void tileJoin(NetworkedBaseTile tile) {
    incomingTiles.add(new Wrapper(tile));
  }

  public static void tileLeave(NetworkedBaseTile tile) {
    outgoingTiles.add(tile);
  }

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  public static void onServerTick(TickEvent.ServerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    if (!loaded) {
      rebuildBySet(DataHelper.NetworkReference.getAllNetworks());
      loaded = true;
    }

    if (!incomingTiles.isEmpty()) {
      Set<Wrapper> removed = new HashSet<>();
      for (Wrapper wrapper : incomingTiles) {
        if (wrapper.expired()) {
          outgoingTiles.add(wrapper.tile);
          removed.add(wrapper);
        } else {
          if (wrapper.tile.generatesNetworkId()) {
            wrapper.tile.generateNetworkId();
          }
          Network network = wrapper.tile.getNetwork();
          if (network == null || wrapper.tile.getWorld() == null) {
            wrapper.tick();
          } else {
            network.add(wrapper.tile);
            removed.add(wrapper);
            wrapper.tile.onNetworkJoined(network);
          }
        }
      }
      incomingTiles.removeAll(removed);
    }

    if (!outgoingTiles.isEmpty()) {
      Set<NetworkedBaseTile> removed = new HashSet<>();
      for (NetworkedBaseTile tile : outgoingTiles) {
        Network network = tile.getNetwork();
        if (network == null) {
          continue;
        }

        network.remove(tile);
        World world = tile.getWorld();
        world.destroyBlock(tile.getPos(), true);
        removed.add(tile);
      }

      outgoingTiles.removeAll(removed);
    }

    int tick = FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter();
    if (tick % ImmanenceGlobal.immanenceTick == 0) {
      ImmanenceGlobal.tickImmanence(tick / ImmanenceGlobal.immanenceTick);
    }
  }

  private static class Wrapper {
    int ticker = 0;
    NetworkedBaseTile tile;

    public Wrapper(NetworkedBaseTile tile) {
      this.tile = tile;
    }

    public void tick() {
      ticker++;
    }

    public boolean expired() {
      return ticker > QUEUE_LIMIT;
    }
  }
}
