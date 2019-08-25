package com.aranaira.arcanearchives.api.immanence;

public interface IImmanenceConsumer extends IImmanenceSubscriber {
	void acceptImmanence (int immanenceCount);

	int requiredImmanence ();

	boolean immanenceMet ();
}
