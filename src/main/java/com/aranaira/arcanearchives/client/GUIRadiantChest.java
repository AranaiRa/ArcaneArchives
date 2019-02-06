package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.packets.SetRadiantChestName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.UUID;

public class GUIRadiantChest extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantchest.png");
	private final int ImageHeight = 253, ImageWidth = 192, ImageScale = 256;
	Minecraft mc = Minecraft.getMinecraft();
	ContainerRadiantChest mContainer;
	UUID mPlayerID;
	private int mNameTextLeftOffset = 53;
	private int mNameTextTopOffset = 238;
	private int mNameTextWidth = 88;
	private int mNameTextHeight = 10;
	private boolean mTextEnteringMode = false;
	private String mNameField;

	public GUIRadiantChest(Container inventorySlotsIn, UUID playerID)
	{
		super(inventorySlotsIn);


		mPlayerID = playerID;

		mContainer = (ContainerRadiantChest) inventorySlotsIn;

		mNameField = mContainer.mName;

		this.xSize = ImageWidth;
		this.ySize = ImageHeight;
	}


	@Override
	public void initGui()
	{
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
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, ImageScale, ImageScale, ImageScale, ImageScale);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();

		GlStateManager.disableLighting();

		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.enableLighting();

		this.renderHoveredToolTip(mouseX, mouseY);

		fontRenderer.drawString(mNameField, guiLeft + mNameTextLeftOffset, guiTop + mNameTextTopOffset, 0x000000);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(mouseButton == 0)
		{
			if(mouseX > guiLeft + mNameTextLeftOffset && mouseX < guiLeft + mNameTextLeftOffset + mNameTextWidth && mouseY > guiTop + mNameTextTopOffset && mouseY < guiTop + mNameTextTopOffset + mNameTextHeight)
			{
				mTextEnteringMode = true;
			} else
			{
				if(mTextEnteringMode)
				{
					mTextEnteringMode = false;
					if(!mContainer.mName.equals(mNameField))
					{
						AAPacketHandler.CHANNEL.sendToServer(new SetRadiantChestName(mContainer.mPos, mNameField, mPlayerID, mContainer.mDimension));
						ArcaneArchives.logger.info("SENT PACKET");
						return;
					}

					//BlockPos pos, String name, UUID uuid, int dimensionID
				}
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}


	@Override
	protected void keyTyped(char typedChar, int keyCode)
	{
		//If the user is currently entering text into the search bar.
		if(mTextEnteringMode)
		{
			//Backspace
			if(keyCode == 14)
			{
				if(mNameField.length() > 0) mNameField = mNameField.substring(0, mNameField.length() - 1);
			}
			//Escape and Enter
			else if(keyCode == 1)
			{
				mTextEnteringMode = false;
				mNameField = mContainer.mName;
			} else if(keyCode == 28)
			{
				mTextEnteringMode = false;
				if(!mContainer.mName.equals(mNameField))
					AAPacketHandler.CHANNEL.sendToServer(new SetRadiantChestName(mContainer.mPos, mNameField, mPlayerID, mContainer.mDimension));
			}
			//Anything else.
			else
			{
				if(Character.isLetterOrDigit(typedChar)) mNameField += typedChar;
				else if(typedChar == ' ') mNameField += typedChar;
			}
		} else if(keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
			Minecraft.getMinecraft().player.closeScreen();
	}

	/*
		private int mNameTextLeftOffset = 53;
		private int mNameTextTopOffset = 238;

		private int mNameTextWidth = 88;
		private int mNameTextHeight = 10;

		*/

	// The method being overriden basically does this and there can be an NPE
	// with it sometimes, which is annoying
	/*@Override
	public void onGuiClosed()
	{
		this.inventorySlots.onContainerClosed(mc.player);
		super.onGuiClosed();
	}*/
}
