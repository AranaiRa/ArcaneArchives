/*package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.api.immanence.IImmanenceBus;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.tilenetwork.NetworkAggregator;

import java.util.Collection;

public class ImmanenceGlobal {
  //public static ImmanenceGlobal instance = new ImmanenceGlobal();
  public static int immanenceTick = 20 * 10; // Every 10 seconds

  public static void tickImmanence(int tick) {
    Collection<Network> networks = NetworkAggregator.getNetworks();

    for (Network base : networks) {
      IImmanenceBus bus = base.getImmanenceBus();

      bus.generateImmanence(tick);
      bus.consumeImmanence(tick);
    }
  }
}*/
