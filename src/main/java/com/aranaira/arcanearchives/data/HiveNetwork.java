package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.types.TileList.TileListIterable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class HiveNetwork extends ServerNetwork {
	private List<ServerNetwork> memberNetworks;
	private ServerNetwork ownerNetwork;

	public HiveNetwork (UUID ownerId, ServerNetwork ownerNetwork, List<ServerNetwork> memberNetworks) {
		super(ownerId);

		this.ownerNetwork = ownerNetwork;
		this.memberNetworks = memberNetworks;
	}

	public boolean validate (Hive hive) {
		if (!this.ownerNetwork.getUuid().equals(hive.getOwner())) {
			return false;
		}

		if (hive.getMembers().size() != memberNetworks.size()) {
			return false;
		}

		for (ServerNetwork member : memberNetworks) {
			if (!hive.getMembers().contains(member.getUuid())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void handleNewOwner () {
		super.handleNewOwner();
	}

	@Nullable
	@Override
	public ServerNetwork getOwnerNetwork () {
		return super.getOwnerNetwork();
	}

	@Nullable
	@Override
	public HiveNetwork getHiveNetwork () {
		return super.getHiveNetwork();
	}

	@Nullable
	@Override
	public EntityPlayer getPlayer () {
		return super.getPlayer();
	}

	@Override
	public UUID getUuid () {
		return super.getUuid();
	}

	@Override
	public void synchroniseData () {
		super.synchroniseData();
	}

	@Override
	public boolean isHiveNetwork () {
		return super.isHiveNetwork();
	}

	@Nullable
	@Override
	public List<ServerNetwork> getContainedNetworks () {
		return super.getContainedNetworks();
	}

	@Override
	public void addNetwork (ServerNetwork network) {
		super.addNetwork(network);
	}

	@Override
	public void removeNetwork (ServerNetwork network) {
		super.removeNetwork(network);
	}

	@Override
	public NBTTagCompound buildSynchroniseManifest () {
		return super.buildSynchroniseManifest();
	}

	@Override
	public void rebuildManifest () {
		super.rebuildManifest();
	}

	@Override
	public TileListIterable getManifestTileEntities () {
		return super.getManifestTileEntities();
	}

	@Override
	public NBTTagCompound buildSynchroniseData () {
		return super.buildSynchroniseData();
	}

	@Override
	public TileListIterable getValidTiles () {
		return super.getValidTiles();
	}

	@Override
	public UUID generateTileUuid () {
		return super.generateTileUuid();
	}

	@Override
	public void handleTileIdChange (UUID oldId, UUID newId) {
		super.handleTileIdChange(oldId, newId);
	}

	@Override
	public void addTile (ImmanenceTileEntity tileEntityInstance) {
		super.addTile(tileEntityInstance);
	}

	@Override
	public void removeTile (ImmanenceTileEntity te) {
		super.removeTile(te);
	}

	@Override
	public void removeTile (UUID tileID) {
		super.removeTile(tileID);
	}

	@Override
	public boolean containsTile (ImmanenceTileEntity tileEntityInstance) {
		return super.containsTile(tileEntityInstance);
	}

	@Override
	public boolean containsTile (UUID tileID) {
		return super.containsTile(tileID);
	}

	@Override
	public NBTTagCompound writeToSave () {
		return super.writeToSave();
	}

	@Override
	public void readFromSave (NBTTagCompound tag) {
		super.readFromSave(tag);
	}

	@Override
	public int getTotalCores () {
		return super.getTotalCores();
	}

	@Override
	public int getTotalResonators () {
		return super.getTotalResonators();
	}

	@Override
	public void rebuildTotals () {
		super.rebuildTotals();
	}
}
