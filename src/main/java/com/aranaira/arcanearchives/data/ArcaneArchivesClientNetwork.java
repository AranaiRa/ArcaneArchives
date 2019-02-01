package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.common.ManifestItemHandler;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.packets.PacketSynchronise;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.types.ManifestList;
import com.aranaira.arcanearchives.util.types.Turple;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.aranaira.arcanearchives.common.ManifestItemHandler.ManifestEntry;

public class ArcaneArchivesClientNetwork
{
	/* Updated data via packet */
	private boolean mShared = false;
	private HashMap<String, UUID> pendingInvites = new HashMap<>();
	private UUID mOwnerId = null;
	private int mCurrentImmanence = 0;
	private int mTotalSpace = 0;
	private int mItemCount = 0;
	private ManifestItemHandler mManifestHandler = null;

	/* Internal values that are overwritten */
	public ManifestList manifestItems = new ManifestList(new ArrayList<>());
	//private TileList<ImmanenceTileEntity> mActualTiles = new TileList<>();

	ArcaneArchivesClientNetwork(UUID id)
	{
		this.mOwnerId = id;
		this.mManifestHandler = new ManifestItemHandler(this, manifestItems);
	}

	public ManifestItemHandler getManifestHandler()
	{
		return mManifestHandler;
	}

	public ManifestList getManifestItems ()
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
		PacketSynchronise.PacketSynchroniseRequest request = new PacketSynchronise.PacketSynchroniseRequest(PacketSynchronise.SynchroniseType.DATA, mOwnerId);
		AAPacketHandler.CHANNEL.sendToServer(request);
	}

	public void synchroniseManifest ()
	{
		PacketSynchronise.PacketSynchroniseRequest request = new PacketSynchronise.PacketSynchroniseRequest(PacketSynchronise.SynchroniseType.MANIFEST, mOwnerId);
		AAPacketHandler.CHANNEL.sendToServer(request);
	}

	public void deserializeManifest (NBTTagCompound tag) {
		manifestItems.clear();

		NBTTagList list = tag.getTagList("manifest", 10);

		for (NBTBase base : list) {
			NBTTagCompound itemEntry = (NBTTagCompound) base;
			int dimension = itemEntry.getInteger("dimension");
			List<BlockPos> positions = new ArrayList<>();
			NBTTagList posList = itemEntry.getTagList("positions", 4);
			for (NBTBase posBase : posList) {
				positions.add(BlockPos.fromLong(((NBTTagLong) posBase).getLong()));
			}
			ItemStack stack = LargeItemNBTUtil.readFromNBT(itemEntry);
			ManifestEntry thisEntry = new ManifestEntry(stack, dimension, positions);

			manifestItems.add(thisEntry);
		}
	}

	public void deserializeData (NBTTagCompound tag)
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

	/*public void refreshTiles () {
		mActualTiles.clear();

		WorldClient world = Minecraft.getMinecraft().world;
		int curDim = world.provider.getDimension();

		for (Tuple<Integer, BlockPos> tup : mNetworkTiles) {
			int dim = tup.val1;
			BlockPos pos = tup.val2;

			if (curDim != dim) continue;

			if (world.isBlockLoaded(pos)) {
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof ImmanenceTileEntity) {
					mActualTiles.add((ImmanenceTileEntity) te);
				}
			}
		}
	}*/

}

