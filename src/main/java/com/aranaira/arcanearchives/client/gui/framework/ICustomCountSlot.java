package com.aranaira.arcanearchives.client.gui.framework;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public interface ICustomCountSlot {
  int getX();

  int getY();

  ItemStack getItemStack();

  default void renderCount(FontRenderer fr) {
    int count = getItemStack().getCount();
    if (count < 2) {
      return;
    }

    final String s;
    if (count < 1000) {
      s = Integer.toString(count);
    } else if (count < 1024) {
      s = "1K";
    } else {
      int z = (32 - Integer.numberOfLeadingZeros(count)) / 10;
      double adjustedCount = (double) count / (1L << (z * 10));
      // limit s to 3 digits, so roll over to next larger unit if s would be 4 digits with unit character on end
      if (adjustedCount < 100) {
        s = String.format("%.0f%s", adjustedCount, " KMGTPE".charAt(z));
      } else {
        ++z;
        s = String.format("%.1f%s", (double) count / (1L << (z * 10)), " KMGTPE".charAt(z));
      }
    }

    // TODO: FIX THIS

/*    GlStateManager.disableLighting();
    GlStateManager.disableDepth();
    GlStateManager.disableBlend();
    GlStateManager.pushMatrix();
    float scale = 0.75f;
    GlStateManager.scale(scale, scale, 1.0F);
    fr.drawStringWithShadow(s, (getX() + 19 - 2 - (fr.getStringWidth(s) * scale)) / scale, (getY() + 6 + 3 + (1 / (scale * scale) - 1)) / scale, 16777215);
    GlStateManager.popMatrix();
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
    GlStateManager.enableBlend();*/
  }
}
