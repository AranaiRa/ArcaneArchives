package com.aranaira.arcanearchives.immanence;

public interface IImmanenceGenerator extends IImmanenceSubscriber {
	boolean canGenerateImmanence ();
	int generateImmanence ();
}
