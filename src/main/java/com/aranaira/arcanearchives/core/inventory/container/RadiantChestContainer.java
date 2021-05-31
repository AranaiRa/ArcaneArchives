package com.aranaira.arcanearchives.core.inventory.container;

import com.aranaira.arcanearchives.core.init.ModContainers;
import com.aranaira.arcanearchives.core.inventory.handlers.RadiantChestInventory;
import com.aranaira.arcanearchives.core.inventory.slot.RadiantChestSlot;
import com.aranaira.arcanearchives.core.tiles.RadiantChestTile;
import net.minecraft.entity.player.PlayerInventory;

import javax.annotation.Nullable;

public class RadiantChestContainer extends AbstractLargeContainer<RadiantChestInventory, RadiantChestTile> {
  public RadiantChestContainer(int id, PlayerInventory inventory) {
    this(id, inventory, null);
  }

  public RadiantChestContainer(int id, PlayerInventory playerInventory, RadiantChestTile tile) {
    super(ModContainers.RADIANT_CHEST.get(), id, 9, playerInventory, tile);
    createInventorySlots();
    createPlayerSlots(142, 200, 16);
  }

  protected void createInventorySlots() {
    int slotIndex = 0;
    for (int row = 0; row < 6; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = 16 + col * 18;
        int y = row * 18 + 16;
        this.addSlot(new RadiantChestSlot(this.getTileInventory(), slotIndex, x, y));
        slotIndex++;
      }
    }
  }

  @Nullable
  @Override
  public RadiantChestInventory getEmptyInventory() {
    return RadiantChestInventory.getEmpty();
  }
}
