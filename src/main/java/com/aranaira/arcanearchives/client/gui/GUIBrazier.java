package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.controls.InvisibleButton;
import com.aranaira.arcanearchives.client.gui.controls.RightClickTextField;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerBrazier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;

public class GUIBrazier extends GuiContainer implements GuiPageButtonList.GuiResponder {

	private static final ResourceLocation TEXTURE_BRAZIER = new ResourceLocation("arcanearchives:textures/gui/brazier_hoarding.png");

	private static final int
			BACKGROUND_X = 0,
			BACKGROUND_Y = 0,
			BACKGROUND_W = 98,
			BACKGROUND_H = 41,
			EYE_OPEN_X = 222,
			EYE_OPEN_Y = 0,
			EYE_CLOSED_X = 210,
			EYE_CLOSED_Y = 0,
			EYE_W = 12,
			EYE_H = 10,
			SLASH_X = 240,
			SLASH_Y = 0,
			SLASH_S = 16,
			CHECK_X = 234,
			CHECK_Y = 0,
			CHECK_S = 6;

	private GuiButton reduceButton, expandButton, fullNetworkToggleButton, visualizerButton;
	private GuiTextField radiusField;

	private ContainerBrazier containerBrazier;

	public GUIBrazier(@Nonnull ContainerBrazier containerBrazier) {
		super(containerBrazier);
		this.containerBrazier = containerBrazier;
		xSize = BACKGROUND_W;
		ySize = BACKGROUND_H;
	}

	@Override
	public void initGui () {
		super.initGui();

		buttonList.clear();

		reduceButton = new InvisibleButton(0, guiLeft + 29, guiTop + 2, 8, 16, "");
		expandButton = new InvisibleButton(1, guiLeft + 85, guiTop + 2, 8, 16, "");
		fullNetworkToggleButton = new InvisibleButton(2, guiLeft + 67, guiTop + 27, 12, 12, "");
		visualizerButton = new InvisibleButton(3, guiLeft + 6, guiTop + 6, 14, 14, "");
		addButton(reduceButton);
		addButton(expandButton);
		addButton(fullNetworkToggleButton);
		addButton(visualizerButton);

		radiusField = new RightClickTextField(0, fontRenderer, guiLeft + 14, guiTop + 4, 36, 12);
		radiusField.setText("" + containerBrazier.getTile().getRadius());
		//ArcaneArchives.logger.info("text field should be "+containerBrazier.getTile().getRadius());
		radiusField.setGuiResponder(this);
		radiusField.setEnableBackgroundDrawing(false);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 0) { //reduce button
			containerBrazier.getTile().reduceRadius();
			radiusField.setText("" + containerBrazier.getTile().getRadius());
		}
		if(button.id == 1) { //expand button
			containerBrazier.getTile().increaseRadius();
			radiusField.setText("" + containerBrazier.getTile().getRadius());
		}
		if(button.id == 2) { //network insertion type button
			containerBrazier.getTile().toggleNetworkMode();
		}
		if(button.id == 3) { //visualizer button
			containerBrazier.getTile().toggleShowRange();
		}
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
			this.mc.getTextureManager().bindTexture(TEXTURE_BRAZIER);
			this.drawTexturedModalRect(i, j, BACKGROUND_X, BACKGROUND_Y, BACKGROUND_W, BACKGROUND_H);
		}

		if(containerBrazier.getTile().getNetworkMode()) {
			this.drawTexturedModalRect(i + 70, j + 30, CHECK_X, CHECK_Y, CHECK_S, CHECK_S);
			this.drawTexturedModalRect(i + 82, j + 25, SLASH_X, SLASH_Y, SLASH_S, SLASH_S);
		}

		if(containerBrazier.getTile().isShowingRange())
			this.drawTexturedModalRect(i + 7, j + 8, EYE_OPEN_X, EYE_OPEN_Y, EYE_W, EYE_H);
		else
			this.drawTexturedModalRect(i + 7, j + 8, EYE_CLOSED_X, EYE_CLOSED_Y, EYE_W, EYE_H);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		radiusField.drawTextBox();
	}

	@Override
	public void setEntryValue(int id, boolean value) {

	}

	@Override
	public void setEntryValue(int id, float value) {

	}

	@Override
	public void setEntryValue(int id, String value) {

	}
}
