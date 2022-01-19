/*
package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import java.util.*;

public class OldDomain implements Iterable<OldDomainEntry> {
  private final UUID domainId;
  private long lastUpdated = -1;
  private ItemTracking tracking = null;

  private final Map<UUID, OldDomainReferenceEntry> entryMap = new HashMap<>();

  public OldDomain(UUID domainId) {
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
  public Iterator<OldDomainEntry> iterator() {
    return entryMap.values().stream().map(OldDomainReferenceEntry::getEntry).iterator();
  }

  public Map<UUID, OldDomainReferenceEntry> getEntries() {
    return entryMap;
  }

  public boolean unenlist (OldDomainEntry entry) {
    return unenlist(entry.getEntityId());
  }

  public boolean unenlist (UUID entryId) {
    return entryMap.remove(entryId) == null;
  }

  public void addOrUpdateEntry (OldDomainEntry entry) {
    OldDomainReferenceEntry existing = entryMap.get(entry.getEntityId());
    OldDomainEntry actualEntry;
    if (existing != null) {
      actualEntry = existing.getEntry();

    }
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
    for (OldDomainEntry entry : this) {
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
      for (OldDomainEntry entry : this) {
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

}
*/
