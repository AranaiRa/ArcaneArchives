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
	 * the number of steps for page up/down
	 */
	private int stepsPerPage;

	/**
	 * The scrollable GUI elements that want to get scrolled by this manager
	 */
	private List<IScrollableContainer> listeners;

	public ScrollEventManager () {
		listeners = new ArrayList<>();
		stepsPerPage = 1;
	}

	/**
	 * Add listener to the list of things that are interested in scroll events passed through this manager
	 *
	 * @param listener {@link IScrollableContainer}
	 */
	public void registerListener (IScrollableContainer listener) {
		listener.registerScrollEventManager(this);
		listeners.add(listener);
	}

	/**
	 * Remove listener to the list of things that are interested in scroll events passed through this manager
	 *
	 * @param listener {@link IScrollableContainer}
	 */
	public void unregisterListener (IScrollableContainer listener) {
		listeners.remove(listener);
	}

	/**
	 * Set {@link IScrollabe#updateY(int)} of all elements to have their y offset set to
	 * scrollPercent of {@link IScrollableContainer#getMaxYOffset()} of the {@link IScrollableContainer}
	 * they're a part of.
	 *
	 * @param scrollPercent percent of {@link IScrollableContainer#getMaxYOffset()} to scroll to
	 */
	public void setScrollPercent (float scrollPercent) {
		// bound effectiveScrollPercent to [0, 1] inclusive
		float effectiveScrollPercent = Math.max(0f, Math.min(1f, scrollPercent));
		// always update currentIncrement so that if we start using arrow keys after
		// calling this function things will work "correctly"
		currentIncrement = Math.round(effectiveScrollPercent * numSteps);

		// ignore currentIncrement and do exact scrolling
		for (IScrollableContainer scrollableContainer : listeners) {
			// round to nearest "pixel"
			int yOffset = Math.round(effectiveScrollPercent * scrollableContainer.getMaxYOffset());
			scrollableContainer.getScrollable().forEach(scrollableElement -> scrollableElement.updateY(yOffset));
		}
	}

	/**
	 * @param stepsPerPage set {@link #stepsPerPage} u
	 */
	public void setStepsPerPage (int stepsPerPage) {
		this.stepsPerPage = stepsPerPage;
	}

	/**
	 * Scroll up of all {@link IScrollableContainer} elements associated with this manager
	 * by one page defined by {@link #stepsPerPage}
	 */
	public void pageUp () {
		currentIncrement = Math.max(0, currentIncrement - stepsPerPage);
		updateYOffsets();
	}

	/**
	 * Scroll down of all {@link IScrollableContainer} elements associated with this manager
	 * by one page defined by {@link #stepsPerPage}
	 */
	public void pageDown () {
		currentIncrement = Math.min(numSteps, currentIncrement + stepsPerPage);
		updateYOffsets();
	}

	/**
	 * Increase the scroll of all {@link IScrollableContainer} elements associated with this manager
	 * by one step
	 */
	public void arrowDown () {
		if (currentIncrement < numSteps) {
			++currentIncrement;
			updateYOffsets();
		}
	}

	/**
	 * Decrease the scroll of all {@link IScrollableContainer} elements associated with this manager
	 * by one step
	 */
	public void arrowUp () {
		if (currentIncrement > 0) {
			--currentIncrement;
			updateYOffsets();
		}
	}

	/**
	 * Set the number of increments in the {@link IScrollableContainer} managed by this manager
	 */
	public void setNumSteps (int numSteps) {
		this.numSteps = numSteps;
		updateYOffsets();
	}

	/**
	 * Update the y offset for current {@link #numSteps} and {@link #currentIncrement} in each
	 * {@link IScrollableContainer#getScrollable()} in {@link #listeners}
	 */
	private void updateYOffsets () {
		for (IScrollableContainer scrollableContainer : listeners) {
			int stepSize = MathUtils.intDivisionCeiling(scrollableContainer.getMaxYOffset(), numSteps);
			int yOffset = Math.min(scrollableContainer.getMaxYOffset(), stepSize * currentIncrement);
			scrollableContainer.getScrollable().forEach(scrollableElement -> scrollableElement.updateY(yOffset));
		}
	}
}
