package com.aranaira.arcanearchives.containers;

import com.aranaira.arcanearchives.containers.slots.SlotExtended;
import com.aranaira.arcanearchives.inventories.TrackingExtendedHandler;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketRadiantChest;
import com.aranaira.arcanearchives.tileentities.RadiantChestTile;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

import javax.annotation.Nullable;
import java.util.Set;

//import invtweaks.api.container.ChestContainer;

//@ChestContainer(isLargeChest = true)
@SuppressWarnings("NullableProblems")
public class ContainerRadiantChest extends Container {
  protected RadiantChestTile tile;
  private int dragMode = -1;
  private int dragEvent;
  private final Set<Slot> dragSlots = Sets.newHashSet();

  public ContainerRadiantChest(RadiantChestTile te, EntityPlayer player) {
    this.tile = te;
    addOwnSlots();
    addPlayerSlots(player.inventory);
  }

  public RadiantChestTile getTile() {
    return this.tile;
  }

  private void addPlayerSlots(IInventory playerinventory) {
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = 16 + col * 18;
        int y = row * 18 + 142;
        this.addSlotToContainer(new Slot(playerinventory, col + row * 9 + 9, x, y) {
          @Override
          public int getItemStackLimit(ItemStack stack) {
            return Math.min(this.getSlotStackLimit(), stack.getMaxStackSize());
          }
        });
      }
    }

    for (int row = 0; row < 9; ++row) {
      int x = 16 + row * 18;
      int y = 200;
      this.addSlotToContainer(new Slot(playerinventory, row, x, y) {
        @Override
        public int getItemStackLimit(ItemStack stack) {
          return Math.min(this.getSlotStackLimit(), stack.getMaxStackSize());
        }
      });
    }
  }

  private void addOwnSlots() {
    TrackingExtendedHandler handler = this.tile.getInventory();
    int slotIndex = 0;
    for (int row = 0; row < 6; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = 16 + col * 18;
        int y = row * 18 + 16;
        this.addSlotToContainer(new SlotExtended(handler, slotIndex, x, y));
        slotIndex++;
      }
    }
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack inSlotCopy = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack inSlot = slot.getStack();
      inSlotCopy = inSlot.copy();

      if (index < 6 * 9) {
        if (!this.mergeItemStack(inSlot, 6 * 9, this.inventorySlots.size(), false)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(inSlot, 0, 6 * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (inSlot.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }

    return inSlotCopy;
  }

  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    ItemStack itemstack = ItemStack.EMPTY;
    InventoryPlayer inventoryplayer = player.inventory;

    if (clickTypeIn == ClickType.QUICK_CRAFT) {
      int drag = this.dragEvent;
      this.dragEvent = getDragEvent(dragType);

      if ((drag != 1 || this.dragEvent != 2) && drag != this.dragEvent) {
        this.resetDrag();
      } else if (inventoryplayer.getItemStack().isEmpty()) {
        this.resetDrag();
      } else if (this.dragEvent == 0) {
        this.dragMode = extractDragMode(dragType);

        if (isValidDragMode(this.dragMode, player)) {
          this.dragEvent = 1;
          this.dragSlots.clear();
        } else {
          this.resetDrag();
        }
      } else if (this.dragEvent == 1) {
        Slot slot = this.inventorySlots.get(slotId);
        ItemStack mouseStack = inventoryplayer.getItemStack();

        if (slot != null && ContainerRadiantChest.canAddItemToSlot(slot, mouseStack, true) && slot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot)) {
          this.dragSlots.add(slot);
        }
      } else if (this.dragEvent == 2) {
        if (!this.dragSlots.isEmpty()) {
          ItemStack mouseStackCopy = inventoryplayer.getItemStack().copy();
          int heldCount = inventoryplayer.getItemStack().getCount();

          for (Slot dragSlot : this.dragSlots) {
            ItemStack mouseStack = inventoryplayer.getItemStack();

            if (dragSlot != null && ContainerRadiantChest.canAddItemToSlot(dragSlot, mouseStack, true) && dragSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(dragSlot)) {
              ItemStack mouseStackCopyCopy = mouseStackCopy.copy();
              int j3 = dragSlot.getHasStack() ? dragSlot.getStack().getCount() : 0;
              computeStackSize(this.dragSlots, this.dragMode, mouseStackCopyCopy, j3);
              int k3 = dragSlot.getItemStackLimit(mouseStackCopyCopy);

              if (mouseStackCopyCopy.getCount() > k3) {
                mouseStackCopyCopy.setCount(k3);
              }

              heldCount -= mouseStackCopyCopy.getCount() - j3;
              dragSlot.putStack(mouseStackCopyCopy);
            }
          }

          mouseStackCopy.setCount(heldCount);
          inventoryplayer.setItemStack(mouseStackCopy);
        }

        this.resetDrag();
      } else {
        this.resetDrag();
      }
    } else if (this.dragEvent != 0) {
      this.resetDrag();
    } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
      if (slotId == -999) {
        if (!inventoryplayer.getItemStack().isEmpty()) {
          if (dragType == 0) {
            player.dropItem(inventoryplayer.getItemStack(), true);
            inventoryplayer.setItemStack(ItemStack.EMPTY);
          }

          if (dragType == 1) {
            player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
          }
        }
      } else if (clickTypeIn == ClickType.QUICK_MOVE) {
        if (slotId < 0) {
          return ItemStack.EMPTY;
        }

        Slot slot = this.inventorySlots.get(slotId);

        if (slot == null || !slot.canTakeStack(player)) {
          return ItemStack.EMPTY;
        }

        for (ItemStack transferStack = this.transferStackInSlot(player, slotId); !transferStack.isEmpty() && ItemStack.areItemsEqual(slot.getStack(), transferStack); transferStack = this.transferStackInSlot(player, slotId)) {
          itemstack = transferStack.copy();
        }
      } else {
        if (slotId < 0) {
          return ItemStack.EMPTY;
        }

        Slot slot = this.inventorySlots.get(slotId);

        if (slot != null) {
          ItemStack slotStack = slot.getStack();
          ItemStack mouseStack = inventoryplayer.getItemStack();

          if (!slotStack.isEmpty()) {
            itemstack = slotStack.copy();
          }

          if (slotStack.isEmpty()) {
            if (!mouseStack.isEmpty() && slot.isItemValid(mouseStack)) {
              int count = dragType == 0 ? mouseStack.getCount() : 1;

              if (count > slot.getItemStackLimit(mouseStack)) {
                count = slot.getItemStackLimit(mouseStack);
              }

              slot.putStack(mouseStack.splitStack(count));
            }
          } else if (slot.canTakeStack(player)) {
            if (mouseStack.isEmpty()) {
              if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
                inventoryplayer.setItemStack(ItemStack.EMPTY);
              } else {
                int slotCount = Math.min(slotStack.getMaxStackSize(), slotStack.getCount());
                int count = dragType == 0 ? slotCount : (slotCount + 1) / 2;
                inventoryplayer.setItemStack(slot.decrStackSize(count));

                if (slotStack.isEmpty()) {
                  slot.putStack(ItemStack.EMPTY);
                }

                slot.onTake(player, inventoryplayer.getItemStack());
              }
            } else if (slot.isItemValid(mouseStack)) {
              if (slotStack.getItem() == mouseStack.getItem() && slotStack.getMetadata() == mouseStack.getMetadata() && ItemStack.areItemStackTagsEqual(slotStack, mouseStack)) {
                int amount = dragType == 0 ? mouseStack.getCount() : 1;

                if (amount > slot.getItemStackLimit(mouseStack) - slotStack.getCount()) {
                  amount = slot.getItemStackLimit(mouseStack) - slotStack.getCount();
                }

                mouseStack.shrink(amount);
                slotStack.grow(amount);
              } else if (mouseStack.getCount() <= slot.getItemStackLimit(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                slot.putStack(mouseStack);
                inventoryplayer.setItemStack(slotStack);
              }
            } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && (!slotStack.getHasSubtypes() || slotStack.getMetadata() == mouseStack.getMetadata()) && ItemStack.areItemStackTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty()) {
              int slotCount = slotStack.getCount();

              if (slotCount + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                mouseStack.grow(slotCount);
                slotStack = slot.decrStackSize(slotCount);

                if (slotStack.isEmpty()) {
                  slot.putStack(ItemStack.EMPTY);
                }

                slot.onTake(player, inventoryplayer.getItemStack());
              }
            }
          }

          slot.onSlotChanged();
        }
      }
    } else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
      Slot slot = this.inventorySlots.get(slotId);
      ItemStack playerStack = inventoryplayer.getStackInSlot(dragType);
      ItemStack slotStack = slot.getStack();

      if (!playerStack.isEmpty() || !slotStack.isEmpty()) {
        if (playerStack.isEmpty()) {
          if (slot.canTakeStack(player)) {
            int maxAmount = slotStack.getMaxStackSize();
            if (slotStack.getCount() > maxAmount) {
              ItemStack newSlotStack = slotStack.copy();
              ItemStack takenStack = newSlotStack.splitStack(maxAmount);
              inventoryplayer.setInventorySlotContents(dragType, takenStack);
              slot.putStack(newSlotStack);
              slot.onTake(player, takenStack);
            } else {
              inventoryplayer.setInventorySlotContents(dragType, slotStack);
              slot.putStack(ItemStack.EMPTY);
              slot.onTake(player, slotStack);
            }
          }
        } else if (slotStack.isEmpty()) {
          if (slot.isItemValid(playerStack)) {
            int playerStackCount = slot.getItemStackLimit(playerStack);

            if (playerStack.getCount() > playerStackCount) {
              slot.putStack(playerStack.splitStack(playerStackCount));
            } else {
              slot.putStack(playerStack);
              inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
            }
          }
        } else if (slot.canTakeStack(player) && slot.isItemValid(playerStack)) {
          int playerStackCount = slot.getItemStackLimit(playerStack);

          if (playerStack.getCount() > playerStackCount) {
            slot.putStack(playerStack.splitStack(playerStackCount));
            slot.onTake(player, slotStack);

            if (!inventoryplayer.addItemStackToInventory(slotStack)) {
              player.dropItem(slotStack, true);
            }
          } else {
            slot.putStack(playerStack);
            if (slotStack.getCount() > slotStack.getMaxStackSize()) {
              ItemStack remainder = slotStack.copy();
              inventoryplayer.setInventorySlotContents(dragType, remainder.splitStack(slotStack.getMaxStackSize()));
              if (!inventoryplayer.addItemStackToInventory(remainder)) {
                player.dropItem(remainder, true);
              }
            } else {
              inventoryplayer.setInventorySlotContents(dragType, slotStack);
            }
            slot.onTake(player, slotStack);
          }
        }
      }
    } else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
      Slot slot = this.inventorySlots.get(slotId);

      if (slot != null && slot.getHasStack()) {
        ItemStack slotStack = slot.getStack().copy();
        slotStack.setCount(slotStack.getMaxStackSize());
        inventoryplayer.setItemStack(slotStack);
      }
    } else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
      Slot slot = this.inventorySlots.get(slotId);

      if (slot != null && slot.getHasStack() && slot.canTakeStack(player)) {
        ItemStack slotModifiedStack = slot.decrStackSize(dragType == 0 ? 1 : slot.getStack().getCount());
        slot.onTake(player, slotModifiedStack);
        if (slotModifiedStack.getCount() > 64) {
          int count = slotModifiedStack.getCount();
          int increment = slotModifiedStack.getMaxStackSize();
          while (count > 0) {
            ItemStack copy = slotModifiedStack.copy();
            copy.setCount(Math.min(count, increment));
            count -= increment;
            player.dropItem(copy, true);
          }
        } else {
          player.dropItem(slotModifiedStack, true);
        }
      }
    } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
      Slot slot = this.inventorySlots.get(slotId);
      ItemStack mouseStack = inventoryplayer.getItemStack();

      if (!mouseStack.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
        int slotMin = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
        int step = dragType == 0 ? 1 : -1;

        for (int k = 0; k < 2; ++k) {
          for (int l = slotMin; l >= 0 && l < this.inventorySlots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += step) {
            Slot thisSlot = this.inventorySlots.get(l);

            if (thisSlot.getHasStack() && ContainerRadiantChest.canAddItemToSlot(thisSlot, mouseStack, true) && thisSlot.canTakeStack(player) && this.canMergeSlot(mouseStack, thisSlot)) {
              ItemStack itemstack2 = thisSlot.getStack();

              if (k != 0 || itemstack2.getCount() < thisSlot.getItemStackLimit(itemstack2)) {
                int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
                ItemStack modifiedStack = thisSlot.decrStackSize(i1);
                mouseStack.grow(i1);

                if (modifiedStack.isEmpty()) {
                  thisSlot.putStack(ItemStack.EMPTY);
                }

                thisSlot.onTake(player, modifiedStack);
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
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }

  @Override
  protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    boolean flag = false;
    int index = startIndex;

    if (reverseDirection) {
      index = endIndex - 1;
    }

    while (!stack.isEmpty()) {
      if (reverseDirection) {
        if (index < startIndex) {
          break;
        }
      } else {
        if (index >= endIndex) {
          break;
        }
      }

      Slot slot = this.inventorySlots.get(index);
      ItemStack inSlot = slot.getStack();

      if (!inSlot.isEmpty() && inSlot.getItem() == stack.getItem() && (stack.getMetadata() == inSlot.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, inSlot)) {
        int combinedCount = inSlot.getCount() + stack.getCount();
        int maxSize = slot.getItemStackLimit(inSlot);

        if (combinedCount <= maxSize) {
          stack.setCount(0);
          inSlot.setCount(combinedCount);
          slot.onSlotChanged();
          flag = true;
        } else if (inSlot.getCount() < maxSize) {
          stack.shrink(maxSize - inSlot.getCount());
          inSlot.setCount(maxSize);
          slot.onSlotChanged();
          flag = true;
        }
      }

      index += (reverseDirection) ? -1 : 1;
    }

    if (!stack.isEmpty()) {
      if (reverseDirection) {
        index = endIndex - 1;
      } else {
        index = startIndex;
      }

      while (true) {
        if (reverseDirection) {
          if (index < startIndex) {
            break;
          }
        } else {
          if (index >= endIndex) {
            break;
          }
        }

        Slot slot = this.inventorySlots.get(index);
        ItemStack inSlot = slot.getStack();

        if (inSlot.isEmpty() && slot.isItemValid(stack)) {
          if (stack.getCount() > slot.getItemStackLimit(stack)) {
            slot.putStack(stack.splitStack(slot.getItemStackLimit(stack)));
          } else {
            slot.putStack(stack.splitStack(stack.getCount()));
          }

          slot.onSlotChanged();
          flag = true;
          break;
        }

        index += (reverseDirection) ? -1 : 1;
      }
    }

    return flag;
  }

  @Override
  protected void resetDrag() {
    this.dragEvent = 0;
    this.dragSlots.clear();
  }

  public static boolean canAddItemToSlot(@Nullable Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
    boolean flag = slotIn == null || !slotIn.getHasStack();
    ItemStack slotStack = slotIn == null ? ItemStack.EMPTY : slotIn.getStack();

    if (!flag && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(slotStack, stack)) {
      return slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slotIn.getItemStackLimit(slotStack);
    }

    return flag;
  }

  @Override
  public void detectAndSendChanges() {
    for (int i = 0; i < this.inventorySlots.size(); ++i) {
      ItemStack slotItem = (this.inventorySlots.get(i)).getStack();
      ItemStack inventoryItem = this.inventoryItemStacks.get(i);

      if (!ItemStack.areItemStacksEqual(inventoryItem, slotItem)) {
        inventoryItem = slotItem.isEmpty() ? ItemStack.EMPTY : slotItem.copy();
        this.inventoryItemStacks.set(i, inventoryItem);

        for (IContainerListener listener : this.listeners) {
          if (listener instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) listener;

            this.syncSlot(player, i, inventoryItem);
          } else {
            listener.sendSlotContents(this, i, inventoryItem);
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
      if (listener instanceof EntityPlayerMP) {
        EntityPlayerMP player = (EntityPlayerMP) listener;

        this.syncInventory(player);
      }
      this.detectAndSendChanges();
    }
  }

  public void syncInventory(EntityPlayerMP player) {
    for (int i = 0; i < this.inventorySlots.size(); i++) {
      if (this.inventorySlots.get(i) instanceof SlotExtended) {
        ItemStack stack = (this.inventorySlots.get(i)).getStack();

        Networking.CHANNEL.sendTo(new PacketRadiantChest.MessageSyncExtendedSlotContents(this.windowId, i, stack), player);
      }
    }

    player.connection.sendPacket(new SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
  }

  private void syncSlot(EntityPlayerMP player, int slot, ItemStack stack) {
    if (getSlot(slot) instanceof SlotExtended) {
      Networking.CHANNEL.sendTo(new PacketRadiantChest.MessageSyncExtendedSlotContents(this.windowId, slot, stack), player);
    }
  }
}
