package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GUIRadiantChest extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantchest.png");
	private final int ImageHeight = 253, ImageWidth = 192, ImageScale = 256;
	private ContainerRadiantChest mContainer;

	private int mNameTextLeftOffset = 53;
	private int mNameTextTopOffset = 238;
	private int mNameTextWidth = 88;
	private int mNameTextHeight = 10;
	private String mNameField;
	private boolean mTextEnteringMode = false;

	public GUIRadiantChest(ContainerRadiantChest inventorySlotsIn, EntityPlayer player)
	{
		super(inventorySlotsIn);

		this.mContainer = inventorySlotsIn;

		this.xSize = ImageWidth;
		this.ySize = ImageHeight;
	}

	public String getName()
	{
		if(mTextEnteringMode)
		{
			return (mNameField == null || mNameField.isEmpty()) ? mContainer.getName() : mNameField;
		}

		return mContainer.getName();
	}

	@Override
	public void initGui()
	{
		super.initGui();

		buttonList.clear();
		//int offLeft = (width - ImageWidth) / 2 - 3;
		//int offTop = 108;
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

		fontRenderer.drawString(getName(), guiLeft + mNameTextLeftOffset, guiTop + mNameTextTopOffset, 0x000000);
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
					if(!mContainer.getName().equals(mNameField))
					{
						mContainer.setName(mNameField);
						mNameField = "";
						return;
					}

					//BlockPos pos, String name, UUID uuid, int dimensionID
				}
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}


	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
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
				mNameField = mContainer.getName();
			} else if(keyCode == 28)
			{
				mTextEnteringMode = false;
				if(!mContainer.getName().equals(mNameField))
				{
					mContainer.setName(mNameField);
					mNameField = "";
				}
			}
			//Anything else.
			else
			{
				if(mNameField == null) mNameField = "";
				if(Character.isLetterOrDigit(typedChar)) mNameField += typedChar;
				else if(typedChar == ' ') mNameField += typedChar;
			}
		} else if(keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			Minecraft.getMinecraft().player.closeScreen();
		} else
		{
			super.keyTyped(typedChar, keyCode);
		}

	}
}
