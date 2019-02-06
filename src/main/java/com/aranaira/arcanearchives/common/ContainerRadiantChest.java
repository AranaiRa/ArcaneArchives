package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.packets.RadiantChestListener;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.handlers.ConfigHandler;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Set;

public class ContainerRadiantChest extends Container
{

	private final Set<Slot> dragSlots = Sets.<Slot>newHashSet();
	public String mName;
	public int mDimension;
	public BlockPos mPos;
	private int dragEvent;
	private int dragMode = -1;

	public ContainerRadiantChest(RadiantChestTileEntity RCTE, IInventory playerInventory)
	{
		mName = RCTE.chestName;
		mPos = RCTE.getPos();
		mDimension = RCTE.getWorld().provider.getDimension();
		AAItemStackHandler handler = (AAItemStackHandler) RCTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		for(int y = 5; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new LargeItemStackSlot(handler, 9 * y + x, x * 18 + 16, y * 18 + 16));
			}
		}

		//Creates the slots for the players inventory.
		int i = 35;
		//Inventory.
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 16 + (18 * x), 142 + (18 * y)));

				i--;
			}
		}
		//Hotbar.
		for(int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 16 + (18 * x), 200));
			i--;
		}
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		if(this.listeners.contains(listener))
		{
			throw new IllegalArgumentException("Listener already listening");
		} else
		{
			if(listener instanceof EntityPlayerMP)
			{
				IContainerListener newListener = new RadiantChestListener((EntityPlayerMP) listener);
				this.listeners.add(newListener);
				newListener.sendAllContents(this, this.getInventory());
				this.detectAndSendChanges();
			} else
			{
				super.addListener(listener);
			}
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if(index < 54)
			{
				if(!mergeItemStack(slotStack, 54, 90, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
			{
				if(!mergeItemStack2(slotStack, 0, 54, true)) return ItemStack.EMPTY;
			}

			if(slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
		}

		return stack;
	}

	@Override
	public void putStackInSlot(int slotID, @Nonnull ItemStack stack)
	{
		super.putStackInSlot(slotID, stack);
	}

	private void ResetDrag()
	{
		dragEvent = 0;
		dragSlots.clear();
	}

	//@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		return super.slotClick(slotId, dragType, clickTypeIn, player);
		/*
		ItemStack itemstack = ItemStack.EMPTY;
		InventoryPlayer inventoryplayer = player.inventory;

		if(clickTypeIn == ClickType.QUICK_CRAFT)
		{

			int j1 = this.dragEvent;
			this.dragEvent = getDragEvent(dragType);

			if((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent)
			{
				ResetDrag();
			}
			else if(inventoryplayer.getItemStack().isEmpty())
			{
				ResetDrag();
			}
			else if(this.dragEvent == 0)
			{
				this.dragMode = extractDragMode(dragType);

				if(isValidDragMode(this.dragMode, player))
				{
					this.dragEvent = 1;
					this.dragSlots.clear();
				} else
				{
					ResetDrag();
				}
			} else if(this.dragEvent == 1)
			{
				Slot slot7 = this.inventorySlots.get(slotId);
				ItemStack itemstack12 = inventoryplayer.getItemStack();

				if(slot7 != null && canAddItemToSlot(slot7, itemstack12, true) && slot7.isItemValid(itemstack12) && (this.dragMode == 2 || itemstack12.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7))
				{
					this.dragSlots.add(slot7);
				}
			} else if(this.dragEvent == 2)
			{
				if(!this.dragSlots.isEmpty())
				{
					ItemStack itemstack9 = inventoryplayer.getItemStack().copy();
					int k1 = inventoryplayer.getItemStack().getCount();

					for(Slot slot8 : this.dragSlots)
					{
						ItemStack itemstack13 = inventoryplayer.getItemStack();

						if(slot8 != null && canAddItemToSlot(slot8, itemstack13, true) && slot8.isItemValid(itemstack13) && (this.dragMode == 2 || itemstack13.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(slot8))
						{
							ItemStack itemstack14 = itemstack9.copy();
							int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
							computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
							int k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));

							if(itemstack14.getCount() > k3)
							{
								itemstack14.setCount(k3);
							}

							k1 -= itemstack14.getCount() - j3;
							slot8.putStack(itemstack14);
						}
					}

					itemstack9.setCount(k1);
					inventoryplayer.setItemStack(itemstack9);
				}

				ResetDrag();
			} else
			{
				ResetDrag();
			}
		} else if(this.dragEvent != 0)
		{
			ResetDrag();
		} else if((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1))
		{
			if(slotId == -999)
			{
				if(!inventoryplayer.getItemStack().isEmpty())
				{
					if(dragType == 0)
					{
						player.dropItem(inventoryplayer.getItemStack(), true);
						inventoryplayer.setItemStack(ItemStack.EMPTY);
					}

					if(dragType == 1)
					{
						player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
					}
				}
			} else if(clickTypeIn == ClickType.QUICK_MOVE)
			{
				if(slotId < 0)
				{
					return ItemStack.EMPTY;
				}

				Slot slot5 = this.inventorySlots.get(slotId);

				if(slot5 == null || !slot5.canTakeStack(player))
				{
					return ItemStack.EMPTY;
				}

				for(ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId))
				{
					itemstack = itemstack7.copy();
				}
			} else
			{
				if(slotId < 0)
				{
					return ItemStack.EMPTY;
				}

				Slot slot6 = this.inventorySlots.get(slotId);

				if(slot6 != null)
				{
					ItemStack itemstack8 = slot6.getStack();
					ItemStack itemstack11 = inventoryplayer.getItemStack();

					if(!itemstack8.isEmpty())
					{
						itemstack = itemstack8.copy();
					}

					if(itemstack8.isEmpty())
					{
						if(!itemstack11.isEmpty() && slot6.isItemValid(itemstack11))
						{
							int i3 = dragType == 0 ? itemstack11.getCount() : 1;

							if(i3 > slot6.getItemStackLimit(itemstack11))
							{
								i3 = slot6.getItemStackLimit(itemstack11);
							}

							slot6.putStack(itemstack11.splitStack(i3));
						}
					} else if(slot6.canTakeStack(player))
					{
						if(itemstack11.isEmpty())
						{
							if(itemstack8.isEmpty())
							{
								slot6.putStack(ItemStack.EMPTY);
								inventoryplayer.setItemStack(ItemStack.EMPTY);
							} else
							{
								int l2 = dragType == 0 ? itemstack8.getCount() : (itemstack8.getCount() + 1) / 2;
								inventoryplayer.setItemStack(slot6.decrStackSize(l2));

								if(itemstack8.isEmpty())
								{
									slot6.putStack(ItemStack.EMPTY);
								}

								slot6.onTake(player, inventoryplayer.getItemStack());
							}
						} else if(slot6.isItemValid(itemstack11))
						{
							if(itemstack8.getItem() == itemstack11.getItem() && itemstack8.getMetadata() == itemstack11.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11))
							{
								int k2 = dragType == 0 ? itemstack11.getCount() : 1;

								// Discrepancies
								// Slot itemStackLimit?
								if(slotId > 53)
								{
									if(k2 > slot6.getItemStackLimit(itemstack11) - itemstack8.getCount())
									{
										k2 = slot6.getItemStackLimit(itemstack11) - itemstack8.getCount();
									}

									if(k2 > itemstack11.getMaxStackSize() - itemstack8.getCount())
									{
										k2 = itemstack11.getMaxStackSize() - itemstack8.getCount();
									}
								} else
								{
									if(k2 > slot6.getItemStackLimit(itemstack11) * ConfigHandler.ConfigValues.iRadiantChestMultiplier - itemstack8.getCount())
									{
										k2 = slot6.getItemStackLimit(itemstack11) * ConfigHandler.ConfigValues.iRadiantChestMultiplier - itemstack8.getCount();
									}

									if(k2 > itemstack11.getMaxStackSize() * ConfigHandler.ConfigValues.iRadiantChestMultiplier - itemstack8.getCount())
									{
										k2 = itemstack11.getMaxStackSize() * ConfigHandler.ConfigValues.iRadiantChestMultiplier - itemstack8.getCount();
									}
								}

								itemstack11.shrink(k2);
								itemstack8.grow(k2);
							} else if(itemstack11.getCount() <= slot6.getItemStackLimit(itemstack11))
							{
								slot6.putStack(itemstack11);
								inventoryplayer.setItemStack(itemstack8);
							}
						} else if(itemstack8.getItem() == itemstack11.getItem() && itemstack11.getMaxStackSize() > 1 && (!itemstack8.getHasSubtypes() || itemstack8.getMetadata() == itemstack11.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack8, itemstack11) && !itemstack8.isEmpty())
						{
							int j2 = itemstack8.getCount();

							if(j2 + itemstack11.getCount() <= itemstack11.getMaxStackSize())
							{
								itemstack11.grow(j2);
								itemstack8 = slot6.decrStackSize(j2);

								if(itemstack8.isEmpty())
								{
									slot6.putStack(ItemStack.EMPTY);
								}

								slot6.onTake(player, inventoryplayer.getItemStack());
							}
						}
					}

					slot6.onSlotChanged();
				}
			}
		} else if(clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9)
		{
			Slot slot4 = this.inventorySlots.get(slotId);
			ItemStack itemstack6 = inventoryplayer.getStackInSlot(dragType);
			ItemStack itemstack10 = slot4.getStack();

			if(!itemstack6.isEmpty() || !itemstack10.isEmpty())
			{
				if(itemstack6.isEmpty())
				{
					if(slot4.canTakeStack(player))
					{
						inventoryplayer.setInventorySlotContents(dragType, itemstack10);
						//slot4.onSwapCraft(itemstack10.getCount());
						slot4.putStack(ItemStack.EMPTY);
						slot4.onTake(player, itemstack10);
					}
				} else if(itemstack10.isEmpty())
				{
					if(slot4.isItemValid(itemstack6))
					{
						int l1 = slot4.getItemStackLimit(itemstack6);

						if(itemstack6.getCount() > l1)
						{
							slot4.putStack(itemstack6.splitStack(l1));
						} else
						{
							slot4.putStack(itemstack6);
							inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
						}
					}
				} else if(slot4.canTakeStack(player) && slot4.isItemValid(itemstack6))
				{
					int i2 = slot4.getItemStackLimit(itemstack6);

					if(itemstack6.getCount() > i2)
					{
						slot4.putStack(itemstack6.splitStack(i2));
						slot4.onTake(player, itemstack10);

						if(!inventoryplayer.addItemStackToInventory(itemstack10))
						{
							player.dropItem(itemstack10, true);
						}
					} else
					{
						slot4.putStack(itemstack6);
						inventoryplayer.setInventorySlotContents(dragType, itemstack10);
						slot4.onTake(player, itemstack10);
					}
				}
			}
		} else if(clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0)
		{
			Slot slot3 = this.inventorySlots.get(slotId);

			if(slot3 != null && slot3.getHasStack())
			{
				ItemStack itemstack5 = slot3.getStack().copy();
				itemstack5.setCount(itemstack5.getMaxStackSize());
				inventoryplayer.setItemStack(itemstack5);
			}
		} else if(clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0)
		{
			Slot slot2 = this.inventorySlots.get(slotId);

			if(slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player))
			{
				ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
				slot2.onTake(player, itemstack4);
				player.dropItem(itemstack4, true);
			}
		} else if(clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0)
		{
			Slot slot = this.inventorySlots.get(slotId);
			ItemStack itemstack1 = inventoryplayer.getItemStack();

			if(!itemstack1.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player)))
			{
				int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
				int j = dragType == 0 ? 1 : -1;

				for(int k = 0; k < 2; ++k)
				{
					for(int l = i; l >= 0 && l < this.inventorySlots.size() && itemstack1.getCount() < itemstack1.getMaxStackSize(); l += j)
					{
						Slot slot1 = this.inventorySlots.get(l);

						if(slot1.getHasStack() && canAddItemToSlot(slot1, itemstack1, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack1, slot1))
						{
							ItemStack itemstack2 = slot1.getStack();

							if(k != 0 || itemstack2.getCount() != itemstack2.getMaxStackSize())
							{
								int i1 = Math.min(itemstack1.getMaxStackSize() - itemstack1.getCount(), itemstack2.getCount());
								ItemStack itemstack3 = slot1.decrStackSize(i1);
								itemstack1.grow(i1);

								if(itemstack3.isEmpty())
								{
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

		return itemstack;*/
	}


	//Daomephsta's code that I have referenced. 
	//https://github.com/Daomephsta/Precision-Crafting/blob/master/src/main/java/leviathan143/precisioncrafting/common/precisiontable/ContainerPrecisionTable.java#L298-L346
	/*
    @Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			// Hotbar
			if (index <= 8)
			{
				if (!mergeItemStack(slotStack, 46, 55, false)) return ItemStack.EMPTY;
			}
			// Player Inventory
			else if (index >= 9 && index <= 35)
			{
				if (!mergeItemStack(slotStack, 46, 55, false)) return ItemStack.EMPTY;
			}
			// Pattern
			else if (index >= 36 && index <= 44)
			{
				return stack;
			}
			// Output
			else if (index == 45)
			{
				if (!mergeItemStack(slotStack, 0, 36, true)) return ItemStack.EMPTY;
			}
			// Ingredients
			else if (index >= 46 && index <= 54)
			{
				if (!mergeItemStack(slotStack, 0, 36, true)) return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return stack;
	}
    */

	// PArameters are always 0, 54 and true
	private boolean mergeItemStack2(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean flag = false;
		int i = startIndex;

		if(reverseDirection)
		{
			i = endIndex - 1;
		}

		if(stack.isStackable())
		{
			while(!stack.isEmpty())
			{
				if(reverseDirection)
				{
					if(i < startIndex)
					{
						break;
					}
				} else if(i >= endIndex)
				{
					break;
				}

				SlotItemHandler slot = (SlotItemHandler) this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();

				if(!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack))
				{
					int j = itemstack.getCount() + stack.getCount();
					int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize() * ConfigHandler.ConfigValues.iRadiantChestMultiplier);

					if(j <= maxSize)
					{
						stack.setCount(0);
						itemstack.setCount(j);
						slot.onSlotChanged();
						flag = true;
					} else if(itemstack.getCount() < maxSize)
					{
						stack.shrink(maxSize - itemstack.getCount());
						itemstack.setCount(maxSize);
						slot.onSlotChanged();
						flag = true;
					}
				}

				if(reverseDirection)
				{
					--i;
				} else
				{
					++i;
				}
			}
		}

		if(!stack.isEmpty())
		{
			if(reverseDirection)
			{
				i = endIndex - 1;
			} else
			{
				i = startIndex;
			}

			while(true)
			{
				if(reverseDirection)
				{
					if(i < startIndex)
					{
						break;
					}
				} else if(i >= endIndex)
				{
					break;
				}

				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if(itemstack1.isEmpty() && slot1.isItemValid(stack))
				{
					if(stack.getCount() > slot1.getSlotStackLimit())
					{
						slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
					} else
					{
						slot1.putStack(stack.splitStack(stack.getCount()));
					}

					slot1.onSlotChanged();
					flag = true;
					break;
				}

				if(reverseDirection)
				{
					--i;
				} else
				{
					++i;
				}
			}
		}

		return flag;
	}

	@Override
	public void detectAndSendChanges()
	{
		for(int i = 0; i < this.inventorySlots.size(); ++i)
		{
			ItemStack itemstack = this.inventorySlots.get(i).getStack();
			ItemStack itemstack1 = this.inventoryItemStacks.get(i);

			if(!ItemStack.areItemStacksEqual(itemstack1, itemstack))
			{
				boolean clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack);
				itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
				this.inventoryItemStacks.set(i, itemstack1);

				if(clientStackChanged) {
					for (IContainerListener listener : this.listeners) {
							listener.sendSlotContents(this, i, itemstack1);
					}
				}
			}
		}
	}
}
