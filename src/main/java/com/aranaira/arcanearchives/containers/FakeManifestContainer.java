package com.aranaira.arcanearchives.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class FakeManifestContainer extends Container {
  private EntityPlayer player;

  public FakeManifestContainer(EntityPlayer playerIn) {
    this.player = playerIn;

    for (int i = 0; i < 81; i++) {
      this.addSlotToContainer(new UnusedSlot((i % ManifestContainer.NUM_CELLS) * ManifestContainer.GRID_SPACING + ManifestContainer.FIRST_CELL_X, (i / ManifestContainer.NUM_CELLS) * ManifestContainer.GRID_SPACING + ManifestContainer.FIRST_CELL_Y));
    }
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
    return true;
  }

  @Override
  public void putStackInSlot(int slotID, ItemStack stack) {
  }

  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);

  public static class UnusedSlot extends Slot {
    public UnusedSlot(int xPosition, int yPosition) {
      super(emptyInventory, 0, xPosition, yPosition);
    }
  }
}
