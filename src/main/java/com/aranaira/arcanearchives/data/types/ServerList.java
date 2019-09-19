package com.aranaira.arcanearchives.data.types;

import com.aranaira.arcanearchives.data.types.HiveNetwork;
import com.aranaira.arcanearchives.data.types.IHiveBase;
import com.aranaira.arcanearchives.data.types.ServerNetwork;
import com.google.common.collect.Iterators;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class ServerList implements Iterable<IHiveBase> {
	public List<ServerNetwork> independents;
	public List<HiveNetwork> hives;

	public ServerList (List<ServerNetwork> independents, List<HiveNetwork> hives) {
		this.independents = independents;
		this.hives = hives;
	}

	@Override
	@Nonnull
	public Iterator<IHiveBase> iterator () {
		return Iterators.concat(independents.iterator(), hives.iterator());
	}
}
