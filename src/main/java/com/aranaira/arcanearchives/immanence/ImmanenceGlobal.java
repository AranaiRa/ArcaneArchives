package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.NetworkSaveData;
import com.aranaira.arcanearchives.data.ServerNetwork;

import java.util.List;

public class ImmanenceGlobal {
	public static ImmanenceGlobal instance = new ImmanenceGlobal();

	private List<ServerNetwork> gatherNetworks () {
		NetworkSaveData saveData = DataHelper.getNetworkData();

		return null;
	}
}
