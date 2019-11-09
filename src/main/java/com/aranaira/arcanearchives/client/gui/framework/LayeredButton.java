package com.aranaira.arcanearchives.client.gui.framework;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Wrapper around {@link Button} that does the appropriate translations to the GL matrix to make the
 * {@link LayeredGuiContainer} work correctly
 */
public class LayeredButton extends Button {

  /**
   * wrapped {@link Button}
   */
  private Button wrapped;

  public LayeredButton(Button wrapped) {
    super(wrapped.id, wrapped.x, wrapped.y, wrapped.width, wrapped.height, wrapped.displayString);
    this.active = wrapped.active;
    this.visible = wrapped.visible;
    this.wrapped = wrapped;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.translatef(0.0f, 0.0f, LayeredGuiContainer.TOP_Z);
    GlStateManager.enableDepth();

    wrapped.drawButton(mc, mouseX, mouseY, partialTicks);

    GlStateManager.disableDepth();

    // clean up z level
    GlStateManager.popMatrix();
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY) {
    wrapped.mouseReleased(mouseX, mouseY);
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    return wrapped.mousePressed(mc, mouseX, mouseY);
  }

  @Override
  public boolean isMouseOver() {
    return wrapped.isMouseOver();
  }

  @Override
  public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    wrapped.drawButtonForegroundLayer(mouseX, mouseY);
  }

  @Override
  public void playPressSound(SoundHandler soundHandlerIn) {
    wrapped.playPressSound(soundHandlerIn);
  }

  @Override
  public int getButtonWidth() {
    return wrapped.getButtonWidth();
  }

  @Override
  public void setWidth(int width) {
    wrapped.setWidth(width);
  }
}
