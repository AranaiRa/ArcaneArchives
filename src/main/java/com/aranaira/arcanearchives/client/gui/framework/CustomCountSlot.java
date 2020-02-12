package com.aranaira.arcanearchives.client.gui.framework;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CustomCountSlot extends SlotItemHandler {

  public CustomCountSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public ItemStack getStack() {
    ItemStack stack = super.getStack();
    if (stack.isEmpty()) {
      return stack;
    }
    stack = stack.copy();
    stack.setCount(1);
    return stack;
  }

  public void renderCount(FontRenderer fr) {
    int count = super.getStack().getCount();
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

    GlStateManager.disableLighting();
    GlStateManager.disableDepth();
    GlStateManager.disableBlend();
    fr.drawStringWithShadow(s, (float) (xPos + 19 - 2 - fr.getStringWidth(s)), (float) (yPos + 6 + 3), 16777215);
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
    GlStateManager.enableBlend();
  }
}
