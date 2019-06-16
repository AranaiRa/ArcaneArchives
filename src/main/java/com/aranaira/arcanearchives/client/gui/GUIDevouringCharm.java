package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerDevouringCharm;
import com.aranaira.arcanearchives.inventory.ContainerGemSocket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GUIDevouringCharm extends GuiContainer {

	private static final ResourceLocation
			TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png"),
			TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png"),
			TEXTURE_DEVOURINGCHARM = new ResourceLocation("arcanearchives:textures/gui/devouring_charm.png"),
			TEXTURE_DEVOURINGCHARM_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/devouring_charm.png");

	private static final int
			DEVOURINGCHARM_X = 0,
			DEVOURINGCHARM_Y = 0,
			DEVOURINGCHARM_S = 130,
			INVENTORY_W = 181,
			INVENTORY_H = 181;


	private ContainerDevouringCharm containerDevouringCharm;

	public GUIDevouringCharm(@Nonnull ContainerDevouringCharm containerDevouringCharm) {
		super(containerDevouringCharm);
		this.containerDevouringCharm = containerDevouringCharm;
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

		if (ConfigHandler.UsePrettyGUIs) {
			float i = (this.width - this.xSize) / 2;
			float j = (this.height - this.ySize) / 2;

			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM);
			this.drawTexturedModalRect(i + 25, j + 58, DEVOURINGCHARM_X, DEVOURINGCHARM_Y, DEVOURINGCHARM_S, DEVOURINGCHARM_S);

			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
			this.drawTexturedModalRect(i, j + 22 + DEVOURINGCHARM_S, 0, 0, INVENTORY_W, INVENTORY_H);
		}
	}
}
