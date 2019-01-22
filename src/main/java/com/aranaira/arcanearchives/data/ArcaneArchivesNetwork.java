package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.common.ManifestItemHandler;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemComparison;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	public boolean mShared = false;
	public UUID mSharedPlayer = null;

	public HashMap<String, UUID> pendingInvites = new HashMap<>();


	private UUID mPlayerId;
	private AAWorldSavedData mParent;

	private List<ImmanenceTileEntity> mNetworkTiles = new ArrayList<>();

	private int mCurrentImmanence;
	private boolean mNeedsToBeUpdated = true;

	public ManifestItemHandler mManifestItemHandler = null;

	private ArcaneArchivesNetwork(UUID id)
	{
		mPlayerId = id;
		mManifestItemHandler = new ManifestItemHandler();
	}

	public void ShareWith(UUID targetNetwork)
	{
		mShared = true;
		mSharedPlayer = targetNetwork;
	}

	public int GetImmanence()
	{
		if(mNeedsToBeUpdated)
		{
			UpdateImmanence();
		}
		return mCurrentImmanence;
	}

	public int CountTileEntities(Class clazz)
	{
		int tmpCount = 0;
		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.getClass().equals(clazz)) tmpCount++;
		}
		return tmpCount;
	}

	public void UpdateImmanence()
	{
		mCurrentImmanence = 0;
		int TotalGeneration = 0;
		int TotalDrain = 0;

		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			TotalGeneration += ITE.ImmanenceGeneration;
		}

		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			int tmpDrain = ITE.ImmanenceDrain;
			if(TotalGeneration > (TotalDrain + tmpDrain))
			{
				TotalDrain += tmpDrain;
				ITE.IsDrainPaid = true;
			} else
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

		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
			{
				inventories.add(ITE.Inventory);
			}
		}

		return inventories;
	}

	public ItemStack InsertItem(ItemStack itemStack, boolean simulate)
	{

		ItemStack temp = itemStack.copy();

		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
			{
				temp = ITE.InsertItem(temp, simulate);
				if(temp.isEmpty()) return temp;
			}
		}

		return temp;
	}

	public ItemStack ExtractItem(ItemStack stack, int amount, boolean simulate)
	{
		int count_needed = amount;
		ItemStack to_return = stack.copy();
		to_return.setCount(0);
		if(amount > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize();
		}
		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
			{
				ItemStack s;
				if((s = ITE.RemoveItemCount(stack, count_needed, simulate)) != ItemStack.EMPTY)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if(count_needed == 0) return to_return;
				}
			}
		}

		return to_return;
	}

	public List<ImmanenceTileEntity> GetTileEntitiesByPriority()
	{
		List<ImmanenceTileEntity> tempList = new ArrayList<>(getBlocks());
		tempList.sort((o1, o2) ->
		{
			if(o1.NetworkPriority > o2.NetworkPriority) return 1;
			else return -1;
		});
		return tempList;
	}

	public ItemStack RemoveItemFromNetwork(ItemStack stack)
	{
		int count_needed = stack.getCount();
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if(stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize();
		}
		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
			{
				ItemStack s;
				if((s = ITE.RemoveItemCount(stack, count_needed, false)) != null)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if(count_needed == 0) return to_return;
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
		if(stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize() / 2;
		}
		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
			{
				ItemStack s;
				if((s = ITE.RemoveItemCount(stack, count_needed, false)) != null)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if(count_needed == 0) return to_return;
				}
			}
		}

		return new ItemStack(Blocks.AIR);
	}

	public void cleanNetworkTiles()
	{
		mNetworkTiles.removeIf(ImmanenceTileEntity::isInvalid);
	}

	public List<ImmanenceTileEntity> getBlocks()
	{
		cleanNetworkTiles();
		return mNetworkTiles;
	}

	public void AddTileToNetwork(ImmanenceTileEntity tileEntityInstance)
	{
		mNetworkTiles.add(tileEntityInstance);
		tileEntityInstance.hasBeenAddedToNetwork = true;

		mNeedsToBeUpdated = true;
		UpdateImmanence();
	}

	public List<RadiantChestTileEntity> GetRadiantChests()
	{
		return mNetworkTiles.stream().filter((k) -> k instanceof RadiantChestTileEntity).map((k) -> (RadiantChestTileEntity) k).collect(Collectors.toList());
	}

	public void triggerUpdate()
	{
		cleanNetworkTiles();
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
		mManifestItemHandler = new ManifestItemHandler();
	}

	public static ArcaneArchivesNetwork newNetwork(UUID playerID)
	{
		return new ArcaneArchivesNetwork(playerID);
	}

	public void MarkUnsaved()
	{
		if(getParent() != null)
		{
			getParent().markDirty();
		} else
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
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork(null);
		network.deserializeNBT(data);
		return network;
	}

	public int GetTotalItems()
	{
		int tmp = 0;

		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
			{
				tmp += ITE.GetTotalItems();
			}
		}

		return tmp;
	}

	public int GetTotalSpace()
	{
		int tmp = 0;

		for(ImmanenceTileEntity ITE : GetTileEntitiesByPriority())
		{
			if(ITE.IsInventory)
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
		for(NonNullList<ItemStack> list : all_items)
		{
			for(ItemStack is : list)
			{
				added = false;
				for(ItemStack i : all_the_items)
				{
					if(ItemComparison.AreItemsEqual(is, i))
					{
						i.setCount(i.getCount() + is.getCount());
						added = true;
						break;
					}
				}
				if(!added) all_the_items.add(is.copy());
			}
		}
		return all_the_items;
	}

	public List<ItemStack> GetFilteredItems(String s)
	{
		List<ItemStack> all_the_items = new ArrayList<>();
		List<NonNullList<ItemStack>> all_items = GetItemsOnNetwork();
		boolean added;
		for(NonNullList<ItemStack> list : all_items)
		{
			for(ItemStack is : list)
			{
				if(!is.getDisplayName().toLowerCase().contains(s.toLowerCase())) continue;
				added = false;
				for(ItemStack i : all_the_items)
				{
					if(is.getTranslationKey() == i.getTranslationKey())
					{
						i.setCount(is.getCount());
						added = true;
						break;
					}
				}
				if(!added) all_the_items.add(is);
			}
		}
		return all_the_items;
	}

	public void Invite(String name, UUID uuid)
	{

		pendingInvites.put(name, uuid);
	}

	public boolean Accept(String name)
	{
		if(!pendingInvites.containsKey(name)) return false;
		mShared = true;
		mSharedPlayer = pendingInvites.get(name);
		return true;
	}
}
