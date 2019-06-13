package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerGemSocket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GUIGemSocket extends GuiContainer {

	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png"), TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png"), TEXTURE_FABRIAL = new ResourceLocation("arcanearchives:textures/gui/fabrial.png"), TEXTURE_FABRIAL_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/fabrial.png");

	private static final int FABRIAL_X = 102, FABRIAL_Y = 0, FABRIAL_S = 22, STRAP_X = 0, STRAP_Y = 101, STRAP_W = 94, STRAP_H = 37, INVENTORY_W = 181, INVENTORY_H = 101;


	private ContainerGemSocket containerGemSocket;

	public GUIGemSocket (@Nonnull ContainerGemSocket containerGemSocket) {
		super(containerGemSocket);
		this.containerGemSocket = containerGemSocket;
		xSize = INVENTORY_W;
		ySize = INVENTORY_H + 22 + FABRIAL_S;
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

			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
			this.drawTexturedModalRect(i, j + 22 + FABRIAL_S, 0, 0, INVENTORY_W, INVENTORY_H);

			this.drawTexturedModalRect(i + 42, j + 11, STRAP_X, STRAP_Y, STRAP_W, STRAP_H);

			this.mc.getTextureManager().bindTexture(TEXTURE_FABRIAL);
			this.drawTexturedModalRect(i + 78, j, FABRIAL_X, FABRIAL_Y, FABRIAL_S, FABRIAL_S);
		}
	}
}
