package com.aranaira.arcanearchives.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ScrollBar extends GuiButton {
	/** The nubbin in the scroll bar */
	private TexturedButton mNub;

	public ScrollBar(GuiScreen guiScreen, int startId, int leftOffset, int topOffset) {
		super(startId, leftOffset, topOffset, "");

		mNub = new TexturedButton(startId + 1, 0, leftOffset, topOffset);

		guiScreen.buttonList.add(this);
		guiScreen.buttonList.add(mNub);
	}

	@Override
	public void drawButton (Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		mNub.drawButton(mc, mouseX, mouseY, partialTicks);
	}
}
