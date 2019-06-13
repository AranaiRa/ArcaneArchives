package com.aranaira.arcanearchives.client.gui.framework;

import java.util.List;

/**
 * For use with {@link ScrollEventManager} and is something that contains some number of {@link IScrollabe}
 * elements which can be drawn at up to {@link #getMaxYOffset()} y offset from their original location
 */
public interface IScrollableContainer {
	/**
	 * @return all {@link IScrollabe} elements in this container
	 */
	List<? extends IScrollabe> getScrollable ();

	/**
	 * @return the max y offset that any {@link IScrollabe} element in this container should be drawn
	 */
	int getMaxYOffset ();

	/**
	 * Will get called by {@link ScrollEventManager#registerListener(IScrollableContainer)} so that
	 * this container has the opportunity to cause events which change the scroll amount of ALL
	 * {@link IScrollableContainer}s registered with that {@link ScrollEventManager}
	 *
	 * @param scrollEventManager
	 */
	void registerScrollEventManager (ScrollEventManager scrollEventManager);
}
