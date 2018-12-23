package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerManifest extends Container 
{

	List<RadiantChestTileEntity> networkChests;
	List<ItemStack> ItemList = new ArrayList<>();
	ManifestItemHandler mManifestItemHandler;
	
	public ContainerManifest(EntityPlayer playerIn)
	{
		
		ArcaneArchivesNetwork aanetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		
		networkChests = aanetwork.GetRadiantChests();
		
		mManifestItemHandler = new ManifestItemHandler(playerIn.getUniqueID());
		
		networkChests.sort(new Comparator<RadiantChestTileEntity>() {
			@Override
			public int compare(final RadiantChestTileEntity rcte1, final RadiantChestTileEntity rcte2)
			{
				double rcte1Dist = rcte1.blockpos.getDistance((int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
				double rcte2Dist = rcte2.blockpos.getDistance((int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
				if (rcte1Dist == rcte2Dist)
					return 0;
				else if (rcte1Dist > rcte2Dist)
					return 1;
				else
					return -1;
			}
		});
		ArcaneArchives.logger.info("Sorted chests by distance.");
		
		for (int i = 0; i < networkChests.size(); i++)
		{
			for (int j = 0; j < networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots(); j++)
			{
				if (!networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(j).isEmpty())
				{
					ItemStack s = networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(j);
					
					boolean added = false;
					
					for (int k = 0; k < ItemList.size(); k++)
					{
						if (ItemComparison.AreItemsEqual(ItemList.get(k), s))
						{
							ItemList.get(k).grow(s.getCount());
							added = true;
						}
					}
					
					if (!added)
						ItemList.add(s.copy());
				}
			}
		}
		ArcaneArchives.logger.info("Added item contents.");

		
		for (int i = 0; i < ItemList.size(); i++)
		{
			mManifestItemHandler.AddItemStack(ItemList.get(i));
		}
		
		int i = 0;
		for (int y = 0; y < 9; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new SlotItemHandler(mManifestItemHandler, i, x * 18 + 12, y * 18 + 30));
				i++;
			}
		}
		ArcaneArchives.logger.info("Added slots.");
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) 
	{
		if (mManifestItemHandler.getStackInSlot(slotId).isEmpty())
			return ItemStack.EMPTY;
		RadiantChestTileEntity RCTE = null;
		for (RadiantChestTileEntity rcte : networkChests)
		{
			if (rcte.Contains(mManifestItemHandler.getStackInSlot(slotId)))
			{
				RCTE = rcte;
				break;
			}
		}
		
		if (RCTE == null)
			return ItemStack.EMPTY;
		
		BlockPos bp = RCTE.blockpos;
		
		AATickHandler.GetInstance().mBlockPosition = new Vec3d(bp.getX(), bp.getY(), bp.getZ());
		AATickHandler.GetInstance().mIsDrawingLine = true;
		
		return ItemStack.EMPTY;
	}

	public void SetSearchString(String SearchText) 
	{
		mManifestItemHandler.setSearchText(SearchText);
	}
}
