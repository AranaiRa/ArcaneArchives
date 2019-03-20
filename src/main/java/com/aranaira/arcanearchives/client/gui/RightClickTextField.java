package com.aranaira.arcanearchives.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class RightClickTextField extends GuiTextField
{
	public RightClickTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height)
	{
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
	{
		if(mouseButton == 1)
		{
			// right click
			this.setText("");
		}

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
