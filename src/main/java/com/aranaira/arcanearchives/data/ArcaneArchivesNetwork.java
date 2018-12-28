package com.aranaira.arcanearchives.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.common.NetworkContainer;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemComparison;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	private UUID mPlayerId;
	private AAWorldSavedData mParent;
	
	private Map<ImmanenceTileEntity, String> mNetworkBlocks = new HashMap<>();
	private List<RadiantChestTileEntity> mRadiantChests = new ArrayList<>();

	private int mCurrentImmanence;
	private boolean mNeedsToBeUpdated = true;

	private ArcaneArchivesNetwork()
	{
		
	}
	
	public int GetImmanence()
	{
		if (mNeedsToBeUpdated)
		{
			UpdateImmanence();
		}
		return mCurrentImmanence;
	}
	
	public int CountBlocks(BlockTemplate block)
	{
		int tmpCount = 0;
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.name == block.refName)
				tmpCount++;
		}
		return tmpCount;
	}
	
	public void UpdateImmanence()
	{
		mCurrentImmanence = 0;
		int TotalGeneration = 0;
		int TotalDrain = 0;
		
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			TotalGeneration += ITE.ImmanenceGeneration;
		}
		
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			int tmpDrain = ITE.ImmanenceDrain;
			if (TotalGeneration > (TotalDrain + tmpDrain))
			{
				TotalDrain += tmpDrain;
				ITE.IsDrainPaid = true;
			}
			else
			{
				ITE.IsDrainPaid = false;
			}
		}
		mCurrentImmanence = TotalGeneration - TotalDrain;
		mNeedsToBeUpdated = false;
	}
	
	public List<NonNullList<ItemStack>> GetItemsOnNetwork()
	{
		List<NonNullList<ItemStack>> inventories = new ArrayList<>();
		
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				inventories.add(ITE.Inventory);
			}
		}
		
		return inventories;
	}
	
	public ItemStack InsertItem(ItemStack itemStack, boolean simulate)
	{
		
		ItemStack temp = itemStack.copy();
		
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				temp = ITE.InsertItem(temp, simulate);
				if (temp.isEmpty())
					return temp;
			}
		}
		
		return temp;
	}

	public ItemStack ExtractItem(ItemStack stack, int amount, boolean simulate) 
	{
		int count_needed = amount;
		ItemStack to_return = stack.copy();
		to_return.setCount(0);
		if (amount > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize();
		}
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				ItemStack s;
				if ((s = ITE.RemoveItemCount(stack, count_needed, simulate)) != ItemStack.EMPTY)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if (count_needed == 0)
						return to_return;
				}
			}
		}
		
		return to_return;
	}
	
	public List<ImmanenceTileEntity> GetTileEntitiesByPriority()
	{
		List<ImmanenceTileEntity> tempList = new ArrayList<ImmanenceTileEntity>();
		tempList.addAll(mNetworkBlocks.keySet());
		Collections.sort(tempList, new Comparator<ImmanenceTileEntity>()
		{
			@Override
			public int compare(ImmanenceTileEntity o1, ImmanenceTileEntity o2)
			{
				if (o1.NetworkPriority > o2.NetworkPriority)
					return 1;
				else
					return -1;
			}
		});
		return tempList;
	}
	
	public ItemStack RemoveItemFromNetwork(ItemStack stack) 
	{
		int count_needed = stack.getCount();
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if (stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize();
		}
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				ItemStack s;
				if ((s = ITE.RemoveItemCount(stack, count_needed, false)) != null)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if (count_needed == 0)
						return to_return;
				}
			}
		}
		
		return new ItemStack(Blocks.AIR);
	}
	
	public ItemStack RemoveHalfStackFromNetwork(ItemStack stack)
	{
		int count_needed = stack.getCount() / 2;
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if (stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize() / 2;
		}
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				ItemStack s;
				if ((s = ITE.RemoveItemCount(stack, count_needed, false)) != null)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if (count_needed == 0)
						return to_return;
				}
			}
		}
		
		return new ItemStack(Blocks.AIR);
	}
	
	public Map<ImmanenceTileEntity, String> getBlocks()
	{
		return mNetworkBlocks;
	}
	
	public void AddBlockToNetwork(String blockName, ImmanenceTileEntity tileEntityInstance)
	{
		if (tileEntityInstance instanceof RadiantChestTileEntity)
		{
			AddRadiantChest((RadiantChestTileEntity)tileEntityInstance);
			tileEntityInstance.hasBeenAddedToNetwork = true;
			return;
		}
		if (IsBlockPosAvailable(tileEntityInstance.blockpos, tileEntityInstance.Dimension))
		{
			mNetworkBlocks.put(tileEntityInstance, blockName);
			tileEntityInstance.hasBeenAddedToNetwork = true;
			mNeedsToBeUpdated = true;
			UpdateImmanence();
		}
	}
	
	public void AddRadiantChest(RadiantChestTileEntity RCTE)
	{
		mRadiantChests.add(RCTE);
	}
	
	public List<RadiantChestTileEntity> GetRadiantChests()
	{
		return mRadiantChests;
	}
	
	public boolean IsBlockPosAvailable(BlockPos pos, int dimID)
	{
		for (ImmanenceTileEntity ITE : mNetworkBlocks.keySet())
		{
			if (ITE.blockpos == pos && ITE.Dimension == dimID)
				return false;
		}
		
		return true;
	}
	
	public void RemoveBlockFromNetwork(ImmanenceTileEntity ITE)
	{
		if (ITE instanceof RadiantChestTileEntity)
		{
			mRadiantChests.remove(ITE);
			return;
		}
		mNetworkBlocks.remove(ITE);
		mNeedsToBeUpdated = true;
		UpdateImmanence();
	}
	
	@Override
	public NBTTagCompound serializeNBT() 
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", mPlayerId);
		
		return tagCompound;
	}
	

	@Override
	public void deserializeNBT(NBTTagCompound nbt) 
	{
		mPlayerId = nbt.getUniqueId("playerId");
	}

	public static ArcaneArchivesNetwork newNetwork(UUID playerID) 
	{
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork();
		network.mPlayerId = playerID;
		return network;
	}

	public void MarkUnsaved() 
	{
		if (getParent() != null)
		{
			getParent().markDirty();
		}
		else
		{
			//TODO: Log that a error had happened and it is not able to be saved
		}
	}
	
	public AAWorldSavedData getParent()
	{
		return mParent;
	}
	
	public ArcaneArchivesNetwork setParent(AAWorldSavedData parent)
	{
		mParent = parent;
		MarkUnsaved();
		return this;
	}

	public UUID getPlayerID() 
	{
		return mPlayerId;
	}

	public static ArcaneArchivesNetwork fromNBT(NBTTagCompound data) 
	{
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork();
		network.deserializeNBT(data);
		return network;
	}

	public int GetTotalItems() 
	{
		int tmp = 0;
		
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				tmp += ITE.GetTotalItems();
			}
		}
		
		return tmp;
	}

	public int GetTotalSpace() 
	{
		int tmp = 0;
		
		for (ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if (ITE.IsInventory)
			{
				tmp += ITE.MaxItems;
			}
		}
		
		return tmp;
	}

	public List<ItemStack> GetAllItemsOnNetwork() 
	{
		List<ItemStack> all_the_items = new ArrayList<>();
		List<NonNullList<ItemStack>> all_items = GetItemsOnNetwork();
		boolean added;
		for (NonNullList<ItemStack> list : all_items)
		{
			for (ItemStack is : list)
			{
				added = false;
				for (ItemStack i : all_the_items)
				{
					if (ItemComparison.AreItemsEqual(is, i))
					{
						i.setCount(i.getCount() + is.getCount());
						added = true;
						break;
					}
				}
				if (!added)
					all_the_items.add(is.copy());
			}
		}
		return all_the_items;
	}

	public List<ItemStack> GetFilteredItems(String s)
	{
		List<ItemStack> all_the_items = new ArrayList<>();
		List<NonNullList<ItemStack>> all_items = GetItemsOnNetwork();
		boolean added;
		for (NonNullList<ItemStack> list : all_items)
		{
			for (ItemStack is : list)
			{
				if (!is.getDisplayName().toLowerCase().contains(s.toLowerCase()))
					continue;
				added = false;
				for (ItemStack i : all_the_items)
				{
					if (is.getUnlocalizedName() == i.getUnlocalizedName())
					{
						i.setCount(is.getCount());
						added = true;
						break;
					}
				}
				if (!added)
					all_the_items.add(is);
			}
		}
		return all_the_items;
	}
}
