package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.immanence.IImmanenceBus;
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

	List<ServerNetwork> getContainedNetworks ();

	IImmanenceBus getImmanenceBus ();
}
