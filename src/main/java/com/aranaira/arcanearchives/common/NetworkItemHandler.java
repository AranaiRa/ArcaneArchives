package com.aranaira.arcanearchives.common;

import java.util.List;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NetworkItemHandler implements IItemHandlerModifiable {

	private UUID playerUUID;
	private String searchText = "";
	
	public NetworkItemHandler(UUID uuid)
	{
		playerUUID = uuid;
	}
	
	public void setSearchString(String s)
	{
		searchText = s;
	}
	
	public String getSearchString()
	{
		return searchText;
	}
	
	@Override
	public int getSlots() 
	{
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		List<ItemStack> listofItems;
		if (searchText.compareTo("") != 0)
			listofItems = NetworkHelper.getArcaneArchivesNetwork(playerUUID).GetFilteredItems(searchText);
		else
			listofItems = NetworkHelper.getArcaneArchivesNetwork(playerUUID).GetAllItemsOnNetwork();
		
		if (listofItems.size() > slot)
			return listofItems.get(slot);
		else
			return ItemStack.EMPTY;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		return NetworkHelper.getArcaneArchivesNetwork(playerUUID).InsertItem(stack.copy(), simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		return NetworkHelper.getArcaneArchivesNetwork(playerUUID).ExtractItem(getStackInSlot(slot), amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		//Should this be something ridiculously high?
		return 100000;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		//if (stack.isEmpty())
		//	return;
		
		//ArcaneArchives.logger.info(NetworkHelper.getArcaneArchivesNetwork(playerUUID).GetTotalItems());
		
		//NetworkHelper.getArcaneArchivesNetwork(playerUUID).InsertItem(stack, false);
		
		//NetworkHelper.getArcaneArchivesNetwork(playerUUID).InsertItem(stack, false);
		//ArcaneArchives.logger.info(slot);
		//NetworkHelper.getArcaneArchivesNetwork(playerUUID).InsertItem(stack);
	}
}
