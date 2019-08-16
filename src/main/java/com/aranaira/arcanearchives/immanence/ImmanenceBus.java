package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.data.ServerNetwork;

import java.util.ArrayList;
import java.util.List;

public class ImmanenceBus implements IImmanenceBus {
	private List<IImmanenceSubscriber> subscribers = new ArrayList<>();
	private final ServerNetwork owner;
	private int lastTotalImmanence = -1;

	public ImmanenceBus (ServerNetwork owner) {
		this.owner = owner;
	}

	@Override
	public void generateImmanence () {

	}

	@Override
	public void consumeImmanence () {
		if (lastTotalImmanence == -1) {
			generateImmanence();
		}
	}

	@Override
	public int totalNetworkImmanence () {
		return lastTotalImmanence;
	}
}
