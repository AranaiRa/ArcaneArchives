package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.client.gui.framework.IScrollabe;
import com.aranaira.arcanearchives.client.gui.framework.IScrollableContainer;
import com.aranaira.arcanearchives.client.gui.framework.ScrollEventManager;
import net.minecraft.client.gui.GuiButton;

import java.util.Collections;
import java.util.List;

public class ScrollBar extends GuiButton implements IScrollableContainer {
	private class ScrollBarNub extends TexturedButton implements IScrollabe {

		/**
		 * Create a {@link GuiButton} with a texture from {@link TexturedButton#BUTTON_TEXTURES}
		 *
		 * @param buttonId  id for this {@link GuiButton}
		 * @param textureId index (zero based) of texture in {@link TexturedButton#BUTTON_TEXTURES}
		 * @param x         x position on screen to draw this button
		 * @param y         y position on screen to draw this button
		 */
		public ScrollBarNub (int buttonId, int textureId, int x, int y) {
			super(buttonId, textureId, x, y);
		}

		@Override
		public void updateY (int yOffset) {
			y = ScrollBar.this.y + yOffset;
		}
	}

	/**
	 * The nubbin in the scroll bar
	 */
	public ScrollBarNub mNub;

	/**
	 * no matter the number of steps, this is the max top offset for the nub, aka when it's at the bottom of the
	 * scroll bar
	 */
	private int maxNubTopOffset;

	private ScrollEventManager scrollEventManager;

	public ScrollBar (int startId, int leftOffset, int topOffset, int bottomOffset) {
		super(startId, leftOffset, topOffset, TexturedButton.getWidth(0), bottomOffset - topOffset, "");

		this.mNub = new ScrollBarNub(startId + 1, 0, leftOffset, topOffset);

		this.maxNubTopOffset = this.height - this.mNub.height;
	}

	@Override
	public void registerScrollEventManager (ScrollEventManager scrollEventManager) {
		this.scrollEventManager = scrollEventManager;
	}

	@Override
	public List<? extends IScrollabe> getScrollable () {
		return Collections.singletonList(mNub);
	}

	@Override
	public int getMaxYOffset () {
		return maxNubTopOffset;
	}
}
