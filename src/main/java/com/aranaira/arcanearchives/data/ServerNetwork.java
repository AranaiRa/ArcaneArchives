package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.inventory.handlers.ManifestItemHandler;
import com.aranaira.arcanearchives.network.AAPacketHandler;
import com.aranaira.arcanearchives.network.PacketNetwork;
import com.aranaira.arcanearchives.tileentities.*;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.types.*;
import com.google.common.annotations.Beta;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.*;

public class ServerNetwork implements INBTSerializable<NBTTagCompound>
{
	public HashMap<String, UUID> pendingInvites = new HashMap<>();
	public ManifestList manifestItems = new ManifestList(new ArrayList<>());
	public ManifestItemHandler mManifestHandler = null;
	private UUID mPlayerId;
	private AAWorldSavedData mParent;
	private TileList mNetworkTiles = new TileList(new ArrayList<>());
	private int mCurrentImmanence;
	private boolean mNeedsToBeUpdated = true;

	private int totalCores = 0;
	private int totalResonators = 0;

	private ServerNetwork(UUID id)
	{
		mPlayerId = id;
		mManifestHandler = new ManifestItemHandler(manifestItems);
	}

	public static ServerNetwork newNetwork(UUID playerID)
	{
		return new ServerNetwork(playerID);
	}

	public static ServerNetwork fromNBT(NBTTagCompound data)
	{
		ServerNetwork network = new ServerNetwork(null);
		network.deserializeNBT(data);
		return network;
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
			TotalGeneration += ITE.immanenceGeneration;
		}

