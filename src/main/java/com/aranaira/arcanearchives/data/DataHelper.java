/*package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.tilenetwork.NetworkName;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class DataHelper {
  public static ServerWorld getWorld() {
    return ServerLifecycleHooks.getCurrentServer().getWorld(World.OVERWORLD);
  }

  private static void save() {
    ServerWorld world = getWorld();
    Objects.requireNonNull(world.getSavedData()).save();
  }

  private static PlayerSaveData getPlayerData(PlayerEntity player) {
    return getData(PlayerSaveData.class, PlayerSaveData::new, player, PlayerSaveData::ID);
  }

  private static ServerNetworkData getServerNetworkData(Network network) {
    return getData(ServerNetworkData.class, ServerNetworkData::new, network, ServerNetworkData::ID);
  }

  private static ServerNetworkData getServerNetworkData(UUID network) {
    return getData(ServerNetworkData.class, ServerNetworkData::new, network, ServerNetworkData::ID);
  }

  public static NameData getNameData() {
    return getData(NameData.class, NameData::new, NameData.PREFIX, (o) -> NameData.PREFIX);
  }

  private static NetworkReferenceData getNetworkReferenceData() {
    return getData(NetworkReferenceData.class, NetworkReferenceData::new, NetworkReferenceData.PREFIX, (o) -> NetworkReferenceData.PREFIX);
  }

  private static <T extends WorldSavedData, U> T getData(Class<T> clazz, Function<U, T> provider, U value, Function<U, String> conversion) {
    // TODO: Fix this
    return null;
*//*    ServerWorld world = getWorld();
    @SuppressWarnings("unchecked") T saveData = (T) Objects.requireNonNull(world.getSavedData()).getOrCreate(() -> provider.apply(value), clazz);
, conversion.apply(value));
    if (saveData == null) {
      saveData = provider.apply(value);
      world.getMapStorage().setData(conversion.apply(value), saveData);
    }

    return saveData;*//*
  }

  public static class Names {
    public static void generate() {
      getNameData().generateNames();
      save();
    }

    public static NetworkName getNetworkName(Network network) {
      return getNetworkName(network.getNetworkId());
    }

    public static NetworkName getNetworkName(UUID uuid) {
      return getNameData().getOrGenerateName(uuid);
    }
  }

  public static class NetworkReference {
    public static Set<UUID> getAllNetworks() {
      return getNetworkReferenceData().getAllNetworks();
    }

    public static boolean containsNetwork(Network network) {
      return getNetworkReferenceData().containsNetwork(network);
    }

    public static boolean containsNetwork(UUID uuid) {
      return getNetworkReferenceData().containsNetwork(uuid);
    }

    public static void addNetwork(Network network) {
      getNetworkReferenceData().addNetwork(network);
      save();
    }

    public static void addNetwork(UUID uuid) {
      addNetwork(uuid, true);
    }

    public static void addNetwork(UUID uuid, boolean notify) {
      getNetworkReferenceData().addNetwork(uuid);
      save();
      if (notify) {
        *//*        PacketNetwork.NameMessage.sendToAll();*//*
      }
    }
  }
}*/
