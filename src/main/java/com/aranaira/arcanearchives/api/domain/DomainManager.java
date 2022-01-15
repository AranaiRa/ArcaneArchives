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

  private static final Map<UUID, Domain> domainMap = Collections.synchronizedMap(new HashMap<>());


  private static final Set<UnknownEntry> unknownEntities = Collections.synchronizedSet(new HashSet<>());
  private static final Set<UnknownEntry> pendingUnknownEntities = Collections.synchronizedSet(new HashSet<>());
  private static final Object unknownTickLock = new Object();
  private static final Object unknownWriteLock = new Object();
  private static volatile boolean tickingUnknown = false;

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
  public static void serverTick(TickEvent.ServerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

    Set<UnknownEntry> toRemove = new HashSet<>();
    Set<UnknownEntry> copy;
    synchronized (unknownTickLock) {
      tickingUnknown = true;
      copy = new HashSet<>(unknownEntities);
      tickingUnknown = false;
    }
    if (!copy.isEmpty()) {
      synchronized (unknownWriteLock) {
        for (UnknownEntry entry : copy) {
          ServerWorld level = server.getLevel(entry.getDimension());
          if (level == null || !level.isAreaLoaded(entry.getPosition(), 1)) {
            toRemove.add(entry);
            continue;
          }
          TileEntity tileAt = level.getBlockEntity(entry.getPosition());
          if (tileAt == null) {
            toRemove.add(entry);
            continue;
          }
          if (tileAt instanceof INetworkedBlockEntity) {
            toRemove.add(entry);
            INetworkedBlockEntity ine = (INetworkedBlockEntity) tileAt;
            if (ine.getNetworkId() != null) {
              getDomain(ine.getNetworkId()).getEntries().add(new DomainEntry(entry.getPosition(), entry.getDimension(), ine.getNetworkId(), ine.getClass()));
            } else {
              level.destroyBlock(entry.getPosition(), true);
            }
          } else {
            toRemove.add(entry);
          }
        }
      }
    }
    synchronized (unknownTickLock) {
      tickingUnknown = true;
      unknownEntities.removeAll(toRemove);
      unknownEntities.addAll(pendingUnknownEntities);
      tickingUnknown = false;
      pendingUnknownEntities.clear();
    }

    if (WORLDS == null) {
      return;
    }

    if (!forceRefresh) {
      return;
    }

    forceRefresh = false;

    Map<UUID, List<DomainEntry>> networkMap = new HashMap<>();

    for (RegistryKey<World> key : WORLDS) {
      ServerWorld world = server.getLevel(key);
      if (world != null) {
        for (TileEntity te : world.blockEntityList) {
          if (te instanceof INetworkedBlockEntity) {
            INetworkedBlockEntity ine = (INetworkedBlockEntity) te;
            if (ine.getNetworkId() == null) {
              // handle a null entity?
              UnknownEntry entry = new UnknownEntry(te.getBlockPos(), key);
            } else {
              DomainEntry entry = new DomainEntry(te.getBlockPos(), key, ine.getNetworkId(), ine.getClass());
              networkMap.computeIfAbsent(ine.getNetworkId(), k -> new ArrayList<>()).add(entry);
            }
          }
        }
      }
    }
  }

  public static void forceRefresh() {
    forceRefresh = true;
  }

  public static Domain getDomain(UUID domainId) {
    return domainMap.computeIfAbsent(domainId, Domain::new);
  }

  public static boolean register(INetworkedBlockEntity entity) {
    UUID id = entity.getNetworkId();
    TileEntity te = entity.getBlockEntity();
    if (te.getLevel() == null) {
      return false;
    }
    if (id == null) {
      addUnknownEntry(new UnknownEntry(te.getBlockPos(), te.getLevel().dimension()));
    } else {
      getDomain(id).getEntries().add(new DomainEntry(te.getBlockPos(), te.getLevel().dimension(), id, entity.getClass()));
    }
    return true;
  }

  public static void addUnknownEntry(UnknownEntry entry) {
    synchronized (unknownTickLock) {
      if (tickingUnknown) {
        pendingUnknownEntities.add(entry);
      } else {
        unknownEntities.add(entry);
      }
    }
  }
}
