package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.slots.SlotExtended;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketRadiantChest.MessageSyncExtendedSlotContents;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity.TrackingExtendedItemStackHandler;
import com.google.common.collect.Sets;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

import javax.annotation.Nullable;
import java.util.Set;

@ChestContainer(isLargeChest = true)
public class ContainerRadiantChest extends Container {

	protected RadiantChestTileEntity tile;

	/**
	 * The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not used ?)
	 */
	protected int dragMode = -1;
	/**
	 * The current drag event (0 : start, 1 : add slot : 2 : end)
	 */
	protected int dragEvent;
	/**
	 * The list of slots where the itemstack holds will be distributed
	 */
	protected final Set<Slot> dragSlots = Sets.newHashSet();

	public ContainerRadiantChest (RadiantChestTileEntity te, EntityPlayer player) {
		this.tile = te;
		addOwnSlots();
		addPlayerSlots(player.inventory);
	}

	public RadiantChestTileEntity getTile () {
		return this.tile;
	}

	@Override
	public void onContainerClosed (EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	protected void addPlayerSlots (IInventory playerinventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 16 + col * 18;
				int y = row * 18 + 142;
				this.addSlotToContainer(new Slot(playerinventory, col + row * 9 + 9, x, y) {
					@Override
					public int getItemStackLimit (ItemStack stack) {
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
				public int getItemStackLimit (ItemStack stack) {
					return Math.min(this.getSlotStackLimit(), stack.getMaxStackSize());
				}
			});
		}
	}

	public void addOwnSlots () {
		TrackingExtendedItemStackHandler handler = this.tile.getInventory();
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

	@Nullable
	@Override
	public ItemStack transferStackInSlot (EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 6 * 9) {
				if (!this.mergeItemStack(itemstack1, 6 * 9, this.inventorySlots.size(), false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 6 * 9, false)) {
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
	public ItemStack slotClick (int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		ItemStack itemstack = ItemStack.EMPTY;
		InventoryPlayer inventoryplayer = player.inventory;

		if (clickTypeIn == ClickType.QUICK_CRAFT) {
			int j1 = this.dragEvent;
			this.dragEvent = getDragEvent(dragType);

			if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
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
				Slot slot7 = this.inventorySlots.get(slotId);
				ItemStack mouseStack = inventoryplayer.getItemStack();

				if (slot7 != null && ContainerRadiantChest.canAddItemToSlot(slot7, mouseStack, true) && slot7.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
					this.dragSlots.add(slot7);
				}
			} else if (this.dragEvent == 2) {
				if (!this.dragSlots.isEmpty()) {
					ItemStack mouseStackCopy = inventoryplayer.getItemStack().copy();
					int k1 = inventoryplayer.getItemStack().getCount();

					for (Slot dragSlot : this.dragSlots) {
						ItemStack mouseStack = inventoryplayer.getItemStack();

						if (dragSlot != null && ContainerRadiantChest.canAddItemToSlot(dragSlot, mouseStack, true) && dragSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(dragSlot)) {
							ItemStack itemstack14 = mouseStackCopy.copy();
							int j3 = dragSlot.getHasStack() ? dragSlot.getStack().getCount() : 0;
							computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
							int k3 = dragSlot.getItemStackLimit(itemstack14);

							if (itemstack14.getCount() > k3) {
								itemstack14.setCount(k3);
							}

							k1 -= itemstack14.getCount() - j3;
							dragSlot.putStack(itemstack14);
						}
					}

					mouseStackCopy.setCount(k1);
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

				Slot slot5 = this.inventorySlots.get(slotId);

				if (slot5 == null || !slot5.canTakeStack(player)) {
					return ItemStack.EMPTY;
				}

				for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId)) {
					itemstack = itemstack7.copy();
				}
			} else {
				if (slotId < 0) {
					return ItemStack.EMPTY;
				}

				Slot slot6 = this.inventorySlots.get(slotId);

				if (slot6 != null) {
					ItemStack slotStack = slot6.getStack();
					ItemStack mouseStack = inventoryplayer.getItemStack();

					if (!slotStack.isEmpty()) {
						itemstack = slotStack.copy();
					}

					if (slotStack.isEmpty()) {
						if (!mouseStack.isEmpty() && slot6.isItemValid(mouseStack)) {
							int i3 = dragType == 0 ? mouseStack.getCount() : 1;

							if (i3 > slot6.getItemStackLimit(mouseStack)) {
								i3 = slot6.getItemStackLimit(mouseStack);
							}

							slot6.putStack(mouseStack.splitStack(i3));
						}
					} else if (slot6.canTakeStack(player)) {
						if (mouseStack.isEmpty()) {
							if (slotStack.isEmpty()) {
								slot6.putStack(ItemStack.EMPTY);
								inventoryplayer.setItemStack(ItemStack.EMPTY);
							} else {
								int l2 = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
								inventoryplayer.setItemStack(slot6.decrStackSize(l2));

								if (slotStack.isEmpty()) {
									slot6.putStack(ItemStack.EMPTY);
								}

								slot6.onTake(player, inventoryplayer.getItemStack());
							}
						} else if (slot6.isItemValid(mouseStack)) {
							if (slotStack.getItem() == mouseStack.getItem() && slotStack.getMetadata() == mouseStack.getMetadata() && ItemStack.areItemStackTagsEqual(slotStack, mouseStack)) {
								int k2 = dragType == 0 ? mouseStack.getCount() : 1;

								if (k2 > slot6.getItemStackLimit(mouseStack) - slotStack.getCount()) {
									k2 = slot6.getItemStackLimit(mouseStack) - slotStack.getCount();
								}

								//if (k2 > mouseStack.getMaxStackSize() - slotStack.getCount()) {
								//    k2 = mouseStack.getMaxStackSize() - slotStack.getCount();
								//}

								mouseStack.shrink(k2);
								slotStack.grow(k2);
							} else if (mouseStack.getCount() <= slot6.getItemStackLimit(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
								slot6.putStack(mouseStack);
								inventoryplayer.setItemStack(slotStack);
							}
						} else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && (!slotStack.getHasSubtypes() || slotStack.getMetadata() == mouseStack.getMetadata()) && ItemStack.areItemStackTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty()) {
							int j2 = slotStack.getCount();

							if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
								mouseStack.grow(j2);
								slotStack = slot6.decrStackSize(j2);

								if (slotStack.isEmpty()) {
									slot6.putStack(ItemStack.EMPTY);
								}

								slot6.onTake(player, inventoryplayer.getItemStack());
							}
						}
					}

					slot6.onSlotChanged();
				}
			}
		} else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
			Slot slot4 = this.inventorySlots.get(slotId);
			ItemStack itemstack6 = inventoryplayer.getStackInSlot(dragType);
			ItemStack slotStack = slot4.getStack();

			if (!itemstack6.isEmpty() || !slotStack.isEmpty()) {
				if (itemstack6.isEmpty()) {
					if (slot4.canTakeStack(player)) {
						int maxAmount = slotStack.getMaxStackSize();
						if (slotStack.getCount() > maxAmount) {
							ItemStack newSlotStack = slotStack.copy();
							ItemStack takenStack = newSlotStack.splitStack(maxAmount);
							inventoryplayer.setInventorySlotContents(dragType, takenStack);
							//slot4.onSwapCraft(takenStack.getCount());
							slot4.putStack(newSlotStack);
							slot4.onTake(player, takenStack);
						} else {
							inventoryplayer.setInventorySlotContents(dragType, slotStack);
							//slot4.onSwapCraft(slotStack.getCount());
							slot4.putStack(ItemStack.EMPTY);
							slot4.onTake(player, slotStack);
						}
					}
				} else if (slotStack.isEmpty()) {
					if (slot4.isItemValid(itemstack6)) {
						int l1 = slot4.getItemStackLimit(itemstack6);

						if (itemstack6.getCount() > l1) {
							slot4.putStack(itemstack6.splitStack(l1));
						} else {
							slot4.putStack(itemstack6);
							inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
						}
					}
				} else if (slot4.canTakeStack(player) && slot4.isItemValid(itemstack6)) {
					int i2 = slot4.getItemStackLimit(itemstack6);

					if (itemstack6.getCount() > i2) {
						slot4.putStack(itemstack6.splitStack(i2));
						slot4.onTake(player, slotStack);

						if (!inventoryplayer.addItemStackToInventory(slotStack)) {
							player.dropItem(slotStack, true);
						}
					} else {
						slot4.putStack(itemstack6);
						if (slotStack.getCount() > slotStack.getMaxStackSize()) {
							ItemStack remainder = slotStack.copy();
							inventoryplayer.setInventorySlotContents(dragType, remainder.splitStack(slotStack.getMaxStackSize()));
							if (!inventoryplayer.addItemStackToInventory(remainder)) {
								player.dropItem(remainder, true);
							}
						} else {
							inventoryplayer.setInventorySlotContents(dragType, slotStack);
						}
						slot4.onTake(player, slotStack);
					}
				}
			}
		} else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
			Slot slot3 = this.inventorySlots.get(slotId);

			if (slot3 != null && slot3.getHasStack()) {
				ItemStack itemstack5 = slot3.getStack().copy();
				itemstack5.setCount(itemstack5.getMaxStackSize());
				inventoryplayer.setItemStack(itemstack5);
			}
		} else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
			Slot slot2 = this.inventorySlots.get(slotId);

