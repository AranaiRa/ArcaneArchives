package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.inventory.ContainerMatrixStorage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GUIMatrixStorage extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/matrix_storage.png");
	private static final ResourceLocation PlayerInvTexture = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation ScrollBar = new ResourceLocation("arcanearchives:textures/gui/icon_scrollbar.png");

	private float mScrollbarPercent = 0.0f;
	private boolean mScrollbarSelected = false;

	public GUIMatrixStorage(EntityPlayer player, ContainerMatrixStorage container)
	{
		super(container);
		this.xSize = 223;
		this.ySize = 246;


	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256, 256, 256, 256);

		this.mc.getTextureManager().bindTexture(PlayerInvTexture);

		drawModalRectWithCustomSizedTexture(guiLeft + 126, guiTop + 124, 0, 0, 181, 98, 256, 128);

		this.mc.getTextureManager().bindTexture(ScrollBar);
		//top    34
		//bottom 200
		drawModalRectWithCustomSizedTexture(guiLeft + 92, guiTop + 34 + (int) (166 * mScrollbarPercent), 0, 0, 12, 12, 16, 16);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		//ArcaneArchives.logger.info("X : " + mouseX + " | Y : " + mouseY + " | TIME : " + timeSinceLastClick);
		if(clickedMouseButton == 0 && mScrollbarSelected)
		{
			mScrollbarPercent = (float) (mouseY - 40 - guiTop) / (float) 166;
			if(mScrollbarPercent > 1) mScrollbarPercent = 1;
			if(mScrollbarPercent < 0) mScrollbarPercent = 0;
		}
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		// ArcaneArchives.logger.info("Mouse : " + mouseX + "," + mouseY);
		// ArcaneArchives.logger.info("Thing : " + (guiLeft + 92) + "," + (guiTop + 34 + (int) (166 * mScrollbarPercent)));
		if(mouseX > guiLeft + 92 && mouseY > guiTop + 34 + (int) (166 * mScrollbarPercent) && mouseX < guiLeft + 92 + 12 && mouseY < guiTop + 34 + (int) (166 * mScrollbarPercent) + 12)
		{
			mScrollbarSelected = true;
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		mScrollbarSelected = false;

		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
