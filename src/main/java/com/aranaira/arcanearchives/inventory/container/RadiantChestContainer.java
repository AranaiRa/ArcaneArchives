package com.aranaira.arcanearchives.inventory.container;

import com.aranaira.arcanearchives.api.crafting.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ITileContainer;
import com.aranaira.arcanearchives.api.inventory.slot.CappedSlot;
import com.aranaira.arcanearchives.inventory.RadiantChestSlot;
import com.aranaira.arcanearchives.init.ModContainers;
import com.aranaira.arcanearchives.inventory.RadiantChestInventory;
import com.aranaira.arcanearchives.network.ExtendedSlotContentsPacket;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.tiles.RadiantChestTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IntReferenceHolder;

/* Contains some code taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/DankSlot.java
 */
public class RadiantChestContainer extends Container implements IPlayerContainer, ITileContainer<RadiantChestInventory, RadiantChestTile> {
  private final PlayerInventory player;
  private final RadiantChestTile tile;
  private RadiantChestInventory inventory;

  @SuppressWarnings("FieldCanBeLocal")
  private final int rows = 9;

  public RadiantChestContainer(int id, PlayerInventory inventory) {
    this(id, inventory, null);
  }

  public RadiantChestContainer(int id, PlayerInventory playerInventory, RadiantChestTile tile) {
    super(ModContainers.RADIANT_CHEST.get(), id);
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
      int oldDragEvent = this.dragEvent;
      this.dragEvent = getDragEvent(dragType);

      if ((oldDragEvent != 1 || this.dragEvent != 2) && oldDragEvent != this.dragEvent) {
        this.resetDrag();
      } else if (this.player.getItemStack().isEmpty()) {
        this.resetDrag();
      } else if (this.dragEvent == 0) { // Start drag
        this.dragMode = extractDragMode(dragType);

        if (isValidDragMode(this.dragMode, player)) {
          this.dragEvent = 1;
          this.dragSlots.clear();
        } else {
          this.resetDrag();
        }
      } else if (this.dragEvent == 1) { // Add slot
        Slot addedSlot = this.inventorySlots.get(slotId);
        ItemStack mouseStack = this.player.getItemStack();

        // TODO: Check if this needs to be replaced
        if (addedSlot != null && canAddItemToSlot(addedSlot, mouseStack, true) && addedSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(addedSlot)) {
          this.dragSlots.add(addedSlot);
        }
      } else if (this.dragEvent == 2) { // End drag
        if (!this.dragSlots.isEmpty()) {
          ItemStack mouseStackCopy = this.player.getItemStack().copy();
          int mouseCount = this.player.getItemStack().getCount();

          for (Slot dragSlot : this.dragSlots) {
            ItemStack mouseStack = this.player.getItemStack();

            // TODO: Check if this needs to be replaced
            if (dragSlot != null && canAddItemToSlot(dragSlot, mouseStack, true) && dragSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(dragSlot)) {
              ItemStack dragCopy = mouseStackCopy.copy();
              int initialDragCount = dragSlot.getHasStack() ? dragSlot.getStack().getCount() : 0;
              computeStackSize(this.dragSlots, this.dragMode, dragCopy, initialDragCount);
              int slotLimit = dragSlot.getItemStackLimit(dragCopy);

              if (dragCopy.getCount() > slotLimit) {
                dragCopy.setCount(slotLimit);
              }

              mouseCount -= dragCopy.getCount() - initialDragCount;
              dragSlot.putStack(dragCopy);
            }
          }

          mouseStackCopy.setCount(mouseCount);
          this.player.setItemStack(mouseStackCopy);
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
      } else if (clickTypeIn == ClickType.QUICK_MOVE) {
        if (slotId < 0) {
          return ItemStack.EMPTY;
        }

        Slot transferSlot = this.inventorySlots.get(slotId);

        if (transferSlot == null || !transferSlot.canTakeStack(player)) {
          return ItemStack.EMPTY;
        }

        for (ItemStack transferStack = this.transferStackInSlot(player, slotId);
             !transferStack.isEmpty() && ItemStack.areItemsEqual(transferSlot.getStack(), transferStack); transferStack = this.transferStackInSlot(player, slotId)) {
          itemstack = transferStack.copy();
        }
      } else {
        if (slotId < 0) {
          return ItemStack.EMPTY;
        }

        Slot clickedSlot = this.inventorySlots.get(slotId);

        if (clickedSlot != null) {
          ItemStack slotStack = clickedSlot.getStack();
          ItemStack mouseStack = this.player.getItemStack();

          if (!slotStack.isEmpty()) {
            itemstack = slotStack.copy();
          }

          if (slotStack.isEmpty()) {
            if (!mouseStack.isEmpty() && clickedSlot.isItemValid(mouseStack)) {
              int splitCount = dragType == 0 ? mouseStack.getCount() : 1;

              if (splitCount > clickedSlot.getItemStackLimit(mouseStack)) {
                splitCount = clickedSlot.getItemStackLimit(mouseStack);
              }

              clickedSlot.putStack(mouseStack.split(splitCount));
            }
          } else if (clickedSlot.canTakeStack(player)) {
            if (mouseStack.isEmpty()) {
              if (slotStack.isEmpty()) {
                clickedSlot.putStack(ItemStack.EMPTY);
                this.player.setItemStack(ItemStack.EMPTY);
              } else {
                int toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                if (clickedSlot instanceof RadiantChestSlot && slotStack.getMaxStackSize() < slotStack.getCount()) {
                  toMove = dragType == 0 ? slotStack.getMaxStackSize() : (slotStack.getMaxStackSize() + 1) / 2;
                }
                this.player.setItemStack(clickedSlot.decrStackSize(toMove));

                if (slotStack.isEmpty()) {
                  clickedSlot.putStack(ItemStack.EMPTY);
                }

                clickedSlot.onTake(player, this.player.getItemStack());
              }
            } else if (clickedSlot.isItemValid(mouseStack)) {
              if (slotStack.getItem() == mouseStack.getItem() && ItemStack.areItemStackTagsEqual(slotStack, mouseStack)) {
                int k2 = dragType == 0 ? mouseStack.getCount() : 1;

                if (k2 > clickedSlot.getItemStackLimit(mouseStack) - slotStack.getCount()) {
                  k2 = clickedSlot.getItemStackLimit(mouseStack) - slotStack.getCount();
                }

                mouseStack.shrink(k2);
                slotStack.grow(k2);
              } else if (mouseStack.getCount() <= clickedSlot.getItemStackLimit(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                clickedSlot.putStack(mouseStack);
                this.player.setItemStack(slotStack);
              }
            } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.areItemStackTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty()) {
              int j2 = slotStack.getCount();

              if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                mouseStack.grow(j2);
                slotStack = clickedSlot.decrStackSize(j2);

                if (slotStack.isEmpty()) {
                  clickedSlot.putStack(ItemStack.EMPTY);
                }

                clickedSlot.onTake(player, this.player.getItemStack());
              }
            }
          }

          clickedSlot.onSlotChanged();
        }
      }
    } else if (clickTypeIn == ClickType.CLONE && player.abilities.isCreativeMode && this.player.getItemStack().isEmpty() && slotId >= 0) {
      Slot cloneSlot = this.inventorySlots.get(slotId);

      if (cloneSlot != null && cloneSlot.getHasStack()) {
        ItemStack clonedStack = cloneSlot.getStack().copy();
        clonedStack.setCount(clonedStack.getMaxStackSize());
        this.player.setItemStack(clonedStack);
      }
    } else if (clickTypeIn == ClickType.THROW && this.player.getItemStack().isEmpty() && slotId >= 0) {
      Slot throwSlot = this.inventorySlots.get(slotId);

      if (throwSlot != null && throwSlot.getHasStack() && throwSlot.canTakeStack(player)) {
        ItemStack thrownSlot = throwSlot.decrStackSize(dragType == 0 ? 1 : throwSlot.getStack().getCount());
        throwSlot.onTake(player, thrownSlot);
        player.dropItem(thrownSlot, true);
      }
    } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
      Slot pickupSlot = this.inventorySlots.get(slotId);
      ItemStack mouseStack = this.player.getItemStack();

