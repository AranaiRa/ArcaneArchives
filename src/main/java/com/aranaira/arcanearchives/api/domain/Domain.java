package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import java.util.*;

public class Domain implements Iterable<DomainEntry> {
  private final UUID domainId;
  private long lastUpdated = -1;
  private ItemTracking tracking = null;

  private final Map<UUID, DomainEntry> entryMap = new HashMap<>();

  public Domain(UUID domainId) {
    this.domainId = domainId;
  }

  public void clearEntries() {
    entryMap.clear();
  }

  public UUID getDomainId() {
    return domainId;
  }

  public long getLastUpdated() {
    return lastUpdated;
  }

  @Override
  public Iterator<DomainEntry> iterator() {
    return entryMap.values().iterator();
  }

  public Map<UUID, DomainEntry> getEntries() {
    return entryMap;
  }

  public boolean unenlist (DomainEntry entry) {
    return unenlist(entry.getEntityId());
  }

  public boolean unenlist (UUID entryId) {
    return entryMap.remove(entryId) == null;
  }

  public void addOrUpdateEntry (DomainEntry entry) {
    DomainEntry existing = entryMap.get(entry.getEntityId());
    boolean changed;
    if (existing != null) {
      changed = existing.updateFrom(entry);
    } else {
      entryMap.put(entry.getEntityId(), entry);
      changed = true;
    }
    if (changed) {
      // update the domain data
    }
  }

  private void rebuildTracking() {
    tracking = new ItemTracking();
    for (DomainEntry entry : this) {
      if (!entry.hasInventory()) {
        continue;
      }

      RemoteInventory inv = entry.getInventory();
      tracking.combine(inv.getTracking());
    }
    //noinspection ConstantConditions
    lastUpdated = ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD).getGameTime();
  }

  public ItemTracking getTracking() {
    if (lastUpdated == -1) {
      tracking = null;
    } else {
      for (DomainEntry entry : this) {
        if (entry.hasInventory() && !entry.hasTracking()) {
          tracking = null;
          break;
        }
      }
    }

    if (tracking == null) {
      rebuildTracking();
    }

    return tracking;
  }

  public static class DomainReferenceEntry {
    private final UUID domainId;
    private final UUID entryId;

    public DomainReferenceEntry(UUID domainId, UUID entryId) {
      this.domainId = domainId;
      this.entryId = entryId;
    }

    public UUID getDomainId() {
      return domainId;
    }

    public UUID getEntryId() {
      return entryId;
    }

    public DomainEntry getEntry () {
      return DataStorage.getDomainEntries().getEntry(entryId);
    }

    public boolean valid () {
      return getEntry() != null;
    }
  }
}
