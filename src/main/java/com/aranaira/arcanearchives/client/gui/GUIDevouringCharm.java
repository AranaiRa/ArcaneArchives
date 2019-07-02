package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerDevouringCharm;
import com.aranaira.arcanearchives.inventory.ContainerGemSocket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GUIDevouringCharm extends GuiContainer {

	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png");
	private static final ResourceLocation TEXTURE_DEVOURINGCHARM = new ResourceLocation("arcanearchives:textures/gui/devouring_charm.png");
	private static final ResourceLocation TEXTURE_DEVOURINGCHARM_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/devouring_charm.png");
	private static final ResourceLocation TEXTURE_FABRIAL = new ResourceLocation("arcanearchives:textures/gui/fabrial.png");
	private static final ResourceLocation TEXTURE_SINGLESLOT_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/single_slot.png");

	private static final int
			DEVOURINGCHARM_X = 0,
			DEVOURINGCHARM_Y = 0,
			DEVOURINGCHARM_S = 130,
			SLOT_X = 102,
			SLOT_Y = 0,
			SLOT_S = 22,
			SLOT_SIMPLE_X = 60,
			SLOT_SIMPLE_Y = 0,
			SLOT_SIMPLE_S = 26,
			INVENTORY_W = 181,
			INVENTORY_H = 101;


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

		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		if (ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM);
		else
			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM_SIMPLE);
		this.drawTexturedModalRect(i + 25, j + 58, DEVOURINGCHARM_X, DEVOURINGCHARM_Y, DEVOURINGCHARM_S, DEVOURINGCHARM_S);

		if (ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
		else
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV_SIMPLE);
		this.drawTexturedModalRect(i, j + 22 + DEVOURINGCHARM_S, 0, 0, INVENTORY_W, INVENTORY_H);

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_FABRIAL);
			this.drawTexturedModalRect(i + 27, j + 46, SLOT_X, SLOT_Y, SLOT_S, SLOT_S);
			this.drawTexturedModalRect(i + 9, j + 73, SLOT_X, SLOT_Y, SLOT_S, SLOT_S);
			this.drawTexturedModalRect(i - 3, j + 102, SLOT_X, SLOT_Y, SLOT_S, SLOT_S);
			this.drawTexturedModalRect(i + 131, j + 46, SLOT_X, SLOT_Y, SLOT_S, SLOT_S);
			this.drawTexturedModalRect(i + 149, j + 73, SLOT_X, SLOT_Y, SLOT_S, SLOT_S);
			this.drawTexturedModalRect(i + 161, j + 102, SLOT_X, SLOT_Y, SLOT_S, SLOT_S);
		}
		else {
			this.mc.getTextureManager().bindTexture(TEXTURE_SINGLESLOT_SIMPLE);
			this.drawTexturedModalRect(i + 25, j + 44, SLOT_SIMPLE_X, SLOT_SIMPLE_Y, SLOT_SIMPLE_S, SLOT_SIMPLE_S);
			this.drawTexturedModalRect(i + 7, j + 71, SLOT_SIMPLE_X, SLOT_SIMPLE_Y, SLOT_SIMPLE_S, SLOT_SIMPLE_S);
			this.drawTexturedModalRect(i - 5, j + 100, SLOT_SIMPLE_X, SLOT_SIMPLE_Y, SLOT_SIMPLE_S, SLOT_SIMPLE_S);
			this.drawTexturedModalRect(i + 129, j + 44, SLOT_SIMPLE_X, SLOT_SIMPLE_Y, SLOT_SIMPLE_S, SLOT_SIMPLE_S);
			this.drawTexturedModalRect(i + 147, j + 71, SLOT_SIMPLE_X, SLOT_SIMPLE_Y, SLOT_SIMPLE_S, SLOT_SIMPLE_S);
			this.drawTexturedModalRect(i + 159, j + 100, SLOT_SIMPLE_X, SLOT_SIMPLE_Y, SLOT_SIMPLE_S, SLOT_SIMPLE_S);
		}
	}
}
