package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerGemSocket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GUIGemSocket extends GuiContainer {

    private static final ResourceLocation
        TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png"),
        TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png"),
        TEXTURE_FABRIAL = new ResourceLocation("arcanearchives:textures/gui/fabrial.png"),
        TEXTURE_FABRIAL_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/fabrial.png");


    private ContainerGemSocket containerGemSocket;

    public GUIGemSocket(@Nonnull ContainerGemSocket containerGemSocket) {
        super(containerGemSocket);
        ArcaneArchives.logger.info("GUI CLASS INSTANTIATED");
        this.containerGemSocket = containerGemSocket;
        xSize = 256;
        ySize = 256;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (ConfigHandler.UsePrettyGUIs) {
            this.mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.drawTexturedModalRect(i, j, 0, 0, 181, 101);
        }
    }
}