		// Avoid the priority as it creates a new list
		// only use if you ACTUALLY care about priority
		// otherwise it would be best to resort the list
		// whenever a new tile is added, based on priority
		for(ImmanenceTileEntity ITE : GetBlocks())
		{
			int tmpDrain = ITE.immanenceDrain;
			if(TotalGeneration > (TotalDrain + tmpDrain))
			{
				TotalDrain += tmpDrain;
				ITE.isDrainPaid = true;
			} else
			{
				ITE.isDrainPaid = false;
			}
		}
		mCurrentImmanence = TotalGeneration - TotalDrain;
		mNeedsToBeUpdated = false;
	}

	public TileList GetTileEntitiesByPriority()
	{
		return this.mNetworkTiles.sorted((o1, o2) ->
		{
			if(o1.networkPriority > o2.networkPriority) return 1;
			else return -1;
		});
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
		return mNetworkTiles.filterValid();
	}

	public void AddTileToNetwork(ImmanenceTileEntity tileEntityInstance)
	{
		if(mNetworkTiles.contains(tileEntityInstance)) return;

		mNetworkTiles.add(tileEntityInstance);
		tileEntityInstance.hasBeenAddedToNetwork = true;

		mNeedsToBeUpdated = true;
		UpdateImmanence();

		if (tileEntityInstance instanceof RadiantResonatorTileEntity || tileEntityInstance instanceof MatrixCoreTileEntity) {
			rebuildTotals();
		}
	}

	public void RemoveTileFromNetwork(ImmanenceTileEntity tileEntityInstance)
	{
		mNetworkTiles.remove(tileEntityInstance);

		mNeedsToBeUpdated = true;
		UpdateImmanence();

		if (tileEntityInstance instanceof RadiantResonatorTileEntity || tileEntityInstance instanceof MatrixCoreTileEntity) {
			rebuildTotals();
		}
	}

	public boolean NetworkContainsTile(ImmanenceTileEntity tileEntityInstance)
	{
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

	public ServerNetwork setParent(AAWorldSavedData parent)
	{
		mParent = parent;
		MarkUnsaved();
		return this;
	}

	public UUID getPlayerID()
	{
		return mPlayerId;
	}

	public int getTotalCores()
	{
		return totalCores;
	}

	public int getTotalResonators()
	{
		return totalResonators;
	}

	public void rebuildManifest()
	{
		manifestItems.clear();

		List<ManifestItemEntry> preManifest = new ArrayList<>();
		Set<RadiantChestTileEntity> done = new HashSet<>();

		for(ImmanenceTileEntity ite : GetRadiantChests())
		{
			RadiantChestTileEntity chest = (RadiantChestTileEntity) ite;
			if(done.contains(chest)) continue;

			int dimId = chest.getWorld().provider.getDimension();
			for(ItemStack is : new SlotIterable(chest.getInventory()))
			{
				if(is.isEmpty()) continue;

				preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(chest.getPos(), chest.getChestName(), is.getCount())));
			}

			done.add(chest);
		}

		List<ManifestEntry> consolidated = ItemStackConsolidator.ConsolidateManifest(preManifest);
		manifestItems.addAll(consolidated);
	}

	public void rebuildTotals () {
		int origResonators = totalResonators;
		int origCores = totalCores;

		totalResonators = 0;
		totalCores = 0;

		for (ImmanenceTileEntity ite : GetBlocks()) {
			if (ite instanceof RadiantResonatorTileEntity) {
				totalResonators++;
			} else if (ite instanceof MatrixCoreTileEntity) {
				totalCores++;
			}
		}

		if (origCores != totalCores || origResonators != totalResonators) {
			if (!synchroniseData()) {
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(this::synchroniseData);
			}
		}
	}

	public NBTTagCompound buildSynchroniseManifest()
	{
		// Step one: iterate loaded chests and get item stacks.
		rebuildManifest();

		NBTTagList manifest = new NBTTagList();

		for(ManifestEntry entry : manifestItems)
		{
			NBTTagCompound itemEntry = new NBTTagCompound();
			LargeItemNBTUtil.writeToNBT(itemEntry, entry.getStack());
			NBTTagList entries = new NBTTagList();
			for(ManifestEntry.ItemEntry iEntry : entry.getEntries())
			{
				entries.appendTag(iEntry.serializeNBT());
			}
			itemEntry.setTag(NetworkTags.ENTRIES, entries);
			itemEntry.setInteger(NetworkTags.DIMENSION, entry.getDimension());
			manifest.appendTag(itemEntry);
		}

		NBTTagCompound result = new NBTTagCompound();
		result.setTag(NetworkTags.MANIFEST, manifest);

		return result;
	}

	public NBTTagCompound buildSynchroniseData()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(NetworkTags.IMMANENCE, mCurrentImmanence);
		tag.setInteger(NetworkTags.TOTAL_SPACE, 0); // GetTotalSpace());
		tag.setInteger(NetworkTags.ITEM_COUNT, 0); //GetItemCount());

		NBTTagList pendingList = new NBTTagList();

		pendingInvites.forEach((key, value) ->
		{
			NBTTagCompound entry = new NBTTagCompound();
			entry.setString(NetworkTags.INVITE_KEY, key);
			entry.setString(NetworkTags.INVITE_VALUE, value.toString());
			pendingList.appendTag(entry);
		});

		rebuildTotals();

		tag.setTag(NetworkTags.INVITES_PENDING, pendingList);

		tag.setInteger(NetworkTags.TOTAL_RESONATORS, totalResonators);
		tag.setInteger(NetworkTags.TOTAL_CORES, totalCores);

		return tag;
	}

	public void Synchronise(PacketNetwork.SynchroniseType type)
	{
		switch(type)
		{
			case DATA:
				synchroniseData();
				break;
		}
	}

	public boolean synchroniseData()
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(server != null)
		{
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(mPlayerId);
			//noinspection ConstantConditions
			if (player == null) return false;
			IMessage packet = new PacketNetwork.PacketSynchroniseResponse(PacketNetwork.SynchroniseType.DATA, mPlayerId, buildSynchroniseData());
			AAPacketHandler.CHANNEL.sendTo(packet, player);
			return true;
		}

		return false;
	}

	public static class ManifestItemEntry
	{
		public ItemStack stack;
		public int dim;
		public ManifestEntry.ItemEntry entry;

		public ManifestItemEntry(ItemStack stack, int dim, ManifestEntry.ItemEntry entry)
		{
			this.stack = stack;
			this.dim = dim;
			this.entry = entry;
		}
	}
}
