package com.aranaira.arcanearchives.api.immanence;

public interface IImmanenceBus {
	void generateImmanence (int currentTick);

	void consumeImmanence (int currentTick);

	int getTotalImmanence ();
	int getLeftoverImmanence ();
	int getBaseImmanence ();
	float getMultiplier ();
}
