package com.aranaira.arcanearchives.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class ScrollBar extends GuiButton {
	/** The nubbin in the scroll bar */
	public TexturedButton mNub;

	public ScrollBar(int startId, int leftOffset, int topOffset) {
		super(startId, leftOffset, topOffset, "");

		mNub = new TexturedButton(startId + 1, 0, leftOffset, topOffset);
	}
}
