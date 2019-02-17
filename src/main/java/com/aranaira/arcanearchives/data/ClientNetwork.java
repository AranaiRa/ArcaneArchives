package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.inventory.handlers.ManifestItemHandler;
import com.aranaira.arcanearchives.network.AAPacketHandler;
import com.aranaira.arcanearchives.network.PacketManifest;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import com.aranaira.arcanearchives.util.types.ManifestList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClientNetwork
{
	/* Internal values that are overwritten */
	public ManifestList manifestItems = new ManifestList(new ArrayList<>());

	/* Updated data via packet */
	private boolean mShared = false;
	private HashMap<String, UUID> pendingInvites = new HashMap<>();
	private UUID mOwnerId = null;
	private int mCurrentImmanence = 0;
	private int mTotalSpace = 0;
	private int mItemCount = 0;

	private int totalResonators = 0;
	private int totalCores = 0;

	private ManifestItemHandler mManifestHandler = null;
	//private TileList<ImmanenceTileEntity> mActualTiles = new TileList<>();

	ClientNetwork(UUID id)
	{
		this.mOwnerId = id;
		this.mManifestHandler = new ManifestItemHandler(manifestItems);
	}

	public ManifestItemHandler getManifestHandler()
	{
		return mManifestHandler;
	}

	public ManifestList getManifestItems()
	{
		return manifestItems;
	}

	public int GetImmanence()
	{
		return mCurrentImmanence;
	}

	// TODO get this as a value form the server
	public int CountTileEntities(Class clazz)
	{
		return 0;
	}

	// TODO this needs to be passed from the server
	public List<NonNullList<ItemStack>> GetItemsOnNetwork()
	{
		return null;
	}

	// This requests a synchronise packet from the server
	// but does not include the manifest info.
	public void synchroniseData()
	{
		PacketManifest.PacketSynchroniseRequest request = new PacketManifest.PacketSynchroniseRequest(PacketManifest.SynchroniseType.DATA, mOwnerId);
		AAPacketHandler.CHANNEL.sendToServer(request);
	}

	public void synchroniseManifest()
	{
		PacketManifest.PacketSynchroniseRequest request = new PacketManifest.PacketSynchroniseRequest(PacketManifest.SynchroniseType.MANIFEST, mOwnerId);
		AAPacketHandler.CHANNEL.sendToServer(request);
	}

	public void deserializeManifest(NBTTagCompound tag)
	{
		manifestItems.clear();

		NBTTagList list = tag.getTagList("manifest", 10);

		for(NBTBase base : list)
		{
			NBTTagCompound itemEntry = (NBTTagCompound) base;
			int dimension = itemEntry.getInteger("dimension");
			List<ManifestEntry.ItemEntry> entries = new ArrayList<>();
			NBTTagList entryList = itemEntry.getTagList("entries", Constants.NBT.TAG_COMPOUND);
			for(NBTBase entry : entryList)
			{
				entries.add(ManifestEntry.ItemEntry.deserializeNBT((NBTTagCompound) entry));
			}
			ItemStack stack = LargeItemNBTUtil.readFromNBT(itemEntry);
			ManifestEntry thisEntry = new ManifestEntry(stack, dimension, entries);

			manifestItems.add(thisEntry);
		}

		int dim = getPlayer().dimension;

		manifestItems.sort((turp1, turp2) ->
		{
			int comp = turp1.getStack().getDisplayName().compareToIgnoreCase(turp2.getStack().getDisplayName());
			boolean t1 = turp1.getDimension() == dim;
			boolean t2 = turp2.getDimension() == dim;
			if(comp != 0) return comp;
			if(t1 == t2) return comp;
			if(t1) return -1;
			return 1;
		});
	}

	public void deserializeData(NBTTagCompound tag)
	{
		this.mShared = tag.getBoolean("mShared");
		this.mOwnerId = UUID.fromString(tag.getString("mOwnerId"));
		this.mCurrentImmanence = tag.getInteger("mCurrentImmanence");
		this.mTotalSpace = tag.getInteger("mTotalSpace");
		this.mItemCount = tag.getInteger("mItemCount");
		this.pendingInvites.clear();

		for(NBTBase nbt : tag.getTagList("pendingInvites", 10))
		{
			if(!(nbt instanceof NBTTagCompound)) continue;

			NBTTagCompound tag2 = (NBTTagCompound) nbt;
			this.pendingInvites.put(tag2.getString("key"), UUID.fromString(tag2.getString("")));
		}
	}

	public UUID getPlayerID()
	{
		return mOwnerId;
	}

	// Okay the client probably needs this
	public int GetItemCount()
	{
		return mItemCount;
	}

	// And this
	public int GetTotalSpace()
	{
		return mTotalSpace;
	}

	// TODO
	public List<ItemStack> GetAllItemsOnNetwork()
	{
		return null;
	}

	// TODO
	public List<ItemStack> GetFilteredItems(String s)
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	public static class Tag {

	}
}

