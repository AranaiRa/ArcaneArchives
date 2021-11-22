package com.aranaira.arcanearchives.client.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class InvisibleButton extends Button {
  private static ITextComponent EMPTY = new StringTextComponent("");
  public InvisibleButton(int pX, int pY, int pWidth, int pHeight, Button.IPressable pOnPress) {
    this(pX, pY, pWidth, pHeight, pOnPress, NO_TOOLTIP);
  }

  public InvisibleButton(int pX, int pY, int pWidth, int pHeight, Button.IPressable pOnPress, Button.ITooltip pOnTooltip) {
    super(pX, pY, pWidth, pHeight, EMPTY, pOnPress, pOnTooltip);
  }

  @Override
  public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
  }
}
