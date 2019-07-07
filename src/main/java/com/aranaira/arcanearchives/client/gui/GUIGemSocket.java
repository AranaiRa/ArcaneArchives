package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerGemSocket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GUIGemSocket extends GuiContainer {

	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png");
	private static final ResourceLocation TEXTURE_FABRIAL = new ResourceLocation("arcanearchives:textures/gui/fabrial.png");
	private static final ResourceLocation TEXTURE_SINGLESLOT_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/single_slot.png");

	private static final int
			FABRIAL_X = 102,
			FABRIAL_Y = 0,
			FABRIAL_S = 22,
			STRAP_X = 0,
			STRAP_Y = 101,
			STRAP_W = 94,
			STRAP_H = 37,
			INVENTORY_W = 181,
			INVENTORY_H = 101,
			OFFHAND_X = 207,
			OFFHAND_Y = 0,
			OFFHAND_W = 49,
			OFFHAND_H = 33,
			OFFHAND_SIMPLE_X = 32,
			OFFHAND_SIMPLE_Y = 0,
			OFFHAND_SIMPLE_S = 28,
			RECHARGE_X = 238,
			RECHARGE_Y = 33,
			RECHARGE_S = 18;


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

		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		if(ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
			this.drawTexturedModalRect(i - 32, j + 106, OFFHAND_X, OFFHAND_Y, OFFHAND_W, OFFHAND_H);
		}
		else {
			this.mc.getTextureManager().bindTexture(TEXTURE_SINGLESLOT_SIMPLE);
			this.drawTexturedModalRect(i - 29, j + 109, OFFHAND_SIMPLE_X, OFFHAND_SIMPLE_Y, OFFHAND_SIMPLE_S, OFFHAND_SIMPLE_S);
		}

		if(ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
		else
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV_SIMPLE);
		this.drawTexturedModalRect(i, j + 22 + FABRIAL_S, 0, 0, INVENTORY_W, INVENTORY_H);
		this.drawTexturedModalRect(i + 153, j + 2, RECHARGE_X, RECHARGE_Y, RECHARGE_S, RECHARGE_S);

		if(ConfigHandler.UsePrettyGUIs) //No strap on simple GUI, so just skip
			this.drawTexturedModalRect(i + 42, j + 11, STRAP_X, STRAP_Y, STRAP_W, STRAP_H);

		if(ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_FABRIAL);
			this.drawTexturedModalRect(i + 78, j, FABRIAL_X, FABRIAL_Y, FABRIAL_S, FABRIAL_S);
		}
		else {
			this.mc.getTextureManager().bindTexture(TEXTURE_SINGLESLOT_SIMPLE);
			this.drawTexturedModalRect(i + 73, j - 5, 0, 0, 32, 32);
		}

	}
}