      if (!mouseStack.isEmpty() && (pickupSlot == null || !pickupSlot.getHasStack() || !pickupSlot.canTakeStack(player))) {
        int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
        int j = dragType == 0 ? 1 : -1;

        for (int k = 0; k < 2; ++k) {
          for (int l = i; l >= 0 && l < this.inventorySlots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
            Slot slot1 = this.inventorySlots.get(l);

            if (slot1.getHasStack() && canAddItemToSlot(slot1, mouseStack, true) && slot1.canTakeStack(player) && this.canMergeSlot(mouseStack, slot1)) {
              ItemStack itemstack2 = slot1.getStack();

              if (k != 0 || itemstack2.getCount() < slot1.getItemStackLimit(itemstack2)) {
                int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
                ItemStack itemstack3 = slot1.decrStackSize(i1);
                mouseStack.grow(i1);

                if (itemstack3.isEmpty()) {
                  slot1.putStack(ItemStack.EMPTY);
                }

                slot1.onTake(player, itemstack3);
              }
            }
          }
        }
      }

      this.detectAndSendChanges();
    }

    if (itemstack.getCount() > 64) {
      itemstack = itemstack.copy();
      itemstack.setCount(64);
    }

    return itemstack;
  }

  @Override
  protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    boolean flag = false;
    int i = startIndex;

    if (reverseDirection) {
      i = endIndex - 1;
    }

    while (!stack.isEmpty()) {
      if (reverseDirection) {
        if (i < startIndex) break;
      } else {
        if (i >= endIndex) break;
      }

      Slot slot = this.inventorySlots.get(i);
      ItemStack slotStack = slot.getStack();

      if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack, slotStack)) {
        int j = slotStack.getCount() + stack.getCount();
        int maxSize = slot.getItemStackLimit(slotStack);

        if (j <= maxSize) {
          stack.setCount(0);
          slotStack.setCount(j);
          slot.onSlotChanged();
          flag = true;
        } else if (slotStack.getCount() < maxSize) {
          stack.shrink(maxSize - slotStack.getCount());
          slotStack.setCount(maxSize);
          slot.onSlotChanged();
          flag = true;
        }
      }

      i += (reverseDirection) ? -1 : 1;
    }

    if (!stack.isEmpty()) {
      if (reverseDirection) i = endIndex - 1;
      else i = startIndex;

      while (true) {
        if (reverseDirection) {
          if (i < startIndex) break;
        } else {
          if (i >= endIndex) break;
        }

        Slot slot1 = this.inventorySlots.get(i);
        ItemStack itemstack1 = slot1.getStack();

        if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
          if (stack.getCount() > slot1.getItemStackLimit(stack)) {
            slot1.putStack(stack.split(slot1.getItemStackLimit(stack)));
          } else {
            slot1.putStack(stack.split(stack.getCount()));
          }

          slot1.onSlotChanged();
          flag = true;
          break;
        }

        i += (reverseDirection) ? -1 : 1;
      }
    }

    return flag;
  }

  //need to override
  @Override
  public void detectAndSendChanges() {
    for (int i = 0; i < this.inventorySlots.size(); ++i) {
      ItemStack itemstack = (this.inventorySlots.get(i)).getStack();
      ItemStack itemstack1 = this.inventoryItemStacks.get(i);

      if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
        itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
        this.inventoryItemStacks.set(i, itemstack1);

        for (IContainerListener listener : this.listeners) {
          if (listener instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) listener;
            this.syncSlot(player, i, itemstack1);
          }
        }
      }

      for (int j = 0; j < this.trackedIntReferences.size(); ++j) {
        IntReferenceHolder intreferenceholder = this.trackedIntReferences.get(j);
        if (intreferenceholder.isDirty()) {
          for (IContainerListener icontainerlistener1 : this.listeners) {
            icontainerlistener1.sendWindowProperty(this, j, intreferenceholder.get());
          }
        }
      }
    }
  }

  @Override
  public void addListener(IContainerListener listener) {
    if (this.listeners.contains(listener)) {
      throw new IllegalArgumentException("Listener already listening");
    } else {
      this.listeners.add(listener);
      if (listener instanceof ServerPlayerEntity) {
        ServerPlayerEntity player = (ServerPlayerEntity) listener;
        this.syncInventory(player);
      }
      this.detectAndSendChanges();
    }
  }

  public void syncInventory(ServerPlayerEntity player) {
    for (int i = 0; i < this.inventorySlots.size(); i++) {
      ItemStack stack = (this.inventorySlots.get(i)).getStack();
      Networking.sendTo(new ExtendedSlotContentsPacket(this.windowId, i, stack), player);
    }
    player.connection.sendPacket(new SSetSlotPacket(-1, -1, player.inventory.getItemStack()));
  }

  public void syncSlot(ServerPlayerEntity player, int slot, ItemStack stack) {
    Networking.sendTo(new ExtendedSlotContentsPacket(this.windowId, slot, stack), player);
  }

  @Override
  public RadiantChestTile getTile() {
    return tile;
  }

  @Override
  public RadiantChestInventory getTileInventory() {
    if (inventory == null) {
      inventory = ITileContainer.super.getTileInventory();
      if (inventory == null) {
        inventory = RadiantChestInventory.getEmpty();
      }
    }
    return inventory;
  }
}
