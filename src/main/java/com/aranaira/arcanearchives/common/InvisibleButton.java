package com.aranaira.arcanearchives.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class InvisibleButton extends GuiButton
{

	public InvisibleButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return this.enabled && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	}
}
