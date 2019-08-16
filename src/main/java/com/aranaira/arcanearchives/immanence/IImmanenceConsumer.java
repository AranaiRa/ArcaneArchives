package com.aranaira.arcanearchives.immanence;

public interface IImmanenceConsumer extends IImmanenceSubscriber {
	void acceptImmanence (int immanenceCount);
	int requiredImmanence ();

	boolean immanenceMet ();
}
