package com.aranaira.arcanearchives.data.types;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import com.aranaira.arcanearchives.types.iterators.TileListIterable;
import com.aranaira.arcanearchives.types.lists.ManifestList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public interface IServerNetwork extends IHiveBase {
	/**
	 * Simply returns the network's uuid.
	 */
	UUID getUuid ();

	World getWorld ();

	/**
	 * Attempts to return a unique tile id.
	 */
	UUID generateTileUuid ();

	/**
	 * TODO: Honestly, I'm not sure what this does any more.
	 * Theoretically it removes the tile under its old uuid and
	 * inserts it under its new uuid. I don't know what circumstances
	 * there could be that would involve this actually happening
	 * though.
	 * I guess the idea is that the tile was given a new ID
	 * from the network, but then it properly loaded its
	 * actual state with its correct ID. This should never
	 * happen.
	 */
	void handleTileIdChange (UUID oldId, UUID newId);

	/**
	 * Function for adding tile entities to the network
	 */
	void addTile (ImmanenceTileEntity tileEntityInstance);

	/**
	 * Block of functions used for determining if this network contains an ITE or tileId.
	 */
	boolean containsTile (ImmanenceTileEntity tileEntityInstance);

	boolean containsTile (UUID tileID);

	void updateTile (ImmanenceTileEntity tileEntityInstance);

	/**
	 * Functions used for interacting with NetworkSaveData.
	 */
	NBTTagCompound writeToSave ();

	void readFromSave (NBTTagCompound tag);

	/**
	 * Contains code relating to per-player limits for resonators (and Matrices)
	 */
	int getTotalCores ();

	int getTotalResonators ();

	void rebuildTotals ();

	/**
	 * Function that should be improved for the manifest.
	 * Converts the manifestItems list into an NBT form
	 * for return to the client.
	 */
	ISerializeByteBuf<ManifestList> buildSynchroniseManifest ();

	void rebuildManifest ();

	TileListIterable getManifestTileEntities ();

	EntityPlayer getPlayer ();

	void removeTile (ImmanenceTileEntity te);

	void removeTile (UUID tileID);

	void synchroniseData ();

	void synchroniseHiveInfo ();

	/**
	 * Code specifically for synchronising data to the player.
	 * Currently this only contains the total number of
	 * resonators and matrix cores.
	 */
	SynchroniseInfo buildSynchroniseData ();

	ISerializeByteBuf<HiveMembershipInfo> buildHiveMembershipData ();
}
