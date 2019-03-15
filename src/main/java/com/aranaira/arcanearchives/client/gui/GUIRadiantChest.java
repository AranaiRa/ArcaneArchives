package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ManifestTracking;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

import java.io.IOException;

@Optional.InterfaceList({@Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IChestButtonCallback", striprefs = true), @Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IItemSearchBar", striprefs = true)})
public class GUIRadiantChest extends GuiContainer implements IChestButtonCallback, IItemSearchBar
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
	private IntArrayList tracked;
	private int dimension;
	private BlockPos pos;


	public GUIRadiantChest(ContainerRadiantChest inventorySlotsIn, EntityPlayer player)
	{
		super(inventorySlotsIn);

		this.mContainer = inventorySlotsIn;

		RadiantChestTileEntity tile = inventorySlotsIn.getTile();
		this.dimension = tile.dimension;
		this.pos = tile.getPos();
		tracked = ManifestTracking.get(dimension, pos);

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
	public void drawSlot(Slot slot)
	{
		ItemStack stack = slot.getStack();
		if(!stack.isEmpty())
		{
			int pack = RecipeItemHelper.pack(stack);
			if(tracked != null && tracked.contains(pack))
			{
				GlStateManager.disableDepth();
				drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, ConfigHandler.MANIFEST_HIGHLIGHT);
			}
		}

		super.drawSlot(slot);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, ImageScale, ImageScale, ImageScale, ImageScale);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		fontRenderer.drawString(getName(), guiLeft + mNameTextLeftOffset, guiTop + mNameTextTopOffset, 0x000000);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		boolean exit = false;

		if(mouseX > guiLeft + mNameTextLeftOffset && mouseX < guiLeft + mNameTextLeftOffset + mNameTextWidth && mouseY > guiTop + mNameTextTopOffset && mouseY < guiTop + mNameTextTopOffset + mNameTextHeight)
		{
			if(mouseButton == 1)
			{
				mNameField = "";
				exit = true;
			}
			mTextEnteringMode = true;
		} else
		{
			exit = true;
		}

		if(mTextEnteringMode && exit)
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
				if(mNameField != null && mNameField.length() > 0)
					mNameField = mNameField.substring(0, mNameField.length() - 1);
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

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		if(tracked != null)
		{
			ManifestTracking.remove(dimension, pos);
		}
	}

	@Optional.Method(modid = "quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int buttonType)
	{
		return true;
	}

	@Optional.Method(modid = "quark")
	@Override
	public void onSearchBarAdded(GuiTextField bar)
	{
		bar.y = (height / 2) + 2;
		bar.x = (width / 2) - bar.width / 2;
	}
}
