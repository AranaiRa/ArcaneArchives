package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.RadiantChestPlaceHolder;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ManifestItemHandler implements IItemHandlerModifiable 
{
	public static ManifestItemHandler mInstance = new ManifestItemHandler();
	
	private UUID mPlayerID;
	public List<ItemStack> mItemStacks;
	private String mSearchText = "";
	public List<RadiantChestPlaceHolder> mChests;
	//TODO setup to set this variable again.
	public EntityPlayer mPlayer = null;

	public ManifestItemHandler()
	{
		mChests = new ArrayList();
		mItemStacks= new ArrayList();
	}
	
	//Returns the slot number of the item added.
	public void AddItemStack(ItemStack item)
	{
		if (mPlayer == null)
			ArcaneArchives.logger.info("mPlayer is Null!");
		mItemStacks.add(item);
	}
	
	@Override
	public int getSlots() {
		// TODO Auto-generated method stub
		return 81;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		if (mSearchText.equals(""))
		{
			if (slot > mItemStacks.size() - 1)
				return ItemStack.EMPTY;
			return mItemStacks.get(slot);
		}
		else
		{
			List<ItemStack> s = filteredResults();
			
			if (slot > s.size() - 1)
				return ItemStack.EMPTY;
			
			return filteredResults().get(slot);
		}
			
	}
	
	public List<ItemStack> filteredResults()
	{
		
		List<ItemStack> temp = new ArrayList();
		
		for (ItemStack s : mItemStacks)
		{
			if (s.getDisplayName().toLowerCase().contains(mSearchText.toLowerCase()))
			{
				temp.add(s);
			}
		}
		
		return temp;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 0;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) 
	{
		
	}

	public void setSearchText(String s)
	{
		mSearchText = s;
	}
	
	public void Clear()
	{
		mChests = new ArrayList();
		mItemStacks= new ArrayList();
		mSearchText = "";
	}

	public void SortChests()
	{
		
		
		mChests.sort(new Comparator<RadiantChestPlaceHolder>() {
			
			@Override
			public int compare(final RadiantChestPlaceHolder rcte1, final RadiantChestPlaceHolder rcte2)
			{
				int posX = 0;
				int posY = 0;
				int posZ = 0;
				if (mPlayer != null)
				{
					posX = (int) mPlayer.posX;
					posY = (int) mPlayer.posY;
					posZ = (int) mPlayer.posZ;
				}
				double rcte1Dist = rcte1.mPos.getDistance((int)posX, (int)posY, (int)posZ);
				double rcte2Dist = rcte2.mPos.getDistance((int)posX, (int)posY, (int)posZ);
				if (rcte1Dist == rcte2Dist)
					return 0;
				else if (rcte1Dist > rcte2Dist)
					return 1;
				else
					return -1;
			}
		});
	}
}
