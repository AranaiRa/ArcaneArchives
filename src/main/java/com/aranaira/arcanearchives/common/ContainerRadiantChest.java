package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerRadiantChest extends Container {

	public ContainerRadiantChest(RadiantChestTileEntity RCTE, IInventory playerInventory)
	{
		for (int y = 5; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(RCTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 9 * y + x, x * 18 + 16, y * 18 + 16));
			}
		}
		

		//Creates the slots for the players inventory.
		int i = 35;
		//Inventory.
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 16 + (18 * x), 142 + (18 * y)));
				
				i--;
			}
		}
		//Hotbar.
		for (int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 16 + (18 * x), 200));
			i--;
		}
		
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		

		if (slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();
			
			//Chest inventory
			if (index < 54)
			{
				if (!mergeItemStack(slotStack, 54, 90, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
			{
				if (!mergeItemStack(slotStack, 0, 54, true)) return ItemStack.EMPTY;
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
	
}
