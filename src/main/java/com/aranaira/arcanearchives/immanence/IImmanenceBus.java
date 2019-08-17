package com.aranaira.arcanearchives.immanence;

public interface IImmanenceBus {
	void generateImmanence (int currentTick);
	void consumeImmanence (int currentTick);
	int totalNetworkImmanence ();
	int leftoverImmanence ();
}
