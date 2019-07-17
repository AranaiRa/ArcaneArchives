package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.util.TileUtils;
import com.aranaira.arcanearchives.util.types.CombinedTileList;
import com.aranaira.arcanearchives.util.types.ITileList;
import com.aranaira.arcanearchives.util.types.IteRef;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
	public ITileList getTiles () {
		List<ITileList> tiles = new ArrayList<>();
		for (ServerNetwork network : getCombinedNetworks()) {
			tiles.add(network.getTiles());
		}
		return new CombinedTileList(tiles);
	}

	@Override
	public Iterable<IteRef> getValidTiles () {
		return TileUtils.filterValid(getTiles());
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
