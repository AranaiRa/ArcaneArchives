package com.aranaira.arcanearchives.api.inventory.slot;

import com.aranaira.arcanearchives.inventory.RadiantChestInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/* Taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/DankSlot.java
 */
public class RadiantChestSlot extends SlotItemHandler {
  private final int index;
  public RadiantChestSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
    this.index = index;
  }

  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    // TODO: Is this necessary? I forget how containers and slots work.
    return true;
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {
    return ((RadiantChestInventory)this.getItemHandler()).getSlotLimit(this.index, stack);
  }

  @Override
  public boolean isSameInventory(Slot other) {
    return other instanceof RadiantChestSlot && ((RadiantChestSlot)other).getItemHandler() == this.getItemHandler();
  }
}
