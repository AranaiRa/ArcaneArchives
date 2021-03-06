package com.aranaira.arcanearchives.client.gui.framework;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Wrapper around {@link GuiButton} that does the appropriate translations to the GL matrix to make the
 * {@link LayeredGuiContainer} work correctly
 */
public class LayeredButton extends GuiButton {

	/**
	 * wrapped {@link GuiButton}
	 */
	private GuiButton wrapped;

	public LayeredButton (GuiButton wrapped) {
		super(wrapped.id, wrapped.x, wrapped.y, wrapped.width, wrapped.height, wrapped.displayString);
		this.enabled = wrapped.enabled;
		this.visible = wrapped.visible;
		this.wrapped = wrapped;
	}

	@Override
	public void drawButton (Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, LayeredGuiContainer.TOP_Z);
		GlStateManager.enableDepth();

		wrapped.drawButton(mc, mouseX, mouseY, partialTicks);

		GlStateManager.disableDepth();

		// clean up z level
		GlStateManager.popMatrix();
	}

	@Override
	public void mouseReleased (int mouseX, int mouseY) {
		wrapped.mouseReleased(mouseX, mouseY);
	}

	@Override
	public boolean mousePressed (Minecraft mc, int mouseX, int mouseY) {
		return wrapped.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public boolean isMouseOver () {
		return wrapped.isMouseOver();
	}

	@Override
	public void drawButtonForegroundLayer (int mouseX, int mouseY) {
		wrapped.drawButtonForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void playPressSound (SoundHandler soundHandlerIn) {
		wrapped.playPressSound(soundHandlerIn);
	}

	@Override
	public int getButtonWidth () {
		return wrapped.getButtonWidth();
	}

	@Override
	public void setWidth (int width) {
		wrapped.setWidth(width);
	}
}
