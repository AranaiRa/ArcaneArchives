/*package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.client.gui.controls.InvisibleButton;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerDevouringCharm;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;

public class GUIDevouringCharm extends GuiContainer {

	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png");
	private static final ResourceLocation TEXTURE_DEVOURINGCHARM = new ResourceLocation("arcanearchives:textures/gui/devouring_charm.png");
	private static final ResourceLocation TEMP_TEXTURE_DEVOURINGCHARM_BACK = new ResourceLocation("arcanearchives:textures/gui/devouring_charm_back.png");
	private static final ResourceLocation TEXTURE_DEVOURINGCHARM_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/devouring_charm.png");
	private static final ResourceLocation TEXTURE_FABRIAL = new ResourceLocation("arcanearchives:textures/gui/fabrial.png");
	private static final ResourceLocation TEXTURE_SINGLESLOT_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/single_slot.png");

	private static final int DEVOURINGCHARM_X = 0, DEVOURINGCHARM_Y = 0, DEVOURINGCHARM_W = 130, DEVOURINGCHARM_H = 129, DEVOURINGCHARM_BACK_X = 126, DEVOURINGCHARM_BACK_Y = 127, DEVOURINGCHARM_BACK_W = 130, DEVOURINGCHARM_BACK_H = 129, FLIP_X = 231, FLIP_Y = 0, FLIP_W = 25, FLIP_H = 15, INVENTORY_W = 181, INVENTORY_H = 101;

	private boolean FLIPPED = false;

	private GuiButton flipButton;

	private ContainerDevouringCharm container;

	public GUIDevouringCharm (@Nonnull ContainerDevouringCharm containerDevouringCharm) {
		super(containerDevouringCharm);
		this.container = containerDevouringCharm;
		xSize = INVENTORY_W;
		ySize = 300;
	}

	@Override
	public void initGui () {
		super.initGui();

		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		buttonList.clear();

		flipButton = new InvisibleButton(0, (int) i, (int) j + 60, FLIP_W, FLIP_H, "");
		addButton(flipButton);
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void actionPerformed (GuiButton button) throws IOException {
		if (button.id == 0) { //flip button
			FLIPPED = !FLIPPED;
			this.container.FLIPPED = FLIPPED;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM);
		} else {
			this.mc.getTextureManager().bindTexture(TEXTURE_DEVOURINGCHARM_SIMPLE);
		}
		// TODO: This is the place where FLIPPED should be checked for moving the UV
		if (!FLIPPED) {
			this.drawTexturedModalRect(i + 25, j + 58, DEVOURINGCHARM_X, DEVOURINGCHARM_Y, DEVOURINGCHARM_W, DEVOURINGCHARM_H);
		} else {
			this.drawTexturedModalRect(i + 25, j + 58, DEVOURINGCHARM_BACK_X, DEVOURINGCHARM_BACK_Y, DEVOURINGCHARM_BACK_W, DEVOURINGCHARM_BACK_H);
		}
		this.drawTexturedModalRect(i, j + 60, FLIP_X, FLIP_Y, FLIP_W, FLIP_H);

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
		} else {
			this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV_SIMPLE);
		}
		this.drawTexturedModalRect(i, j + 22 + DEVOURINGCHARM_H, 0, 0, INVENTORY_W, INVENTORY_H);
	}
}*/
