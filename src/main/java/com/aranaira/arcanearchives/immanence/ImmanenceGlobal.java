package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.api.immanence.IImmanenceBus;
import com.aranaira.arcanearchives.data.DataHelper;

public class ImmanenceGlobal {
	public static ImmanenceGlobal instance = new ImmanenceGlobal();

	public void tickImmanence (int tick) {
		ServerList networks = DataHelper.getNetworks();

		for (IHiveBase base : networks) {
			IImmanenceBus bus = base.getImmanenceBus();

			bus.generateImmanence(tick);
			bus.consumeImmanence(tick);
		}
	}
}
