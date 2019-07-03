package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerDevouringCharm;
import com.aranaira.arcanearchives.inventory.ContainerDevouringCharmBackside;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GUIDevouringCharmBackside extends GuiContainer {

	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png");
	private static final ResourceLocation TEXTURE_DEVOURINGCHARM = new ResourceLocation("arcanearchives:textures/gui/devouring_charm.png");
	private static final ResourceLocation TEXTURE_DEVOURINGCHARM_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/devouring_charm.png");
	private static final ResourceLocation TEXTURE_FABRIAL = new ResourceLocation("arcanearchives:textures/gui/fabrial.png");
	private static final ResourceLocation TEXTURE_SINGLESLOT_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/single_slot.png");

	private static final int
			DEVOURINGCHARM_X = 126,
			DEVOURINGCHARM_Y = 127,
			DEVOURINGCHARM_W = 130,
			DEVOURINGCHARM_H = 129,
			FLIP_X = 231,
			FLIP_Y = 0,
			FLIP_W = 25,
			FLIP_H = 15,
			INVENTORY_W = 181,
			INVENTORY_H = 101;


	private ContainerDevouringCharmBackside containerDevouringCharmBackside;

	public GUIDevouringCharmBackside(@Nonnull ContainerDevouringCharmBackside containerDevouringCharmBackside) {
		super(containerDevouringCharmBackside);
		this.containerDevouringCharmBackside = containerDevouringCharmBackside;
		xSize = INVENTORY_W;
		ySize = 300;
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		if (ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM);
		else
			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM_SIMPLE);
		this.drawTexturedModalRect(i + 25, j + 58, DEVOURINGCHARM_X, DEVOURINGCHARM_Y, DEVOURINGCHARM_W, DEVOURINGCHARM_H);
		this.drawTexturedModalRect(i, j + 60, FLIP_X, FLIP_Y, FLIP_W, FLIP_H);

		if (ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
		else
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV_SIMPLE);
		this.drawTexturedModalRect(i, j + 22 + DEVOURINGCHARM_H, 0, 0, INVENTORY_W, INVENTORY_H);
	}
}
