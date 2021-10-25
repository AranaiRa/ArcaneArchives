package com.aranaira.arcanearchives.client.screen;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

public class CrystalWorkbenchScreen extends ContainerScreen<CrystalWorkbenchContainer> {
  private final static ResourceLocation background = new ResourceLocation(ArcaneArchives.MODID, "textures/gui/crystal_workbench.png");

  public CrystalWorkbenchScreen(CrystalWorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    imageWidth = 206;
    imageHeight = 254;
  }

  @Override
  protected void init() {
    super.init();
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.enableBlend();
    this.minecraft.getTextureManager().bind(background);
    int i = this.leftPos;
    int j = this.topPos;
    blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight, 256, 256);
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    RenderSystem.disableBlend();
    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    super.renderLabels(matrixStack, x, y);

    CrystalWorkbenchBlockEntity entity = this.menu.getBlockEntity();
    if (entity == null) {
      return;
    }
    UUID id = entity.getEntityId();
    if (id != null) {
        this.font.draw(matrixStack, id.toString(), 1, 1, 0x000000);
    }
  }
}
