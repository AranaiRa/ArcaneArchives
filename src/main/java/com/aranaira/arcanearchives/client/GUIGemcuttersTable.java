package com.aranaira.arcanearchives.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.ContainerGemcuttersTable;
import com.aranaira.arcanearchives.common.ContainerManifest;
import com.aranaira.arcanearchives.common.InvisibleButton;
import com.aranaira.arcanearchives.util.ItemComparison;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class GUIGemcuttersTable extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/gemcutterstable.png");

	ContainerGemcuttersTable mCGCT;
	
	private InvisibleButton PrevPageButton;
	private InvisibleButton NextPageButton;
	
	public GUIGemcuttersTable(EntityPlayer player, ContainerGemcuttersTable container)
	{
		super(container);
		mCGCT = container;
		this.xSize = 206;
		this.ySize = 254;
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.clear();
		
		this.PrevPageButton = new InvisibleButton(0, guiLeft + 26, guiTop + 69, 10, 18, null);
		this.NextPageButton = new InvisibleButton(1, guiLeft + 170, guiTop + 69, 10, 18, null);
		
		PrevPageButton.visible = false;
		NextPageButton.visible = false;
		
		this.buttonList.add(PrevPageButton);
		this.buttonList.add(NextPageButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		// TODO Auto-generated method stub
		super.drawScreen(mouseX, mouseY, partialTicks);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);
		
		if (mCGCT.getItemHandler().mRecipe != null)
		{
			for (int i = 18; i < 25; i ++)
			{
				//ArcaneArchives.logger.info(mCGCT.getItemHandler().mRecipe.getOutput().getDisplayName());
				if (ItemComparison.AreItemsEqual(mCGCT.getItemHandler().getStackInSlot(i), mCGCT.getItemHandler().mRecipe.getOutput()))
				{
					this.drawTexturedModalRect(guiLeft + (i - 18) * 18 + 39, guiTop + 68, 206, 0, 20, 20);
					break;
				}
			}
			List<String> mRecipeInput = new ArrayList();
			if (mCGCT.getItemHandler().getRecipeStatus())
			{
				mRecipeInput.add("§2" + mCGCT.getItemHandler().mRecipe.getOutput().getDisplayName());
			}
			else
			{
				mRecipeInput.add("§4" + mCGCT.getItemHandler().mRecipe.getOutput().getDisplayName());
			}
			
			for (ItemStack item : mCGCT.getItemHandler().mRecipe.getInput())
			{
				mRecipeInput.add("§2" + item.getDisplayName() + " : " + item.getCount());
			}
			
			this.drawHoveringText(mRecipeInput, guiLeft + 206, guiTop);
		}
		
		this.renderHoveredToolTip(mouseX, mouseY);
		
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 206,256,256,256);
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
		{
			mCGCT.getItemHandler().prevPage();
		}
		if (button.id == 1)
		{
			mCGCT.getItemHandler().nextPage();
		}
		super.actionPerformed(button);
	}
}
