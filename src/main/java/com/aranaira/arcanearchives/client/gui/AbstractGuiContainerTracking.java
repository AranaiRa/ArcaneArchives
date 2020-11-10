/*package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.util.ColorUtils;
import com.aranaira.arcanearchives.util.ColorUtils.Color;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

public abstract class AbstractGuiContainerTracking extends ContainerScreen {
  public AbstractGuiContainerTracking(Container inventorySlotsIn) {
    super(inventorySlotsIn);
  }

  public void glowSlot(Slot slot) {
    GlStateManager.disableDepth();
    long worldTime = this.mc.player.world.getWorldTime();
    Color c = ColorUtils.getColorFromTime(worldTime);
    ContainerScreen.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, c.toInteger());
    GlStateManager.enableDepth();
  }
}*/
