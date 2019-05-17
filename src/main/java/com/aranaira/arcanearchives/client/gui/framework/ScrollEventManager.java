package com.aranaira.arcanearchives.client.gui.framework;

import com.aranaira.arcanearchives.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages scrolling events between {@link IScrollableContainer} which contain elements which are {@link IScrollabe}
 */
public class ScrollEventManager {
	/**
	 * represents which step from the top of the scroll bar we're currently at, so if the number of steps change
	 * we can update the position of the nub
	 */
	private int currentIncrement;
	/**
	 * the current total number of steps for the scroll bar nub
	 */
	private int numSteps;
	/**
	 * The scrollable GUI elements that want to get scrolled by this manager
	 */
	private List<IScrollableContainer> listeners;

	public ScrollEventManager() {
		listeners = new ArrayList<>();
	}

	/**
	 * Add listener to the list of things that are interested in scroll events passed through this manager
	 *
	 * @param listener {@link IScrollableContainer}
	 */
	public void registerListener(IScrollableContainer listener) {
		listeners.add(listener);
		listener.registerScrollEventManager(this);
	}

	/**
	 * Increase the scroll of all {@link IScrollableContainer} elements associated with this manager
	 * by increment
	 *
	 * @param increment increment
	 */
	public void incrementBy(int increment) {
		if (increment > 0) {
			currentIncrement = Math.min(numSteps, currentIncrement + increment);
		}
	}

	/**
	 * Increase the scroll of all {@link IScrollableContainer} elements associated with this manager
	 * by one
	 */
	public void nextIncrement() {
		incrementBy(1);
	}

	/**
	 * Decrease the scroll of all {@link IScrollableContainer} elements associated with this manager
	 * by decrement
	 *
	 * @param decrement decrement
	 */
	public void decrementBy(int decrement) {
		if (decrement > 0) {
			currentIncrement = Math.max(0, currentIncrement - decrement);
		}
	}

	/**
	 * Decrease the scroll of all {@link IScrollableContainer} elements associated with this manager
	 * by one
	 */
	public void nextDecrement() {
		decrementBy(1);
	}

	/**
	 * Set the number of increments in the {@link IScrollableContainer} managed by this manager
	 */
	public void setNumSteps (int numSteps) {
		this.numSteps = numSteps;
	}

	/**
	 * Update the y offset for current {@link #numSteps} and {@link #currentIncrement} in each
	 * {@link IScrollableContainer#getScrollable()} in {@link #listeners}
	 */
	public void updateYOffsets() {
		for (IScrollableContainer scrollableContainer : listeners) {
			int stepSize = MathUtils.intDivisionCeiling(scrollableContainer.getMaxYOffset(), numSteps);
			int yOffset = Math.min(scrollableContainer.getMaxYOffset(), stepSize * currentIncrement);
			scrollableContainer.getScrollable().forEach(scrollableElement -> scrollableElement.updateY(yOffset));
		}
	}
}
