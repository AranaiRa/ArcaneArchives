package com.aranaira.arcanearchives.items;

import gigaherz.lirelent.guidebook.guidebook.client.GuiGuidebook;
import gigaherz.lirelent.guidebook.guidebook.client.background.IBookBackground;
import gigaherz.lirelent.guidebook.guidebook.client.background.IBookBackgroundFactory;
import gigaherz.lirelent.guidebook.guidebook.elements.ElementImage;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TomeOfArcanaItemBackground implements IBookBackground {
	private final GuiGuidebook gui;

	private final ResourceLocation imageLocation;
	private final int imageX;
	private final int imageY;
	private final int imageWidth;
	private final int imageHeight;
	private final int imageFileWidth;
	private final int imageFileHeight;
	private final float scale;

	private boolean closing = false;

	private TomeOfArcanaItemBackground (GuiGuidebook gui, ElementImage image) {
		this.gui = gui;
		this.imageLocation = image.textureLocation;
		this.imageX = image.tx;
		this.imageY = image.ty;
		this.imageWidth = image.tw;
		this.imageHeight = image.th;
		this.imageFileWidth = (image.w > 0 ? image.w : image.tw);
		this.imageFileHeight = (image.h > 0 ? image.h : image.th);
		this.scale = image.scale;
	}

	@Override
	public Layout getLayout()
	{
		return Layout.ONE_PAGE;
	}

	@Override
	public void draw(float partialTicks, int bookHeight, float scalingFactor)
	{
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		// ====================================================================

		// translate to "center" of gui
		// and make sure the texture is "behind" other GUI elements
		GlStateManager.translate(gui.width * 0.5, gui.height * 0.5, -50);
		float effectiveScale = 1.05f * scalingFactor;
		GlStateManager.scale(effectiveScale, effectiveScale, 1.0f);

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		gui.mc.getTextureManager().bindTexture(imageLocation);

		// then because we're at the "center" of the gui draw the texture at half the texture size left and up
		Gui.drawModalRectWithCustomSizedTexture(-imageWidth / 2, -imageHeight / 2,
				imageX, imageY, imageWidth, imageHeight, imageFileWidth, imageFileHeight);

		// ====================================================================

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
	}

	@Override
	public void startClosing()
	{
		closing = true;
	}

	@Override
	public boolean isFullyOpen()
	{
		// always fully open
		return true;
	}

	@Override
	public boolean update()
	{
		// if closing, than instantly closed, and reset
		if (closing) {
			closing = false;
			return true;
		}

		return false;
	}

	@Override
	public int getWidth()
	{
		return (int)(imageWidth * scale);
	}

	@Override
	public int getHeight()
	{
		return (int)(imageHeight * scale);
	}

	@Override
	public int getInnerMargin () {
		return 35;
	}

	@Override
	public int getOuterMargin () {
		return 5;
	}

	@Override
	public int getTopMargin () {
		return 5;
	}

	@Override
	public int getBottomMargin () {
		return 13;
	}

	@Override
	public int getBookScaleMargin()
	{
		return 10;
	}

	public static IBookBackgroundFactory TomeOfArcanaItemBackgroundFactory = (gui, backgroundLocation) -> {
		ElementImage elementImage = new ElementImage(false, false);

		elementImage.textureLocation = backgroundLocation;
		elementImage.tx = 0;
		elementImage.ty = 0;
		elementImage.tw = 243;
		elementImage.th = 253;
		elementImage.w = 256;
		elementImage.h = 256;
		elementImage.scale = 1f;

		return new TomeOfArcanaItemBackground(gui, elementImage);
	};
}
