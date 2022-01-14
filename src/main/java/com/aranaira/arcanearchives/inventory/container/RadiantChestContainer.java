package com.aranaira.arcanearchives.inventory.container;

import com.aranaira.arcanearchives.block.entity.RadiantChestBlockEntity;
import com.aranaira.arcanearchives.init.ModBlocks;
import com.aranaira.arcanearchives.init.ModContainers;
import com.aranaira.arcanearchives.inventory.handlers.RadiantChestInventory;
import com.aranaira.arcanearchives.inventory.slot.RadiantChestSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RadiantChestContainer extends AbstractLargeContainer<RadiantChestInventory, RadiantChestBlockEntity> {
  private final List<Slot> playerSlots = new ArrayList<>();

  public RadiantChestContainer(ContainerType<RadiantChestContainer> type, int id, PlayerInventory inventory, PacketBuffer buffer) {
    // TODO: FIX THIS
    super(type, id, 9, inventory, buffer.readBlockPos());
    createInventorySlots();
    createPlayerSlots(142, 200, 16, playerSlots);
  }

  public RadiantChestContainer(int id, PlayerInventory playerInventory, IWorldPosCallable access) {
    super(ModContainers.RADIANT_CHEST.get(), id, 9, playerInventory, access);
    createInventorySlots();
    createPlayerSlots(142, 200, 16, playerSlots);
  }

  @Override
  protected RadiantChestBlockEntity resolveBlockEntity(TileEntity be) {
    if (be instanceof RadiantChestBlockEntity) {
      return (RadiantChestBlockEntity) be;
    } else {
      return null;
    }
  }

  protected void createInventorySlots() {
    int slotIndex = 0;
    for (int row = 0; row < 6; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = 16 + col * 18;
        int y = row * 18 + 16;
        this.addSlot(new RadiantChestSlot(this.getBlockEntityInventory(), slotIndex, x, y));
        slotIndex++;
      }
    }
  }

  @Nullable
  @Override
  public RadiantChestInventory getEmptyInventory() {
    return RadiantChestInventory.getEmpty();
  }

  @Override
  public void inventoryChanged(ILargeInventory inventory, int slot) {
  }

  @Override
  public List<Slot> getIngredientSlots() {
    return Collections.emptyList();
  }

  @Override
  public List<Slot> getCombinedIngredientSlots() {
    return getPlayerSlots();
  }

  @Override
  public List<Slot> getPlayerSlots() {
    return playerSlots;
  }

  @Override
  public boolean stillValid(PlayerEntity playerIn) {
    return stillValid(this.access, playerIn, ModBlocks.RADIANT_CHEST.get());
  }
}
