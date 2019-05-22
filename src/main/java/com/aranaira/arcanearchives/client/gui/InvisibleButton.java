package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class InvisibleButton extends GuiButton {

	/**
	 * Draw shaded box where this invisible button is to aid in debugging placement
	 */
	public static boolean DEBUG_LOCATION = false;

	public InvisibleButton (int buttonId, int x, int y, int widthIn, int heightIn, @Nonnull String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	/**
	 * Draws this button text (if set) to the screen. Mostly a copy paste
	 * of {@link GuiButton#drawButton(Minecraft, int, int, float)}
	 */
	@Override
	public void drawButton (Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			this.mouseDragged(mc, mouseX, mouseY);

			if (DEBUG_LOCATION) {
				GuiContainer.drawRect(x, y, x + width, y + height, ConfigHandler.MANIFEST_HIGHLIGHT);
			}

			if (!displayString.isEmpty()) {
				// largely cribbed from GuiButton::DrawButton
				zLevel = 200;

				FontRenderer fontrenderer = mc.fontRenderer;
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				this.hovered = mousePressed(mc, mouseX, mouseY);

				GlStateManager.enableDepth();
				GlStateManager.disableBlend();

				// GuiButton::DrawButton draws textures here, for invisibleButtons we don't want a texture

				this.mouseDragged(mc, mouseX, mouseY);

				// note that "color" in minecraft is "alpha", "red", "green", "blue" with 2 hex chars for each
				int j = 0xFFE0E0E0;

				if (packedFGColour != 0) {
					j = packedFGColour;
				} else if (!this.enabled) {
					j = 0xFFA0A0A0;
				} else if (this.hovered) {
					j = 0xFFFFFFA0;
				}

				this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);

				zLevel = 0;
			}
		}
	}
}
