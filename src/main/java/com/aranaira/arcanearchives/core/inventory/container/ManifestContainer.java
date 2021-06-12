package com.aranaira.arcanearchives.core.inventory.container;

import com.aranaira.arcanearchives.client.screen.ManifestScreen;
import com.aranaira.arcanearchives.core.inventory.slot.LockedSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ManifestContainer extends Container {
  /**
   * the list of items in this container
   */
  public final NonNullList<ItemStack> itemList = NonNullList.create();

  public ManifestContainer(PlayerEntity player) {
    super(null, 0);
    PlayerInventory playerinventory = player.inventory;

    for (int i = 0; i < 5; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new LockedSlot(ManifestScreen.TMP_INVENTORY, i * 9 + j, 9 + j * 18, 18 + i * 18));
      }
    }

    for (int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(playerinventory, k, 9 + k * 18, 112));
    }

    this.scrollTo(0.0F);
  }

  /**
   * Determines whether supplied player can use this container
   */
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }

  /**
   * Updates the gui slots ItemStack's based on scroll position.
   */
  public void scrollTo(float pos) {
    int i = (this.itemList.size() + 9 - 1) / 9 - 5;
    int j = (int) ((double) (pos * (float) i) + 0.5D);
    if (j < 0) {
      j = 0;
    }

    for (int k = 0; k < 5; ++k) {
      for (int l = 0; l < 9; ++l) {
        int i1 = l + (k + j) * 9;
        if (i1 >= 0 && i1 < this.itemList.size()) {
          ManifestScreen.TMP_INVENTORY.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
        } else {
          ManifestScreen.TMP_INVENTORY.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
        }
      }
    }

  }

  public boolean canScroll() {
    return this.itemList.size() > 45;
  }

  /**
   * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
   * inventory and the other inventory(s).
   */
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size()) {
      Slot slot = this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
        slot.putStack(ItemStack.EMPTY);
      }
    }

    return ItemStack.EMPTY;
  }

  /**
   * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
   * is null for the initial slot that was double-clicked.
   */
  public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
    return slotIn.inventory != ManifestScreen.TMP_INVENTORY;
  }

  /**
   * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
   * the slot can be added to a list of Slots to split the held ItemStack across.
   */
  public boolean canDragIntoSlot(Slot slotIn) {
    return slotIn.inventory != ManifestScreen.TMP_INVENTORY;
  }
}
