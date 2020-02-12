package com.aranaira.arcanearchives.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIBookContainer extends GuiContainer {
  private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/requisition_items.png");
  private final int ImageHeight = 256, ImageWidth = 256, ImageScale = 256;
  // TODO: This could cause problems if the class is ever loaded in a server context without the Minecraft class
  Minecraft mc = Minecraft.getMinecraft();
  //Buttons to switch interface.
  private GenericButton Tab1;
  private GenericButton Tab2;
  private GenericButton Tab3;

  //Sorting buttons.
  private GenericButton TopButton1;
  private GenericButton TopButton2;
  private GenericButton TopButton3;
  private GenericButton TopButton4;

  //Clears the crafting area and puts the items back into the storage if possible.
  private GenericButton ClearCrafting;

  //List of the slots for the player's inventory.
  private List<Slot> mSlots = new ArrayList<Slot>();


  //If the player is currently entering text into the search bar.
  private boolean isEnteringText = false;

  //The search bar's text used for filtering the list of items on the network.
  private String SearchText = "";

  private Container container;

  public GUIBookContainer(Container container) {

    super(container);

    this.container = container;
    this.xSize = ImageWidth;
    this.ySize = ImageHeight;
  }

  @Override
  public void initGui() {
    super.initGui();

    buttonList.clear();
    int offLeft = (width - ImageWidth) / 2 - 3;
    int offTop = 108;

    //buttonList.add(TopButton1 = new GenericButton(offLeft + (285 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));
    //buttonList.add(TopButton2 = new GenericButton(offLeft + (303 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));
    //buttonList.add(TopButton3 = new GenericButton(offLeft + (321 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));
    //buttonList.add(TopButton4 = new GenericButton(offLeft + (339 - 140) + (14 / 3), 44 + offTop, 14, 12, ""));

    //buttonList.add(Tab3 = new GenericButton(offLeft + (375 - 140) + (15 / 3), 124 + offTop, 15, 40, ""));
    //buttonList.add(Tab2 = new GenericButton(offLeft + (374 - 140) + (20 / 3), 40 + offTop, 20, 40, ""));
    //buttonList.add(Tab1 = new GenericButton(offLeft + (375 - 140) + (15 / 3), 82 + offTop, 15, 40, ""));


    //buttonList.add(ClearCrafting = new GenericButton(offLeft + (259 - 140) + (10 / 3), 116 + offTop, 7, 7, ""));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.enableColorMaterial();

    GlStateManager.disableLighting();


    super.drawScreen(mouseX, mouseY, partialTicks);

    GlStateManager.enableLighting();

    this.renderHoveredToolTip(mouseX, mouseY);


    String temp = fontRenderer.trimStringToWidth(SearchText, 6 * 15, true);

    //Decides what to display if the player has typed something in the search bar.
    if (SearchText.equals("")) {
      fontRenderer.drawString("Search", guiLeft + 46, 20 + guiTop, 0x000000);
    } else {
      fontRenderer.drawString(temp, guiLeft + 46, 20 + guiTop, 0x000000);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.enableColorMaterial();
    this.mc.getTextureManager().bindTexture(GUITextures);

    drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, ImageScale, ImageScale, ImageScale, ImageScale);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    if (mouseButton == 0) {
      //Checks Text Box Bounds
      isEnteringText = mouseX > guiLeft + 46 && mouseX < guiLeft + 46 + 88 && mouseY > guiTop + 20 && mouseY < guiTop + 30;
    }

    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  public void onGuiClosed() {
    container.onContainerClosed(mc.player);
    super.onGuiClosed();
  }

  @Override
  public void updateScreen() {
    //Sets the GUI Buttons to be usable.
    for (GuiButton button : buttonList) {
      button.visible = true;
    }


    super.updateScreen();
  }

  @SideOnly(Side.CLIENT)
  static class GenericButton extends GuiButton {
    public GenericButton(int x, int y, int width, int height, String text) {
      super(1, x, y, width, height, text);
    }
  }
}
