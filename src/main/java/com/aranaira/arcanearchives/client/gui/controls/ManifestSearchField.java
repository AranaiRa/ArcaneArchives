package com.aranaira.arcanearchives.client.gui.controls;

import com.aranaira.arcanearchives.client.gui.GUIManifest;
import com.aranaira.arcanearchives.integration.jei.JEIPlugin;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.input.Keyboard;

public class ManifestSearchField extends RightClickTextField {
  private int id;
  private GUIManifest gui;

  public ManifestSearchField(GUIManifest gui, int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
    super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    this.id = componentId;
    this.gui = gui;
  }

  @Override
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
    if (flag && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))) {
      syncFromJEI(true);
    }

    return super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  public void syncToJEI() {
    syncToJEI(false);
  }

  public void syncToJEI(boolean force) {
    if (!this.gui.getJEISync() && !force) {
      return;
    }

    if (!Loader.isModLoaded("jei")) {
      return;
    }

    // This function manually pushes the current search term from here into JEI
    // if our current string is not empty.
    String current = this.getText();
    JEIPlugin.runtime.getIngredientFilter().setFilterText(current);
  }

  public void syncFromJEI(boolean force) {
    if (!this.gui.getJEISync() && !force) {
      return;
    }

    if (!Loader.isModLoaded("jei")) {
      return;
    }

    // This forcefully copies the current search term from JEI into the Manifest.
    // It will override any value in the manifest current.

    String filterText = JEIPlugin.runtime.getIngredientFilter().getFilterText();
    setText(filterText);
    setResponderEntryValue(this.id, filterText);
  }

  @Override
  public void setResponderEntryValue(int idIn, String textIn) {
    super.setResponderEntryValue(idIn, textIn);
    syncToJEI();
  }
}
