package com.aranaira.arcanearchives.inventory.container;

import com.aranaira.arcanearchives.client.screen.ManifestScreen;
import com.aranaira.arcanearchives.inventory.slot.LockedSlot;
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
  public boolean stillValid(PlayerEntity playerIn) {
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
          ManifestScreen.TMP_INVENTORY.setItem(l + k * 9, this.itemList.get(i1));
        } else {
          ManifestScreen.TMP_INVENTORY.setItem(l + k * 9, ItemStack.EMPTY);
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
  public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
    if (index >= this.slots.size() - 9 && index < this.slots.size()) {
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
        slot.set(ItemStack.EMPTY);
      }
    }

    return ItemStack.EMPTY;
  }

  /**
   * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
   * is null for the initial slot that was double-clicked.
   */
  public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
    return slotIn.container != ManifestScreen.TMP_INVENTORY;
  }

  /**
   * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
   * the slot can be added to a list of Slots to split the held ItemStack across.
   */
  public boolean canDragTo(Slot slotIn) {
    return slotIn.container != ManifestScreen.TMP_INVENTORY;
  }
}
