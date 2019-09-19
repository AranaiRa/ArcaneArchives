package com.aranaira.arcanearchives.data.types;

import com.aranaira.arcanearchives.api.immanence.IImmanenceBus;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.lists.ITileList;

import javax.annotation.Nullable;
import java.util.List;

public interface IHiveBase {
	ServerNetwork getOwnerNetwork ();

	@Nullable
	HiveNetwork getHiveNetwork ();

	boolean isHiveMember ();

	boolean anyLoaded ();

	ITileList getTiles ();

	Iterable<IteRef> getValidTiles ();

	Iterable<IteRef> getImmananceTiles ();

	List<ServerNetwork> getContainedNetworks ();

	IImmanenceBus getImmanenceBus ();
}
