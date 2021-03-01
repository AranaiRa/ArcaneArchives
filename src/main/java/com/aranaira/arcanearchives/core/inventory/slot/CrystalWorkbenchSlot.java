package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.inventory.handlers.RadiantChestInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/* Taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/DankSlot.java
 */
public class CrystalWorkbenchSlot extends SlotItemHandler {
  private final IArcaneInventory inventory;
  private final int index;

  public CrystalWorkbenchSlot(IArcaneInventory itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
    this.index = index;
    this.inventory = itemHandler;
  }

  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    // TODO: Is this necessary? I forget how containers and slots work.
    return true;
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {
    return ((CrystalWorkbenchInventory) this.getItemHandler()).getSlotLimit(this.index, stack);
  }

  @Override
  public boolean isSameInventory(Slot other) {
    return other instanceof CrystalWorkbenchSlot && ((CrystalWorkbenchSlot) other).getItemHandler() == this.getItemHandler();
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {
    super.onSlotChange(oldStackIn, newStackIn);
    onSlotChanged();
  }

  @Override
  public void onSlotChanged() {
    this.inventory.markDirty();
  }
}