			if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
				ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
				slot2.onTake(player, itemstack4);
				player.dropItem(itemstack4, true);
			}
		} else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
			Slot slot = this.inventorySlots.get(slotId);
			ItemStack mouseStack = inventoryplayer.getItemStack();

			if (!mouseStack.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player))) {
				int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
				int j = dragType == 0 ? 1 : -1;

				for (int k = 0; k < 2; ++k) {
					for (int l = i; l >= 0 && l < this.inventorySlots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
						Slot slot1 = this.inventorySlots.get(l);

						if (slot1.getHasStack() && ContainerRadiantChest.canAddItemToSlot(slot1, mouseStack, true) && slot1.canTakeStack(player) && this.canMergeSlot(mouseStack, slot1)) {
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
	public boolean canInteractWith (EntityPlayer playerIn) {
		return true;
	}

	@Override
	protected boolean mergeItemStack (ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection) {
			i = endIndex - 1;
		}

		while (!stack.isEmpty()) {
			if (reverseDirection) {
				if (i < startIndex) {
					break;
				}
			} else {
				if (i >= endIndex) {
					break;
				}
			}

			Slot slot = this.inventorySlots.get(i);
			ItemStack itemstack = slot.getStack();

			if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
				int j = itemstack.getCount() + stack.getCount();
				int maxSize = slot.getItemStackLimit(itemstack);

				if (j <= maxSize) {
					stack.setCount(0);
					itemstack.setCount(j);
					slot.onSlotChanged();
					flag = true;
				} else if (itemstack.getCount() < maxSize) {
					stack.shrink(maxSize - itemstack.getCount());
					itemstack.setCount(maxSize);
					slot.onSlotChanged();
					flag = true;
				}
			}

			i += (reverseDirection) ? -1 : 1;
		}

		if (!stack.isEmpty()) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while (true) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else {
					if (i >= endIndex) {
						break;
					}
				}

				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
					if (stack.getCount() > slot1.getItemStackLimit(stack)) {
						slot1.putStack(stack.splitStack(slot1.getItemStackLimit(stack)));
					} else {
						slot1.putStack(stack.splitStack(stack.getCount()));
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

	@Override
	protected void resetDrag () {
		this.dragEvent = 0;
		this.dragSlots.clear();
	}

	public static boolean canAddItemToSlot (@Nullable Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
		boolean flag = slotIn == null || !slotIn.getHasStack();
		ItemStack slotStack = slotIn.getStack();

		if (!flag && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(slotStack, stack)) {
			return slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slotIn.getItemStackLimit(slotStack);
		}

		return flag;
	}

	@Override
	public void detectAndSendChanges () {
		for (int i = 0; i < this.inventorySlots.size(); ++i) {
			ItemStack itemstack = (this.inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = this.inventoryItemStacks.get(i);

			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
				this.inventoryItemStacks.set(i, itemstack1);

				for (int j = 0; j < this.listeners.size(); ++j) {
					IContainerListener listener = this.listeners.get(j);
					if (listener instanceof EntityPlayerMP) {
						EntityPlayerMP player = (EntityPlayerMP) listener;

						this.syncSlot(player, i, itemstack1);
					} else {
						listener.sendSlotContents(this, i, itemstack1);
					}
				}
			}
		}
	}

	@Override
	public void addListener (IContainerListener listener) {
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

	public void syncInventory (EntityPlayerMP player) {
		for (int i = 0; i < this.inventorySlots.size(); i++) {
			if (this.inventorySlots.get(i) instanceof SlotExtended) {
				ItemStack stack = (this.inventorySlots.get(i)).getStack();

				Networking.CHANNEL.sendTo(new MessageSyncExtendedSlotContents(this.windowId, i, stack), player);
			}
		}

		player.connection.sendPacket(new SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
	}

	public void syncSlot (EntityPlayerMP player, int slot, ItemStack stack) {
		if (getSlot(slot) instanceof SlotExtended) {
			Networking.CHANNEL.sendTo(new MessageSyncExtendedSlotContents(this.windowId, slot, stack), player);
		}
	}
}
