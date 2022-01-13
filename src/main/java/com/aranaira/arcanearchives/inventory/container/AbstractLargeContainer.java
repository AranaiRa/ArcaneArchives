package com.aranaira.arcanearchives.inventory.container;

import com.aranaira.arcanearchives.api.inventory.slot.CappedSlot;
import com.aranaira.arcanearchives.init.ModBlocks;
import com.aranaira.arcanearchives.inventory.slot.RadiantChestSlot;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.packets.ExtendedSlotContentsPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import noobanidus.libs.noobutil.block.entities.IInventoryBlockEntity;
import noobanidus.libs.noobutil.container.IBlockEntityContainer;
import noobanidus.libs.noobutil.container.IPartitionedPlayerContainer;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import javax.annotation.Nullable;
import java.util.List;

/* Contains some code taken from Dank Storage by tfarecnim
Licensed under a CC0 license but used with permission
https://github.com/Tfarcenim/Dank-Storage/blob/1.16.x/src/main/java/tfar/dankstorage/inventory/DankSlot.java
 */
// TODO:
// - Add slots to ignore for transfer stack in slot
public abstract class AbstractLargeContainer<V extends ILargeInventory, T extends IInventoryBlockEntity<V>> extends Container implements IPartitionedPlayerContainer, IBlockEntityContainer<V, T> {
  protected final PlayerInventory player;
  protected T blockEntity;
  protected V inventory;
  protected final int rows;
  protected final boolean clientSide;
  protected IWorldPosCallable access;

  public AbstractLargeContainer(ContainerType<?> container, int id, int rows, PlayerInventory playerInventory, IWorldPosCallable access) {
    super(container, id);
    this.player = playerInventory;
    this.rows = rows;
    this.clientSide = false;
    this.access = access;
  }

  public AbstractLargeContainer(ContainerType<?> container, int id, int rows, PlayerInventory playerInventory, BlockPos position) {
    super(container, id);
    this.player = playerInventory;
    this.rows = rows;
    this.clientSide = true;
    this.access = IWorldPosCallable.create(playerInventory.player.level, position);
  }

  public boolean isClientSide() {
    return clientSide;
  }

  protected abstract T resolveBlockEntity(TileEntity be);

  // SHOULD BE OVERRIDDEN
  // BECAUSE I DON'T KNOW HOW OFFSETS WORK
  protected void createPlayerSlots(int yOffset1, int yOffset2, int xOffset1) {
    createPlayerSlots(yOffset1, yOffset2, xOffset1, null);
  }

  protected void createPlayerSlots(int yOffset1, int yOffset2, int xOffset1, @Nullable List<Slot> playerSlotStorage) {
    for (int row = 0; row < 3; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = xOffset1 + col * 18;
        int y = row * 18 + yOffset1;
        Slot slot = new CappedSlot(this.player, col + row * 9 + 9, x, y);
        this.addSlot(slot);
        if (playerSlotStorage != null) {
          playerSlotStorage.add(slot);
        }
      }
    }

