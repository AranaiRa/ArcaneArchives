package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.GemcuttersTableTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGemcuttersTable extends Container 
{
	public ContainerGemcuttersTable(GemcuttersTableTileEntity GCTTE, IInventory playerInventory)
	{
		//ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		
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
		//crafting output
		this.addSlotToContainer(new SlotItemHandler(GCTTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, 95, 18));
		i--;
		
		//selector
		for (int y = 0; y > -1; y--)
		{
			for (int x = 6; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(GCTTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, x * 18 + 41, y * 18 + 70));
				i--;
			}
		}
		
		//internal storage
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

}
