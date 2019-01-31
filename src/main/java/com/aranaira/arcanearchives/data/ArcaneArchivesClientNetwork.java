package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.common.ManifestItemHandler;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.AACollectors;
import com.aranaira.arcanearchives.util.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.*;

public class ArcaneArchivesClientNetwork
{
	/* Updated data via packet */
	private boolean mShared = false;
	private HashMap<String, UUID> pendingInvites = new HashMap<>();
	private UUID mOwnerId = null;
	private List<Tuple<Integer, BlockPos>> mNetworkTiles = new ArrayList<>();
	private int mCurrentImmanence = 0;
	private ManifestItemHandler mManifestHandler = null;

	/* Internal values that are overwritten */
	//private TileList<ImmanenceTileEntity> mActualTiles = new TileList<>();

	public ManifestItemHandler getManifestHandler () {
		return mManifestHandler;
	}

	ArcaneArchivesClientNetwork (UUID id) {
		this.mOwnerId = id;
		this.mManifestHandler = new ManifestItemHandler();
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

	// TODO:
	public void deserializeNBT(NBTTagCompound tag)
	{
		this.mShared = tag.getBoolean("mShared");
		this.mOwnerId = UUID.fromString(tag.getString("mOwnerId"));
		this.mCurrentImmanence = tag.getInteger("mCurrentImmanence");
		this.mManifestHandler.deserializeNBT(tag.getCompoundTag("maniafest"));
		this.pendingInvites.clear();

		for (NBTBase nbt : tag.getTagList("pendingInvites", 10)) {
			if (!(nbt instanceof NBTTagCompound)) continue;

			NBTTagCompound tag2 = (NBTTagCompound) nbt;
			this.pendingInvites.put(tag2.getString("key"), UUID.fromString(tag2.getString("")));
		}

		/*this.mNetworkTiles.clear();

		for (NBTBase nbt : tag.getTagList("tiles", 10)) {
			if (!(nbt instanceof NBTTagCompound)) continue;

			NBTTagCompound tag2 = (NBTTagCompound) nbt;
			Tuple<Integer, BlockPos> tup = new Tuple<>(tag2.getInteger("dimension"), BlockPos.fromLong(tag2.getLong("pos")));
			this.mNetworkTiles.add(tup);
		}*/
	}

	public UUID getPlayerID()
	{
		return mOwnerId;
	}

	// Okay the client probably needs this
	public int GetTotalItems()
	{
		return 0;
	}

	// And this
	public int GetTotalSpace()
	{
		return 0;
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

