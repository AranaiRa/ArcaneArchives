package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.block.entity.INetworkedBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ArcaneArchivesAPI.MODID)
public class DomainManager {
  public static Set<RegistryKey<World>> WORLDS = null;
  private static volatile boolean forceRefresh = false;

  private static final Map<UUID, Domain> domainMap = new LinkedHashMap<>();
  private static final Set<DomainEntry> pendingEntries = new LinkedHashSet<>();
  private static volatile boolean editingDomainMap;
  private static final Object domainTickLock = new Object();
  private static final Object domainWriteLock = new Object();

  // TODO: Is this superfluous?
  private static final Set<UnknownEntry> unknownTiles = new LinkedHashSet<>();
  private static final Set<UnknownEntry> pendingUnknownTiles = new LinkedHashSet<>();
  private static volatile boolean tickingUnknown;
  private static final Object unknownTickLock = new Object();
  private static final Object unknownWriteLock = new Object();

  @SubscribeEvent
  public static void onServerStarted(FMLServerStartedEvent event) {
    Optional<MutableRegistry<World>> opRg = event.getServer().registryAccess().registry(Registry.DIMENSION_REGISTRY);
    opRg.ifPresent(reg -> {
      WORLDS = reg.keySet().stream().map(o -> RegistryKey.create(Registry.DIMENSION_REGISTRY, o)).collect(Collectors.toSet());
    });
  }

  @SubscribeEvent
  public static void onServerStopped(FMLServerStoppedEvent event) {
    WORLDS = null;
  }

  @SubscribeEvent
  public static void serverTick (TickEvent.ServerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    if (WORLDS == null) {
      return;
    }

    if (!forceRefresh) {
      return;
    }

    forceRefresh = false;

    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    Map<UUID, List<INetworkedBlockEntity>> networkMap = new HashMap<>();

    for (RegistryKey<World> key : WORLDS) {
      ServerWorld world = server.getLevel(key);
      if (world != null) {
        for (TileEntity te : world.blockEntityList) {
          if (te instanceof INetworkedBlockEntity) {
            INetworkedBlockEntity ine = (INetworkedBlockEntity) te;
            if (ine.getNetworkId() == null) {
              // handle a null entity?
            } else {
              networkMap.computeIfAbsent(ine.getNetworkId(), k -> new ArrayList<>()).add(ine);
            }
          }
        }
      }
    }
  }

  public static void forceRefresh () {
    forceRefresh = true;
  }

  public static Domain getDomain (UUID domainId) {
    return domainMap.computeIfAbsent(domainId, k -> new Domain());
  }

  public static void register (INetworkedBlockEntity entity) {

  }
}
