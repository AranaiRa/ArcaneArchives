package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.tileentities.GemcuttersTableTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGemcuttersTable extends Container 
{
	GemcuttersTableTileEntity mTileEntity;
	boolean isServer;
	
	public ContainerGemcuttersTable(GemcuttersTableTileEntity GCTTE, IInventory playerInventory, boolean serverSide)
	{
		isServer = serverSide;
		//ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		mTileEntity = GCTTE;
		
		getItemHandler().isServer = serverSide;
		
		ArcaneArchives.logger.info("^^^^NULL CHECKS");
		ArcaneArchives.logger.info("inv null? "+playerInventory.equals(null));
		ArcaneArchives.logger.info("te null? "+GCTTE.equals(null));
		
		//player inventory
		int i = 35;
		//Inventory
		for (int y = 2; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 166 + (18 * y)));
				i--;
			}
		}
		//hotbar
		for (int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 224));
			i--;
		}

		i = 25;
		//crafting output - 0
		this.addSlotToContainer(new SlotItemHandler(GCTTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, 95, 18));
		i--;
		
		//selector - 1 - 8
		for (int y = 0; y > -1; y--)
		{
			for (int x = 6; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(GCTTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, x * 18 + 41, y * 18 + 70));
				i--;
			}
		}
		
		//internal storage - 9 - 25
		for (int y = 1; y > -1; y--)
		{
			for (int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(GCTTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 9 * y + x, x * 18 + 23, y * 18 + 105));
				i--;
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
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
			if (index < 36)
			{
				if (!mergeItemStack(slotStack, 45, 62, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
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
	
	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean temp = super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
		this.getItemHandler().updateOutput();
		return temp;
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) 
	{
		if (slotId <= 43 && slotId >= 37)
		{
			getItemHandler().setRecipe(this.getSlot(slotId).getStack());
		}
			
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
	
	public GCTItemHandler getItemHandler()
	{
		return ((GCTItemHandler)mTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
	}
}
