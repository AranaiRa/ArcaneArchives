package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIRadiantCraftingTable extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantcraftingtable.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/radiantcraftingtable.png");

	public GUIRadiantCraftingTable(EntityPlayer player, ContainerRadiantCraftingTable container) {
		super(container);
		this.xSize = 206;
		this.ySize = 203;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		if(ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(GUITextures);
		else
			this.mc.getTextureManager().bindTexture(GUITexturesSimple);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256, 256, 256, 256);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
