package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.api.immanence.IImmanenceBus;
import com.aranaira.arcanearchives.api.immanence.IImmanenceSubscriber;
import com.aranaira.arcanearchives.immanence.ImmanenceBus;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.lists.CombinedTileList;
import com.aranaira.arcanearchives.types.lists.ITileList;
import com.aranaira.arcanearchives.util.TileUtils;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HiveNetwork implements IHiveBase {
	private List<ServerNetwork> memberNetworks;
	private ServerNetwork ownerNetwork;
	private ImmanenceBus immanenceBus;

	public HiveNetwork (ServerNetwork ownerNetwork, List<ServerNetwork> memberNetworks) {
		this.ownerNetwork = ownerNetwork;
		this.memberNetworks = memberNetworks;
		this.immanenceBus = new ImmanenceBus(this);
	}

	private void applyToHive (Consumer<ServerNetwork> consumer) {
		consumer.accept(ownerNetwork);

		memberNetworks.forEach(consumer);
	}

	private boolean applyToHive (Predicate<ServerNetwork> predicate) {
		if (predicate.test(ownerNetwork)) {
			return true;
		}

		for (ServerNetwork network : memberNetworks) {
			if (predicate.test(network)) {
				return true;
			}
		}

		return false;
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
	public boolean anyLoaded () {
		return applyToHive(ServerNetwork::anyLoaded);
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

	@Override
	public Iterable<IteRef> getImmananceTiles () {
		return TileUtils.filterAssignableClass(getTiles(), IImmanenceSubscriber.class);
	}

	@Override
	public List<ServerNetwork> getContainedNetworks () {
		return memberNetworks;
	}

	@Override
	public IImmanenceBus getImmanenceBus () {
		return immanenceBus;
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
