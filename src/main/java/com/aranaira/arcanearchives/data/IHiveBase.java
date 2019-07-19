package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.util.types.lists.ITileList;
import com.aranaira.arcanearchives.util.types.IteRef;

import javax.annotation.Nullable;
import java.util.List;

public interface IHiveBase {
	@Nullable
	ServerNetwork getOwnerNetwork ();

	@Nullable
	HiveNetwork getHiveNetwork ();

	boolean isHiveMember ();

	boolean isHiveNetwork ();

	ITileList getTiles ();

	Iterable<IteRef> getValidTiles ();

	@Nullable
	List<ServerNetwork> getContainedNetworks ();
}
