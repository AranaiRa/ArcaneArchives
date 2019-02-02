package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.common.ManifestItemHandler;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.packets.PacketSynchronise;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.*;
import com.aranaira.arcanearchives.util.types.SlotIterable;
import com.aranaira.arcanearchives.util.types.TileList;
import com.aranaira.arcanearchives.util.types.Tuple;
import com.aranaira.arcanearchives.util.types.Turple;
import com.google.common.annotations.Beta;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.*;
import java.util.List;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	public boolean mShared = false;
	public UUID mSharedPlayer = null;

	public HashMap<String, UUID> pendingInvites = new HashMap<>();

	private UUID mPlayerId;
	private AAWorldSavedData mParent;

	private TileList mNetworkTiles = new TileList(new ArrayList<>());

	private int mCurrentImmanence;
	private boolean mNeedsToBeUpdated = true;

	// ManifestItemHandler is client-only for now

	private ArcaneArchivesNetwork(UUID id)
	{
		mPlayerId = id;
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

	public TileList.TileListIterable FetchTileEntities(Class<? extends AATileEntity> clazz)
	{
		return mNetworkTiles.filterClass(clazz);
	}

	public int CountTileEntities(Class<? extends AATileEntity> clazz)
	{
		int tmpCount = 0;
		for(ImmanenceTileEntity ite : mNetworkTiles.filterClass(clazz))
		{
			tmpCount++;
		}
		return tmpCount;
	}

	public void UpdateImmanence()
	{
		mCurrentImmanence = 0;
		int TotalGeneration = 0;
		int TotalDrain = 0;

		for(ImmanenceTileEntity ITE : GetBlocks())
		{
			TotalGeneration += ITE.ImmanenceGeneration;
		}

		// Avoid the priority as it creates a new list
		// only use if you ACTUALLY care about priority
		// otherwise it would be best to resort the list
		// whenever a new tile is added, based on priority
		for(ImmanenceTileEntity ITE : GetBlocks())
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

	@Beta
	public List<NonNullList<ItemStack>> GetItemsOnNetwork()
	{
		List<NonNullList<ItemStack>> inventories = new ArrayList<>();

		for(ImmanenceTileEntity ITE : GetBlocks())
		{
			if(ITE.IsInventory)
			{
				// Should use addAll? TODO
				inventories.add(ITE.Inventory);
			}
		}

		return inventories;
	}

	@Beta
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

	@Beta
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

	public TileList GetTileEntitiesByPriority()
	{
		return this.mNetworkTiles.sorted((o1, o2) ->
		{
			if(o1.NetworkPriority > o2.NetworkPriority) return 1;
			else return -1;
		});
	}

	@Beta
	public ItemStack RemoveItemFromNetwork(ItemStack stack)
	{
		int count_needed = stack.getCount();
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if(stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize();
		}
		return getItemStack(stack, count_needed, to_return);
	}

	@Beta
	public ItemStack RemoveHalfStackFromNetwork(ItemStack stack)
	{
		int count_needed = stack.getCount() / 2;
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if(stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize() / 2;
		}
		return getItemStack(stack, count_needed, to_return);
	}

	/* What is this used for? */
	@Beta
	private ItemStack getItemStack(ItemStack stack, int count_needed, ItemStack to_return)
	{
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

	public TileList GetTiles () {
		return this.mNetworkTiles;
	}

	public TileList.TileListIterable GetBlocks()
	{
		return GetBlocks(false);
	}

	public TileList.TileListIterable GetBlocks(boolean started)
	{
		if(!started)
		{
			return mNetworkTiles.filterActive();
		}
		return mNetworkTiles.iterable();
	}

	public void AddTileToNetwork(ImmanenceTileEntity tileEntityInstance)
	{
		if (mNetworkTiles.contains(tileEntityInstance)) return;

		mNetworkTiles.add(tileEntityInstance);
		tileEntityInstance.hasBeenAddedToNetwork = true;

		mNeedsToBeUpdated = true;
		UpdateImmanence();
	}

	public void RemoveTileFromNetwork(ImmanenceTileEntity tileEntityInstance)
	{
		mNetworkTiles.remove(tileEntityInstance);

		mNeedsToBeUpdated = true;
		UpdateImmanence();
	}

	public boolean NetworkContainsTile (ImmanenceTileEntity tileEntityInstance) {
		return mNetworkTiles.contains(tileEntityInstance);
	}

	public TileList.TileListIterable GetRadiantChests()
	{
		return mNetworkTiles.filterClass(RadiantChestTileEntity.class);
	}

	public void triggerUpdate()
	{
		//mNetworkTiles.cleanInvalid();
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

	@Beta
	public int GetItemCount()
	{
		int tmp = 0;

		for(ImmanenceTileEntity ITE : GetBlocks())
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

		for(ImmanenceTileEntity ITE : GetBlocks())
		{
			if(ITE.IsInventory)
			{
				tmp += ITE.MaxItems;
			}
		}

		return tmp;
	}

	@Beta
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

	public NBTTagCompound buildSynchroniseManifest () {
		List<Turple<ItemStack, Integer, BlockPos>> map = new ArrayList<>();

		// Step one: iterate loaded chests and get item stacks.

		for (ImmanenceTileEntity ite : GetRadiantChests()) {
			RadiantChestTileEntity chest = (RadiantChestTileEntity) ite;
			int dimId = chest.getWorld().provider.getDimension();
			for (ItemStack is : new SlotIterable(chest.getInventory())) {
				if (is.isEmpty()) continue;

				map.add(new Turple<>(is.copy(), dimId, chest.getPos()));
			}
		}

		NBTTagList manifest = new NBTTagList();

		// TODO: Change into ManifestList
		List<Turple<ItemStack, Integer, List<BlockPos>>> consolidated = ItemStackConsolidator.TurpleConsolidatedItems(map);
		consolidated.sort((turp1, turp2) -> {
			if (ItemComparison.AreItemsEqual(turp1.val1, turp2.val1) && turp1.val2.equals(turp2.val2)) return 0;
			return -1;
		});

		for (Turple<ItemStack, Integer, List<BlockPos>> entry : consolidated) {
			NBTTagCompound itemEntry = new NBTTagCompound();
			LargeItemNBTUtil.writeToNBT(itemEntry, entry.val1);
			NBTTagList positions = new NBTTagList();
			for (BlockPos pos : entry.val3) {
				positions.appendTag(new NBTTagLong(pos.toLong()));
			}
			itemEntry.setTag("positions", positions);
			itemEntry.setInteger("dimension", entry.val2);
			manifest.appendTag(itemEntry);
		}

		NBTTagCompound result = new NBTTagCompound();
		result.setTag("manifest", manifest);

		return result;
	}

	public NBTTagCompound buildSynchroniseData()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("mShared", mShared);
		tag.setString("mOwnerId", mPlayerId.toString());
		tag.setInteger("mCurrentImmanence", mCurrentImmanence);
		tag.setInteger("mTotalSpace", GetTotalSpace());
		tag.setInteger("mItemCount", GetItemCount());

		NBTTagList pendingList = new NBTTagList();

		pendingInvites.forEach((key, value) ->
		{
			NBTTagCompound entry = new NBTTagCompound();
			entry.setString("key", key);
			entry.setString("value", value.toString());
			pendingList.appendTag(entry);
		});

		tag.setTag("pendingInvites", pendingList);

		return tag;
	}

	public void Synchronise (PacketSynchronise.SynchroniseType type)
	{
		switch (type) {
			case DATA:
				SynchroniseData();
				break;
		}
	}

	public void SynchroniseData () {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null)
		{
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(mPlayerId);
			IMessage packet = new PacketSynchronise.PacketSynchroniseResponse(PacketSynchronise.SynchroniseType.DATA, mPlayerId, buildSynchroniseData());
			AAPacketHandler.CHANNEL.sendTo(packet, player);
		}
	}
}
