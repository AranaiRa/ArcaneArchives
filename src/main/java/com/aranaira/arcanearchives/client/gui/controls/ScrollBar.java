package com.aranaira.arcanearchives.client.gui.controls;

import com.aranaira.arcanearchives.client.gui.framework.IScrollabe;
import com.aranaira.arcanearchives.client.gui.framework.IScrollableContainer;
import com.aranaira.arcanearchives.client.gui.framework.ScrollEventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

import java.util.Collections;
import java.util.List;

public class ScrollBar extends InvisibleButton implements IScrollableContainer {
  private class ScrollBarNub extends TexturedButton implements IScrollabe {

    /**
     * Create a {@link Button} with a texture from {@link TexturedButton#BUTTON_TEXTURES}
     *
     * @param buttonId  id for this {@link Button}
     * @param textureId index (zero based) of texture in {@link TexturedButton#BUTTON_TEXTURES}
     * @param x         x position on screen to draw this button
     * @param y         y position on screen to draw this button
     */
    public ScrollBarNub(int buttonId, int textureId, int x, int y) {
      super(buttonId, textureId, x, y);

      this.isBeingDragged = false;
    }

    /**
     * true iff this button is currently being dragged
     */
    private boolean isBeingDragged;

    @Override
    public void updateY(int yOffset) {
      y = ScrollBar.this.y + yOffset;
    }

    /**
     * Computer scrollPercent based on arbitrary y position from mouse
     *
     * @param mouseY
     * @return percent from top that mouseY is
     */
    private float getScrollPercent(int mouseY) {
      return (mouseY - (this.height / 2) - ScrollBar.this.y) / (float) ScrollBar.this.getMaxYOffset();
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      final boolean interacted = super.mousePressed(mc, mouseX, mouseY);

      if (visible && interacted) {
        isBeingDragged = true;
        ScrollBar.this.scrollEventManager.setScrollPercent(getScrollPercent(mouseY));
      }

      return interacted;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
      super.mouseDragged(mc, mouseX, mouseY);

      if (visible && isBeingDragged) {
        ScrollBar.this.scrollEventManager.setScrollPercent(getScrollPercent(mouseY));
      }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
      super.mouseReleased(mouseX, mouseY);

      isBeingDragged = false;
    }
  }

  /**
   * The nubbin in the scroll bar
   */
  public ScrollBarNub mNub;

  /**
   * no matter the number of steps, this is the max top offset for the nub, aka when it's at the bottom of the
   * scroll bar
   */
  private int maxNubTopOffset;

  private ScrollEventManager scrollEventManager;

  public ScrollBar(int startId, int leftOffset, int topOffset, int bottomOffset) {
    super(startId, leftOffset, topOffset, TexturedButton.getWidth(0), bottomOffset - topOffset, "");

    this.mNub = new ScrollBarNub(startId + 1, 0, leftOffset, topOffset);
    this.maxNubTopOffset = this.height - this.mNub.height;

    this.scrollEventManager = null;
  }

  @Override
  public void registerScrollEventManager(ScrollEventManager scrollEventManager) {
    if (this.scrollEventManager != null) {
      this.scrollEventManager.unregisterListener(this);
    }

    this.scrollEventManager = scrollEventManager;
  }

  @Override
  public List<? extends IScrollabe> getScrollable() {
    return Collections.singletonList(mNub);
  }

  @Override
  public int getMaxYOffset() {
    return maxNubTopOffset;
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    final boolean interacted = super.mousePressed(mc, mouseX, mouseY);

    if (interacted && !mNub.mousePressed(mc, mouseX, mouseY)) {
      if (mouseY < mNub.y) {
        scrollEventManager.pageUp();
      } else {
        scrollEventManager.pageDown();
      }
    }

    return interacted;
  }
}