    for (int row = 0; row < 9; ++row) {
      int x = xOffset1 + row * 18;
      int y = yOffset2;
      Slot slot = new CappedSlot(this.player, row, x, y);
      this.addSlot(slot);
      if (playerSlotStorage != null) {
        playerSlotStorage.add(slot);
      }
    }
  }

  protected abstract void createInventorySlots();

  @Override
  public PlayerEntity getPlayer() {
    return player.player;
  }

  @Override
  public boolean stillValid(PlayerEntity playerIn) {
    return stillValid(this.access, playerIn, ModBlocks.CRYSTAL_WORKBENCH.get());
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();

      if (index < rows * 9) {
        if (!this.moveItemStackTo(itemstack1, rows * 9, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 0, rows * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }

      slot.onTake(playerIn, itemstack1);
    }
    return itemstack;
  }

  @Override
  public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    ItemStack itemstack = ItemStack.EMPTY;

    if (clickTypeIn == ClickType.QUICK_CRAFT) {
      int oldDragEvent = this.quickcraftStatus;
      this.quickcraftStatus = getQuickcraftHeader(dragType);

      if ((oldDragEvent != 1 || this.quickcraftStatus != 2) && oldDragEvent != this.quickcraftStatus) {
        this.resetQuickCraft();
      } else if (this.player.getCarried().isEmpty()) {
        this.resetQuickCraft();
      } else if (this.quickcraftStatus == 0) { // Start drag
        this.quickcraftType = getQuickcraftType(dragType);

        if (isValidQuickcraftType(this.quickcraftType, player)) {
          this.quickcraftStatus = 1;
          this.quickcraftSlots.clear();
        } else {
          this.resetQuickCraft();
        }
      } else if (this.quickcraftStatus == 1) { // Add slot
        Slot addedSlot = this.slots.get(slotId);
        ItemStack mouseStack = this.player.getCarried();

        // TODO: Check if this needs to be replaced
        if (addedSlot != null && canItemQuickReplace(addedSlot, mouseStack, true) && addedSlot.mayPlace(mouseStack) && (this.quickcraftType == 2 || mouseStack.getCount() > this.quickcraftSlots.size()) && this.canDragTo(addedSlot)) {
          this.quickcraftSlots.add(addedSlot);
        }
      } else if (this.quickcraftStatus == 2) { // End drag
        if (!this.quickcraftSlots.isEmpty()) {
          ItemStack mouseStackCopy = this.player.getCarried().copy();
          int mouseCount = this.player.getCarried().getCount();

          for (Slot dragSlot : this.quickcraftSlots) {
            ItemStack mouseStack = this.player.getCarried();

            // TODO: Check if this needs to be replaced
            if (dragSlot != null && canItemQuickReplace(dragSlot, mouseStack, true) && dragSlot.mayPlace(mouseStack) && (this.quickcraftType == 2 || mouseStack.getCount() >= this.quickcraftSlots.size()) && this.canDragTo(dragSlot)) {
              ItemStack dragCopy = mouseStackCopy.copy();
              int initialDragCount = dragSlot.hasItem() ? dragSlot.getItem().getCount() : 0;
              getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, dragCopy, initialDragCount);
              int slotLimit = dragSlot.getMaxStackSize(dragCopy);

              if (dragCopy.getCount() > slotLimit) {
                dragCopy.setCount(slotLimit);
              }

              mouseCount -= dragCopy.getCount() - initialDragCount;
              dragSlot.set(dragCopy);
            }
          }

          mouseStackCopy.setCount(mouseCount);
          this.player.setCarried(mouseStackCopy);
        }

        this.resetQuickCraft();
      } else {
        this.resetQuickCraft();
      }
    } else if (this.quickcraftStatus != 0) {
      this.resetQuickCraft();
    } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
      if (slotId == -999) {
        if (!this.player.getCarried().isEmpty()) {
          if (dragType == 0) {
            player.drop(this.player.getCarried(), true);
            this.player.setCarried(ItemStack.EMPTY);
          }

          if (dragType == 1) {
            player.drop(this.player.getCarried().split(1), true);
          }
        }
      } else if (clickTypeIn == ClickType.QUICK_MOVE) {
        if (slotId < 0) {
          return ItemStack.EMPTY;
        }

        Slot transferSlot = this.slots.get(slotId);

        if (transferSlot == null || !transferSlot.mayPickup(player)) {
          return ItemStack.EMPTY;
        }

        for (ItemStack transferStack = this.quickMoveStack(player, slotId);
             !transferStack.isEmpty() && ItemStack.isSame(transferSlot.getItem(), transferStack); transferStack = this.quickMoveStack(player, slotId)) {
          itemstack = transferStack.copy();
        }
      } else {
        if (slotId < 0) {
          return ItemStack.EMPTY;
        }

        Slot clickedSlot = this.slots.get(slotId);

        if (clickedSlot != null) {
          ItemStack slotStack = clickedSlot.getItem();
          ItemStack mouseStack = this.player.getCarried();

          if (!slotStack.isEmpty()) {
            itemstack = slotStack.copy();
          }

          if (slotStack.isEmpty()) {
            if (!mouseStack.isEmpty() && clickedSlot.mayPlace(mouseStack)) {
              int splitCount = dragType == 0 ? mouseStack.getCount() : 1;

              if (splitCount > clickedSlot.getMaxStackSize(mouseStack)) {
                splitCount = clickedSlot.getMaxStackSize(mouseStack);
              }

              clickedSlot.set(mouseStack.split(splitCount));
            }
          } else if (clickedSlot.mayPickup(player)) {
            if (mouseStack.isEmpty()) {
              if (slotStack.isEmpty()) {
                clickedSlot.set(ItemStack.EMPTY);
                this.player.setCarried(ItemStack.EMPTY);
              } else {
                int toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                if (clickedSlot instanceof RadiantChestSlot && slotStack.getMaxStackSize() < slotStack.getCount()) {
                  toMove = dragType == 0 ? slotStack.getMaxStackSize() : (slotStack.getMaxStackSize() + 1) / 2;
                }
                this.player.setCarried(clickedSlot.remove(toMove));

                if (slotStack.isEmpty()) {
                  clickedSlot.set(ItemStack.EMPTY);
                }

                clickedSlot.onTake(player, this.player.getCarried());
              }
            } else if (clickedSlot.mayPlace(mouseStack)) {
              if (slotStack.getItem() == mouseStack.getItem() && ItemStack.tagMatches(slotStack, mouseStack)) {
                int k2 = dragType == 0 ? mouseStack.getCount() : 1;

                if (k2 > clickedSlot.getMaxStackSize(mouseStack) - slotStack.getCount()) {
                  k2 = clickedSlot.getMaxStackSize(mouseStack) - slotStack.getCount();
                }

                mouseStack.shrink(k2);
                slotStack.grow(k2);
              } else if (mouseStack.getCount() <= clickedSlot.getMaxStackSize(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                clickedSlot.set(mouseStack);
                this.player.setCarried(slotStack);
              }
            } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.tagMatches(slotStack, mouseStack) && !slotStack.isEmpty()) {
              int j2 = slotStack.getCount();

              if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                mouseStack.grow(j2);
                slotStack = clickedSlot.remove(j2);

                if (slotStack.isEmpty()) {
                  clickedSlot.set(ItemStack.EMPTY);
                }

                clickedSlot.onTake(player, this.player.getCarried());
              }
            }
          }

          clickedSlot.setChanged();
        }
      }
    } else if (clickTypeIn == ClickType.CLONE && player.abilities.instabuild && this.player.getCarried().isEmpty() && slotId >= 0) {
      Slot cloneSlot = this.slots.get(slotId);

      if (cloneSlot != null && cloneSlot.hasItem()) {
        ItemStack clonedStack = cloneSlot.getItem().copy();
        clonedStack.setCount(clonedStack.getMaxStackSize());
        this.player.setCarried(clonedStack);
      }
    } else if (clickTypeIn == ClickType.THROW && this.player.getCarried().isEmpty() && slotId >= 0) {
      Slot throwSlot = this.slots.get(slotId);

      if (throwSlot != null && throwSlot.hasItem() && throwSlot.mayPickup(player)) {
        ItemStack thrownSlot = throwSlot.remove(dragType == 0 ? 1 : throwSlot.getItem().getCount());
        throwSlot.onTake(player, thrownSlot);
        player.drop(thrownSlot, true);
      }
    } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
      Slot pickupSlot = this.slots.get(slotId);
      ItemStack mouseStack = this.player.getCarried();

      if (!mouseStack.isEmpty() && (pickupSlot == null || !pickupSlot.hasItem() || !pickupSlot.mayPickup(player))) {
        int i = dragType == 0 ? 0 : this.slots.size() - 1;
        int j = dragType == 0 ? 1 : -1;

        for (int k = 0; k < 2; ++k) {
          for (int l = i; l >= 0 && l < this.slots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
            Slot slot1 = this.slots.get(l);

            if (slot1.hasItem() && canItemQuickReplace(slot1, mouseStack, true) && slot1.mayPickup(player) && this.canTakeItemForPickAll(mouseStack, slot1)) {
              ItemStack itemstack2 = slot1.getItem();

              if (k != 0 || itemstack2.getCount() < slot1.getMaxStackSize(itemstack2)) {
                int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
                ItemStack itemstack3 = slot1.remove(i1);
                mouseStack.grow(i1);

                if (itemstack3.isEmpty()) {
                  slot1.set(ItemStack.EMPTY);
                }

                slot1.onTake(player, itemstack3);
              }
            }
          }
        }
      }

      this.broadcastChanges();
    }

    if (itemstack.getCount() > 64) {
      itemstack = itemstack.copy();
      itemstack.setCount(64);
    }

    return itemstack;
  }

  @Override
  protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
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

      Slot slot = this.slots.get(i);
      ItemStack slotStack = slot.getItem();

      if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ItemStack.tagMatches(stack, slotStack)) {
        int j = slotStack.getCount() + stack.getCount();
        int maxSize = slot.getMaxStackSize(slotStack);

        if (j <= maxSize) {
          stack.setCount(0);
          slotStack.setCount(j);
          slot.setChanged();
          flag = true;
        } else if (slotStack.getCount() < maxSize) {
          stack.shrink(maxSize - slotStack.getCount());
          slotStack.setCount(maxSize);
          slot.setChanged();
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

        Slot slot1 = this.slots.get(i);
        ItemStack itemstack1 = slot1.getItem();

        if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
          if (stack.getCount() > slot1.getMaxStackSize(stack)) {
            try {
              slot1.set(stack.split(slot1.getMaxStackSize(stack)));
            } catch (ArrayIndexOutOfBoundsException e) {
              throw e;
            }
          } else {
            slot1.set(stack.split(stack.getCount()));
          }

          slot1.setChanged();
          flag = true;
          break;
        }

        i += (reverseDirection) ? -1 : 1;
      }
    }

    return flag;
  }

  protected boolean skipSlot(int i) {
    return skipSlot(this.slots.get(i));
  }

  protected boolean skipSlot(Slot slot) {
    return false;
  }

  //need to override
  @Override
  public void broadcastChanges() {
    for (int i = 0; i < this.slots.size(); ++i) {
      if (!skipSlot(i)) {
        ItemStack itemstack = (this.slots.get(i)).getItem();
        ItemStack itemstack1 = this.lastSlots.get(i);

        if (!ItemStack.matches(itemstack1, itemstack)) {
          itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
          this.lastSlots.set(i, itemstack1);

          for (IContainerListener listener : this.containerListeners) {
            if (listener instanceof ServerPlayerEntity) {
              ServerPlayerEntity player = (ServerPlayerEntity) listener;
              this.syncSlot(player, i, itemstack1);
            } else {
              listener.slotChanged(this, i, itemstack1);
            }
          }
        }
      }
    }

    for (int j = 0; j < this.dataSlots.size(); ++j) {
      IntReferenceHolder intreferenceholder = this.dataSlots.get(j);
      if (intreferenceholder.checkAndClearUpdateFlag()) {
        for (IContainerListener icontainerlistener1 : this.containerListeners) {
          icontainerlistener1.setContainerData(this, j, intreferenceholder.get());
        }
      }
    }
  }

  @Override
  public void addSlotListener(IContainerListener listener) {
    if (this.containerListeners.contains(listener)) {
      throw new IllegalArgumentException("Listener already listening");
    } else {
      this.containerListeners.add(listener);
      if (listener instanceof ServerPlayerEntity) {
        ServerPlayerEntity player = (ServerPlayerEntity) listener;
        this.syncInventory(player);
      }
      this.broadcastChanges();
    }
  }

  public void syncInventory(ServerPlayerEntity player) {
    for (int i = 0; i < this.slots.size(); i++) {
      if (skipSlot(i)) {
        continue;
      }
      ItemStack stack = (this.slots.get(i)).getItem();
      Networking.sendTo(new ExtendedSlotContentsPacket(this.containerId, i, stack), player);
    }
    player.connection.send(new SSetSlotPacket(-1, -1, player.inventory.getCarried()));
  }

  public void syncSlot(ServerPlayerEntity player, int slot, ItemStack stack) {
    if (!skipSlot(slot)) {
      Networking.sendTo(new ExtendedSlotContentsPacket(this.containerId, slot, stack), player);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T getBlockEntity() {
    if (blockEntity == null) {
      this.access.execute((level, pos) -> {
        blockEntity = (T) level.getBlockEntity(pos);
      });
    }
    return blockEntity;
  }

  @Override
  public V getBlockEntityInventory() {
    if (inventory == null) {
      inventory = IBlockEntityContainer.super.getBlockEntityInventory();
      if (inventory == null) {
        inventory = getEmptyInventory();
      }
    }
    return inventory;
  }
}
