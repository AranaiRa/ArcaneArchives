package com.aranaira.arcanearchives.api.domain;

import com.aranaira.arcanearchives.api.inventory.RemoteInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.libs.noobutil.tracking.ItemTracking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Domain implements Iterable<DomainEntry<?>> {
  private final UUID domainId;
  private long lastUpdated = -1;
  private ItemTracking tracking = null;

  private final List<DomainEntry<?>> entries = new ArrayList<>();

  public Domain(UUID domainId) {
    this.domainId = domainId;
  }

  public void clearEntries() {
    entries.clear();
  }

  public UUID getDomainId() {
    return domainId;
  }

  public long getLastUpdated() {
    return lastUpdated;
  }

  @Override
  public Iterator<DomainEntry<?>> iterator() {
    return entries.iterator();
  }

  public List<DomainEntry<?>> getEntries() {
    return entries;
  }

  private void rebuildTracking() {
    tracking = new ItemTracking();
    for (DomainEntry<?> entry : this) {
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
      for (DomainEntry<?> entry : this) {
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
