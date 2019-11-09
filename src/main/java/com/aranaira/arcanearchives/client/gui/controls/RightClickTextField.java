package com.aranaira.arcanearchives.client.gui.controls;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class RightClickTextField extends TextFieldWidget {
  private int id;

  public RightClickTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
    super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    this.id = componentId;
  }

  @Override
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
    if (mouseButton == 1 && flag) {
      // right click
      this.setText("");
      this.setResponderEntryValue(this.id, "");
    }

    return super.mouseClicked(mouseX, mouseY, mouseButton);
  }
}
