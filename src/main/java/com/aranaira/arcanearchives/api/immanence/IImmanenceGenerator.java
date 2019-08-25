package com.aranaira.arcanearchives.api.immanence;

public interface IImmanenceGenerator extends IImmanenceSubscriber {
	boolean canGenerateImmanence ();

	int generateImmanence ();
}
