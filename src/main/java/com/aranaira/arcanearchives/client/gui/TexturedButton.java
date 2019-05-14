package com.aranaira.arcanearchives.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
/**
 * A {@link GuiButton} with a texture from {@link #BUTTON_TEXTURES}
 */ public class TexturedButton extends GuiButton {
	private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("arcanearchives:textures/gui/buttons.png");
	private static int[] TEXTURE_X_START = new int[]{0};
	private static int[] TEXTURE_Y_START = new int[]{0};
	private static int[] TEXTURE_HEIGHT = new int[]{16};
	private static int[] TEXTURE_WIDTH = new int[]{16};

	private int mTextureId;

	/**
	 * Create a {@link GuiButton} with a texture from {@link #BUTTON_TEXTURES}
	 *
	 * @param buttonId  id for this {@link GuiButton}
	 * @param textureId index (zero based) of texture in {@link #BUTTON_TEXTURES}
	 * @param x         x position on screen to draw this button
	 * @param y         y position on screen to draw this button
	 */
	public TexturedButton (int buttonId, int textureId, int x, int y) {
		super(buttonId, x, y, "");

		mTextureId = textureId;
	}

	@Override
	public void drawButton (Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			int x = TEXTURE_X_START[mTextureId];
			int y = TEXTURE_Y_START[mTextureId];
			int w = TEXTURE_HEIGHT[mTextureId];
			int h = TEXTURE_WIDTH[mTextureId];

			this.drawTexturedModalRect(this.x, this.y, x, y, w, h);
		}
	}
}
