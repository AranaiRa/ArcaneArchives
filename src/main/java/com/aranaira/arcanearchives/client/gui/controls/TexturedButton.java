package com.aranaira.arcanearchives.client.gui.controls;

import com.aranaira.arcanearchives.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

@OnlyIn(Dist.CLIENT)
/**
 * A {@link Button} with a texture from {@link #BUTTON_TEXTURES}
 */ public class TexturedButton extends Button {
  private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("arcanearchives:textures/gui/buttons.png");
  private static final ResourceLocation BUTTON_TEXTURES_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/buttons.png");
  private static final int[] TEXTURE_X_START = new int[]{0};
  private static final int[] TEXTURE_Y_START = new int[]{0};
  private static final int[] TEXTURE_HEIGHT = new int[]{12};
  private static final int[] TEXTURE_WIDTH = new int[]{12};
  private static final float BUTTON_TEXTURE_TOTAL_SIZE = 16;

  private int mTextureId;

  /**
   * Create a {@link Button} with a texture from {@link #BUTTON_TEXTURES}
   *
   * @param buttonId  id for this {@link Button}
   * @param textureId index (zero based) of texture in {@link #BUTTON_TEXTURES}
   * @param x         x position on screen to draw this button
   * @param y         y position on screen to draw this button
   */
  public TexturedButton(int buttonId, int textureId, int x, int y) {
    super(buttonId, x, y, TEXTURE_WIDTH[textureId], TEXTURE_HEIGHT[textureId], "");

    mTextureId = textureId;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    if (this.visible) {
      this.mouseDragged(mc, mouseX, mouseY);

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (ConfigHandler.UsePrettyGUIs) {
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
      } else {
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES_SIMPLE);
      }
      int x = TEXTURE_X_START[mTextureId];
      int y = TEXTURE_Y_START[mTextureId];

      drawModalRectWithCustomSizedTexture(this.x, this.y, x, y, width, height, BUTTON_TEXTURE_TOTAL_SIZE, BUTTON_TEXTURE_TOTAL_SIZE);
    }
  }

  /**
   * Get width of given texture in pixels
   *
   * @param textureId index (zero based) of texture in {@link #BUTTON_TEXTURES}
   * @return width in pixels
   */
  static public int getWidth(int textureId) {
    return TEXTURE_WIDTH[textureId];
  }
}
