package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.IManifestTileEntity;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.types.*;
import com.aranaira.arcanearchives.util.types.TileList.TileListIterable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HiveNetwork implements IHiveBase {
	private List<ServerNetwork> memberNetworks;
	private ServerNetwork ownerNetwork;

	public HiveNetwork (ServerNetwork ownerNetwork, List<ServerNetwork> memberNetworks) {
		this.ownerNetwork = ownerNetwork;
		this.memberNetworks = memberNetworks;
	}

	// TODO: ????
	private List<ServerNetwork> getCombinedNetworks () {
		List<ServerNetwork> combined = new ArrayList<>(getContainedNetworks());
		combined.add(ownerNetwork);
		return combined;
	}

	@Nullable
	@Override
	public ServerNetwork getOwnerNetwork () {
		return ownerNetwork;
	}

	@Nullable
	@Override
	public HiveNetwork getHiveNetwork () {
		return this;
	}

	@Override
	public boolean isHiveMember () {
		return true;
	}

	@Override
	public boolean isHiveNetwork () {
		return true;
	}

	@Override
	public ITileList getValidTiles () {
		List<ITileList> tiles = new ArrayList<>();
		for (ServerNetwork network : getCombinedNetworks()) {
			tiles.add(network.getTiles());
		}
		return new CombinedTileList(tiles);
	}

	@Nullable
	@Override
	public List<ServerNetwork> getContainedNetworks () {
		return memberNetworks;
	}

	@Nullable
	public ServerNetwork getContainedNetwork (EntityPlayer player) {
		for (ServerNetwork network : getCombinedNetworks()) {
			if (network.getUuid().equals(player.getUniqueID())) {
				return network;
			}
		}

		return null;
	}
}
