package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.MathUtils;
import net.minecraft.client.gui.GuiButton;

public class ScrollBar extends GuiButton {
	/**
	 * The nubbin in the scroll bar
	 */
	public TexturedButton mNub;

	/**
	 * represents which step from the top of the scroll bar we're currently at, so if the number of steps change
	 * we can update the position of the nub
	 */
	private int currentIncrement;
	/**
	 * no matter the number of steps, this is the max top offset for the nub, aka when it's at the bottom of the
	 * scroll bar
	 */
	private int maxNubTopOffset;
	/**
	 * size of a step in pixels
	 */
	private int stepSize;
	/**
	 * the current total number of steps for the scroll bar nub
	 */
	private int numSteps;

	public ScrollBar (int startId, int leftOffset, int topOffset, int bottomOffset) {
		super(startId, leftOffset, topOffset, TexturedButton.getWidth(0), bottomOffset - topOffset, "");

		this.mNub = new TexturedButton(startId + 1, 0, leftOffset, topOffset);

		this.currentIncrement = 0;
		this.numSteps = 0;

		this.maxNubTopOffset = bottomOffset - this.mNub.height;
	}

	/**
	 * Something has happened that we need to update where in the scroll bar we need to draw the nub
	 */
	private void updateNubOffset () {
		// with a new step size update current nub position, limited by max offset
		mNub.y = Math.min(maxNubTopOffset, y + (stepSize * currentIncrement));
	}

	/**
	 * recompute the step size so that there is given possible steps along the scroll bar
	 */
	public void setNumSteps (int numSteps) {
		this.numSteps = numSteps;
		// step size is ceiling of height of scroll bar divided by desired number of steps
		stepSize = MathUtils.intDivisionCeiling(maxNubTopOffset - y, numSteps);
		updateNubOffset();
	}

	/**
	 * update nub position for scrolling down by one step
	 */
	public void scrollDown () {
		if (currentIncrement < numSteps) {
			++currentIncrement;
			updateNubOffset();
		}
	}

	/**
	 * update nub position for scrolling up by one step
	 */
	public void scrollUp () {
		if (currentIncrement > 0) {
			--currentIncrement;
			updateNubOffset();
		}
	}
}
