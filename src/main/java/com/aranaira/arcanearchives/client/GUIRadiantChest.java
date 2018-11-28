package com.aranaira.arcanearchives.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIRadiantChest extends GuiContainer {

	public GUIRadiantChest(Container inventorySlotsIn) 
	{
		super(inventorySlotsIn);
		// TODO Auto-generated constructor stub
		this.xSize = ImageWidth;
		this.ySize = ImageHeight;
	}


	Minecraft mc = Minecraft.getMinecraft();
	private final int ImageHeight = 253, ImageWidth = 210, ImageScale = 256;
	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantchest.png");
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
		int offLeft = (width - ImageWidth) / 2 - 3;
		int offTop = 108;

	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		// TODO Auto-generated method stub
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, ImageScale,ImageScale,ImageScale,ImageScale);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		// TODO Auto-generated method stub
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();

		GlStateManager.disableLighting();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		GlStateManager.enableLighting();
		
		this.renderHoveredToolTip(mouseX, mouseY);
		
		
	}
	
	@Override
	public void onGuiClosed() 
	{
		this.inventorySlots.onContainerClosed(mc.player);
		super.onGuiClosed();
	}
}
