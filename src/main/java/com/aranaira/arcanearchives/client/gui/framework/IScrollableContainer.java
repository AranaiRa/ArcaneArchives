package com.aranaira.arcanearchives.client.gui.framework;

import java.util.List;

public interface IScrollableContainer {
	List<? extends IScrollabe> getScrollable();

	int getMaxYOffset();
}
