package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerManifest extends Container 
{

	public ContainerManifest(EntityPlayer playerIn)
	{
		
		ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		
		List<RadiantChestTileEntity> networkChests = aanetwork.GetRadiantChests();
		
		Queue<Integer> itemSlotsList = new LinkedList<>();
		Queue<IItemHandler> correspondingItemHandler = new LinkedList<>();

		
		for (int i = 0; i < networkChests.size(); i++)
		{
			for (int j = 0; j < networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots(); j++)
			{
				if (!networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(j).isEmpty())
				{
					itemSlotsList.add(j);
					correspondingItemHandler.add(networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
				}
			}
		}
		
		for (int y = 0; y < 9; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				if (itemSlotsList.peek() == null)
					return;
				this.addSlotToContainer(new SlotItemHandler(correspondingItemHandler.poll(), itemSlotsList.poll(), x * 18 + 12, y * 18 + 30));
				
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return true;
	}

	
}
