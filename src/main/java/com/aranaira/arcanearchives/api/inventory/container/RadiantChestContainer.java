package com.aranaira.arcanearchives.api.inventory.container;

import com.aranaira.arcanearchives.api.crafting.IPlayerContainer;
import com.aranaira.arcanearchives.api.inventory.slot.CappedSlot;
import com.aranaira.arcanearchives.api.inventory.slot.RadiantChestSlot;
import com.aranaira.arcanearchives.api.tiles.IInventoryTile;
import com.aranaira.arcanearchives.inventory.RadiantChestInventory;
import com.aranaira.arcanearchives.tiles.RadiantChestTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/* Contains some code taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/DankSlot.java
 */
public class RadiantChestContainer extends Container implements IPlayerContainer, IInventoryTile<RadiantChestInventory> {
  private final PlayerInventory player;
  private final RadiantChestTile tile;

  private final int rows = 9;

  public RadiantChestContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, RadiantChestTile tile) {
    super(type, id);
    this.player = playerInventory;
    this.tile = tile;
    createInventorySlots();
    createPlayerSlots();
  }

  private void createPlayerSlots() {
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = 16 + col * 18;
        int y = row * 18 + 142;
        this.addSlot(new CappedSlot(this.player, col + row * 9 + 9, x, y));
      }
    }

    for (int row = 0; row < 9; ++row) {
      int x = 16 + row * 18;
      int y = 200;
      this.addSlot(new CappedSlot(this.player, row, x, y));
    }
  }

  private void createInventorySlots() {
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

  @Override
  public PlayerEntity getPlayer() {
    return player.player;
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    // TODO
    return true;
  }

  @Override
  public RadiantChestInventory getTileInventory() {
    return tile.getTileInventory();
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index < rows * 9) {
        if (!this.mergeItemStack(itemstack1, rows * 9, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, rows * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }
    return itemstack;
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    ItemStack itemstack = ItemStack.EMPTY;

    if (clickTypeIn == ClickType.QUICK_CRAFT) {
      int drag = this.dragEvent;
      this.dragEvent = getDragEvent(dragType);

      if ((drag != 1 || this.dragEvent != 2) && drag != this.dragEvent) {
        this.resetDrag();
      } else if (this.player.getItemStack().isEmpty()) {
        this.resetDrag();
      } else if (this.dragEvent == 0) { // start drag
        this.dragMode = extractDragMode(dragType);
        if (isValidDragMode(this.dragMode, player)) {
          this.dragEvent = 1;
          this.dragSlots.clear();
        } else {
          this.resetDrag();
        }
      } else if (this.dragEvent == 1) { // add slot
        Slot addedSlot = this.inventorySlots.get(slotId);
        ItemStack mouseStack = this.player.getItemStack();

        if (addedSlot != null && addedSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size() && this.canDragIntoSlot(addedSlot))) {
          this.dragSlots.add(addedSlot);
        }
      } else if (this.dragEvent == 2) { // end drag
        if (!this.dragSlots.isEmpty()) {
          ItemStack mouseCopy = this.player.getItemStack().copy();
          int mouseCount = mouseCopy.getCount();

          for (Slot draggedSlot : this.dragSlots) {
            ItemStack mouseStack = this.player.getItemStack();

            if (draggedSlot != null && draggedSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(draggedSlot)) {
              ItemStack dragCopy = mouseCopy.copy();
              int slotCount = draggedSlot.getHasStack() ? draggedSlot.getStack().getCount() : 0;
              computeStackSize(this.dragSlots, this.dragMode, dragCopy, slotCount);
              int slotLimit = draggedSlot.getItemStackLimit(dragCopy);
              if (dragCopy.getCount() > slotLimit) {
                dragCopy.setCount(slotLimit);
              }

              mouseCount -= dragCopy.getCount() - slotCount;
              draggedSlot.putStack(dragCopy);
            }
          }

          mouseCopy.setCount(mouseCount);
          this.player.setItemStack(mouseCopy);
        }

        this.resetDrag();
      } else {
        this.resetDrag();
      }
    } else if (this.dragEvent != 0) {
      this.resetDrag();
    } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
      if (slotId == -999) {
        if (!this.player.getItemStack().isEmpty()) {
          if (dragType == 0) {
            player.dropItem(this.player.getItemStack(), true);
            this.player.setItemStack(ItemStack.EMPTY);
          }

          if (dragType == 1) {
            player.dropItem(this.player.getItemStack().split(1), true);
          }
        }
      }
    } else if (clickTypeIn == ClickType.QUICK_MOVE) {
      if (slotId < 0) {
        return ItemStack.EMPTY;
      }

      Slot quickSlot = this.inventorySlots.get(slotId);

      if (quickSlot == null || !quickSlot.canTakeStack(player)) {
        return ItemStack.EMPTY;
      }

      for (ItemStack transferStack = this.transferStackInSlot(player, slotId); !transferStack.isEmpty() && ItemStack.areItemsEqual(quickSlot.getStack(), transferStack); transferStack = this.transferStackInSlot(player, slotId)) {
        itemstack = transferStack.copy();
      }
    } else {
      if (slotId < 0) {
        return ItemStack.EMPTY;
      }

      Slot clickedSlot = this.inventorySlots.get(slotId);

      if (clickedSlot != null) {
        ItemStack clickedStack = clickedSlot.getStack();
        ItemStack mouseStack = this.player.getItemStack();

        if (!clickedStack.isEmpty()) {
          itemstack = clickedStack.copy();
        }

        if (clickedStack.isEmpty()) {
          if (!mouseStack.isEmpty() && clickedSlot.isItemValid(mouseStack)) {
            int countFromDrag = dragType == 0 ? mouseStack.getCount() : 1;

            if (countFromDrag > clickedSlot.getItemStackLimit(mouseStack)) {
              countFromDrag = clickedSlot.getItemStackLimit(mouseStack);
            }

            clickedSlot.putStack(mouseStack.split(countFromDrag));
          }
        } else if (clickedSlot.canTakeStack(player)) {
          if (mouseStack.isEmpty()) {
            if (clickedStack.isEmpty()) {
              clickedSlot.putStack(ItemStack.EMPTY);
              this.player.setItemStack(ItemStack.EMPTY);
            } else {
              int toMove;
              if (clickedSlot instanceof RadiantChestSlot) {
                if (clickedStack.getMaxStackSize() < clickedStack.getCount()) {
                  toMove = dragType == 0 ? clickedStack.getMaxStackSize() : (clickedStack.getMaxStackSize() + 1) / 2;
                } else {
                  toMove = dragType == 0 ? clickedStack.getCount() : (clickedStack.getCount() + 1) / 2;
                }
              } else {
                toMove = dragType == 0 ? clickedStack.getCount() : (clickedStack.getCount() + 1) / 2;
              }
              this.player.setItemStack(clickedSlot.decrStackSize(toMove));

              if (clickedStack.isEmpty()) {
                clickedSlot.putStack(ItemStack.EMPTY);
              }

              clickedSlot.onTake(player, this.player.getItemStack());
            }
          }
        } else if (clickedSlot.isItemValid(mouseStack)) {
          if (clickedStack.getItem() == mouseStack.getItem() && ItemStack.areItemStackTagsEqual(clickedStack, mouseStack)) {
            int toMove = dragType == 0 ? mouseStack.getCount() : 1;
            if (toMove > clickedSlot.getItemStackLimit(mouseStack) - clickedStack.getCount()) {
              toMove = clickedSlot.getItemStackLimit(mouseStack) - clickedStack.getCount();
            }

            mouseStack.shrink(toMove);
            clickedStack.grow(toMove);
          } else if (mouseStack.getCount() <= clickedSlot.getItemStackLimit(mouseStack) && clickedStack.getCount() <= clickedStack.getMaxStackSize()) {
            clickedSlot.putStack(mouseStack);
            this.player.setItemStack(clickedStack);
          }
        } else if (clickedStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.areItemStackTagsEqual(clickedStack, mouseStack) ) {
          int toMove = clickedStack.getCount();

          if (toMove + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
            mouseStack.grow(toMove);
            clickedStack = clickedSlot.decrStackSize(toMove);

            if (clickedStack.isEmpty()) {
              clickedSlot.putStack(ItemStack.EMPTY);
            }

            clickedSlot.onTake(player, this.player.getItemStack());
          }
        }
      }

      clickedSlot.onSlotChanged();
    }
  }
}
