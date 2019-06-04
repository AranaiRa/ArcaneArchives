package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.types.TileList;
import com.aranaira.arcanearchives.util.types.TileList.TileListIterable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public interface IServerNetwork extends IHiveBase {
	/**
	 * Simply returns the network's uuid.
	 */
	UUID getUuid ();
	World getWorld ();

	/**
	 * Function for retrieving the current list of valid tiles from the network.
	 */
	TileList.TileListIterable getValidTiles ();

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

	/**
	*	Functions used for interacting with NetworkSaveData.
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
	NBTTagCompound buildSynchroniseManifest ();
	void rebuildManifest ();
	TileList.TileListIterable getManifestTileEntities ();
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
	NBTTagCompound buildSynchroniseData ();
	NBTTagCompound buildHiveMembershipData ();
}
