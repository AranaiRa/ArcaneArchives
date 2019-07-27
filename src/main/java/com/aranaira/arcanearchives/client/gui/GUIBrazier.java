package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.client.gui.controls.InvisibleButton;
import com.aranaira.arcanearchives.client.gui.controls.RightClickTextField;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerBrazier;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketBrazier.DecrementRadius;
import com.aranaira.arcanearchives.network.PacketBrazier.IncrementRadius;
import com.aranaira.arcanearchives.network.PacketBrazier.SetRadius;
import com.aranaira.arcanearchives.network.PacketBrazier.SetSubnetworkMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIBrazier extends GuiContainer implements GuiPageButtonList.GuiResponder {

	private static final ResourceLocation TEXTURE_BRAZIER = new ResourceLocation("arcanearchives:textures/gui/brazier_hoarding.png");

	private static final int BACKGROUND_X = 0, BACKGROUND_Y = 0, BACKGROUND_W = 98, BACKGROUND_H = 41, EYE_OPEN_X = 222, EYE_OPEN_Y = 0, EYE_CLOSED_X = 210, EYE_CLOSED_Y = 0, EYE_W = 12, EYE_H = 10, SLASH_X = 240, SLASH_Y = 0, SLASH_S = 16, CHECK_X = 234, CHECK_Y = 0, CHECK_S = 6, ROUTING_TOOLTIP_X = 67, ROUTING_TOOLTIP_Y = 28, ROUTING_TOOLTIP_W = 31, ROUTING_TOOLTIP_H = 16, VISUALIZER_TOOLTIP_X = 6, VISUALIZER_TOOLTIP_Y = 7, VISUALIZER_TOOLTIP_W = 13, VISUALIZER_TOOLTIP_H = 12, RADIUS_LEFT_TOOLTIP_X = 29, RADIUS_LEFT_TOOLTIP_Y = 6, RADIUS_LEFT_TOOLTIP_W = 8, RADIUS_LEFT_TOOLTIP_H = 16, RADIUS_RIGHT_TOOLTIP_X = 85, RADIUS_RIGHT_TOOLTIP_Y = 6, RADIUS_RIGHT_TOOLTIP_W = 8, RADIUS_RIGHT_TOOLTIP_H = 16;

	private GuiButton reduceButton, expandButton, fullNetworkToggleButton, visualizerButton;
	private GuiTextField radiusField;

	private ContainerBrazier containerBrazier;

	public GUIBrazier (@Nonnull ContainerBrazier containerBrazier) {
		super(containerBrazier);
		this.containerBrazier = containerBrazier;
		xSize = BACKGROUND_W;
		ySize = BACKGROUND_H;
		this.containerBrazier.getTile().addUpdateHook(this::updateHook);
	}

	@Override
	public void initGui () {
		super.initGui();

		buttonList.clear();

		radiusField = new RightClickTextField(0, fontRenderer, guiLeft + 45, guiTop + 9, 36, 12);
		radiusField.setText("" + containerBrazier.getTile().getRadius());
		radiusField.setGuiResponder(this);
		radiusField.setEnableBackgroundDrawing(false);
		radiusField.setValidator((input) -> (input.matches("[0-9]+") && input.length() <= 3) || input.isEmpty());

		reduceButton = new InvisibleButton(0, guiLeft + 29, guiTop + 2, 8, 16, "");
		expandButton = new InvisibleButton(1, guiLeft + 85, guiTop + 2, 8, 16, "");
		fullNetworkToggleButton = new InvisibleButton(2, guiLeft + 67, guiTop + 27, 12, 12, "");
		visualizerButton = new InvisibleButton(3, guiLeft + 6, guiTop + 6, 14, 14, "");
		addButton(reduceButton);
		addButton(expandButton);
		addButton(fullNetworkToggleButton);
		addButton(visualizerButton);
	}

	@Override
	protected void actionPerformed (GuiButton button) throws IOException {
		if (button.id == 0) { //reduce button
			DecrementRadius packet = new DecrementRadius(containerBrazier.getTile().getUuid());
			Networking.CHANNEL.sendToServer(packet);
		}
		if (button.id == 1) { //expand button
			IncrementRadius packet = new IncrementRadius(containerBrazier.getTile().getUuid());
			Networking.CHANNEL.sendToServer(packet);
		}
		if (button.id == 2) { //network insertion type button
			boolean newMode = !containerBrazier.getTile().getNetworkMode();
			SetSubnetworkMode packet = new SetSubnetworkMode(newMode, containerBrazier.getTile().getUuid());
			Networking.CHANNEL.sendToServer(packet);
		}
		if (button.id == 3) { //visualizer button
			containerBrazier.getTile().toggleShowRange();
		}
	}


	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		boolean wasEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GlStateManager.disableLighting();
		radiusField.drawTextBox();
		if (wasEnabled) {
			GlStateManager.enableLighting();
		}

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		float i = (this.width - this.xSize) / 2;
		float j = (this.height - this.ySize) / 2;

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(TEXTURE_BRAZIER);
			this.drawTexturedModalRect(i, j, BACKGROUND_X, BACKGROUND_Y, BACKGROUND_W, BACKGROUND_H);
		}

		if (containerBrazier.getTile().getNetworkMode()) {
			this.drawTexturedModalRect(i + 70, j + 30, CHECK_X, CHECK_Y, CHECK_S, CHECK_S);
			this.drawTexturedModalRect(i + 82, j + 25, SLASH_X, SLASH_Y, SLASH_S, SLASH_S);
		}

		if (containerBrazier.getTile().isShowingRange()) {
			this.drawTexturedModalRect(i + 7, j + 8, EYE_OPEN_X, EYE_OPEN_Y, EYE_W, EYE_H);
		} else {
			this.drawTexturedModalRect(i + 7, j + 8, EYE_CLOSED_X, EYE_CLOSED_Y, EYE_W, EYE_H);
		}
	}

	@Override
	protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {
		if (radiusField.mouseClicked(mouseX, mouseY, mouseButton)) {
			return;
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		if (radiusField.isFocused() && radiusField.textboxKeyTyped(typedChar, keyCode)) {
			return;
		}

		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void setEntryValue (int id, boolean value) {

	}

	@Override
	public void setEntryValue (int id, float value) {

	}

	@Override
	public void setEntryValue (int id, String value) {
		if (value.isEmpty()) {
			return;
		}

		int newRadius;

		try {
			newRadius = Integer.parseInt(value);
		} catch (NumberFormatException exception) {
			return;
		}

		if (newRadius > 0 && newRadius <= 300) {
			SetRadius packet = new SetRadius(newRadius, containerBrazier.getTile().getUuid());
			Networking.CHANNEL.sendToServer(packet);
		}
	}

	public void updateHook () {
		radiusField.setText("" + containerBrazier.getTile().getRadius());
	}

	@Override
	public void onGuiClosed () {
		containerBrazier.getTile().removeUpdateHook(this::updateHook);
	}

	@Override
	protected void renderHoveredToolTip (int mouseX, int mouseY) {
		if (mouseY >= guiTop + ROUTING_TOOLTIP_Y && mouseY <= guiTop + ROUTING_TOOLTIP_Y + ROUTING_TOOLTIP_H) {
			if (mouseX >= guiLeft + ROUTING_TOOLTIP_X && mouseX <= guiLeft + ROUTING_TOOLTIP_X + ROUTING_TOOLTIP_W) {
				List<String> tooltip = new ArrayList<>();
				if (containerBrazier.getTile().getNetworkMode()) {
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.routingmode.mine1"));
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.routingmode.mine2"));
				} else {
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.routingmode.all1"));
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.routingmode.all2"));
				}
				this.drawHoveringText(tooltip, mouseX, mouseY);
				return;
			}
		}
		if (mouseY >= guiTop + VISUALIZER_TOOLTIP_Y && mouseY <= guiTop + VISUALIZER_TOOLTIP_Y + VISUALIZER_TOOLTIP_H) {
			if (mouseX >= guiLeft + VISUALIZER_TOOLTIP_X && mouseX <= guiLeft + VISUALIZER_TOOLTIP_X + VISUALIZER_TOOLTIP_W) {
				List<String> tooltip = new ArrayList<>();
				if (containerBrazier.getTile().isShowingRange()) {
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.visualizer.hide"));
				} else {
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.visualizer.show1"));
					tooltip.add(I18n.format("arcanearchives.tooltip.brazier.visualizer.show2"));
				}
				this.drawHoveringText(tooltip, mouseX, mouseY);
				return;
			}
		}
		if (mouseY >= guiTop + RADIUS_LEFT_TOOLTIP_Y && mouseY <= guiTop + RADIUS_LEFT_TOOLTIP_Y + RADIUS_LEFT_TOOLTIP_H) {
			if (mouseX >= guiLeft + RADIUS_LEFT_TOOLTIP_X && mouseX <= guiLeft + RADIUS_LEFT_TOOLTIP_X + RADIUS_LEFT_TOOLTIP_W) {
				List<String> tooltip = new ArrayList<>();
				tooltip.add(I18n.format("arcanearchives.tooltip.brazier.radius.decrease"));
				this.drawHoveringText(tooltip, mouseX, mouseY);
				return;
			}
		}
		if (mouseY >= guiTop + RADIUS_RIGHT_TOOLTIP_Y && mouseY <= guiTop + RADIUS_RIGHT_TOOLTIP_Y + RADIUS_RIGHT_TOOLTIP_H) {
			if (mouseX >= guiLeft + RADIUS_RIGHT_TOOLTIP_X && mouseX <= guiLeft + RADIUS_RIGHT_TOOLTIP_X + RADIUS_RIGHT_TOOLTIP_W) {
				List<String> tooltip = new ArrayList<>();
				tooltip.add(I18n.format("arcanearchives.tooltip.brazier.radius.increase"));
				this.drawHoveringText(tooltip, mouseX, mouseY);
				return;
			}
		}
	}
}
