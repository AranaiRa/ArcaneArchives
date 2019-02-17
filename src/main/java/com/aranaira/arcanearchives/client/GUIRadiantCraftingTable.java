package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GUIRadiantCraftingTable extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantcraftingtable.png");

	public GUIRadiantCraftingTable(EntityPlayer player, ContainerRadiantCraftingTable container)
	{
		super(container);
		this.xSize = 206;
		this.ySize = 203;

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256, 256, 256, 256);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
        this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
