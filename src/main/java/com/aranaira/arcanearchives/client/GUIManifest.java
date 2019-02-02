package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.common.ContainerManifest;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public class GUIManifest extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/manifest.png");

	private String mSearchText = "";

	private boolean mIsEnteringText = false;

	private ContainerManifest mContainer;

	private int mTextTopOffset = 14;
	private int mTextLeftOffset = 13;

	private int mEndTrackingLeftOffset = 67;
	private int mEndTrackingTopOffset = 202;

	private int mEndTrackingButtonLeftOffset = 65;
	private int mEndTrackingButtonTopOffset = 200;
	private int mEndTrackingButtonWidth = 54;
	private int mEndTrackingButtonHeight = 14;

	private int OTHER_DIMENSION = 0x10989898;
	private final EntityPlayer player;

	public GUIManifest(EntityPlayer player, ContainerManifest container)
	{
		super(container);

		mContainer = container;

		this.xSize = 184;
		this.ySize = 224;

		this.player = player;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.renderHoveredToolTip(mouseX, mouseY);

		String temp = fontRenderer.trimStringToWidth(mSearchText, 6 * 15, true);

		if(mSearchText.equals(""))
			fontRenderer.drawString("Search", guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x000000);
		else if(mIsEnteringText)
			fontRenderer.drawString(temp, guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x4363ff);
		else fontRenderer.drawString(temp, guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x000000);

		fontRenderer.drawString("End Track", guiLeft + mEndTrackingLeftOffset, mEndTrackingTopOffset + guiTop, 0x000000);
	}

	@Override
	protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_)
	{
		super.renderHoveredToolTip(p_191948_1_, p_191948_2_);
	}

	//TODO Figure out how to display what chest the item is in.
	@Override
	@Nonnull
	public List<String> getItemToolTip(ItemStack p_191927_1_)
	{
		// List<String> ls =
		return super.getItemToolTip(p_191927_1_);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256, 256, 256, 256);

	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
	}


	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		//If the user is currently entering text into the search bar.
		if(mIsEnteringText)
		{
			//Backspace
			if(keyCode == 14)
			{
				if(mSearchText.length() > 0) mSearchText = mSearchText.substring(0, mSearchText.length() - 1);
			}
			//Escape and Enter
			else if(keyCode == 1 || keyCode == 28)
			{
				mIsEnteringText = false;
			}
			//Anything else.
			else
			{
				if(Character.isLetterOrDigit(typedChar)) mSearchText += typedChar;
				else if(typedChar == ' ') mSearchText += typedChar;
			}
			mContainer.SetSearchString(mSearchText);
		} else if(keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
			Minecraft.getMinecraft().player.closeScreen();
		}
	}


	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{

		if(mouseButton == 0)
		{
			//Checks Text Box Bounds
			mIsEnteringText = mouseX > guiLeft + mTextLeftOffset && mouseX < guiLeft + mTextLeftOffset + 88 && mouseY > guiTop + mTextTopOffset && mouseY < guiTop + mTextTopOffset + 10;

			if(mouseX > guiLeft + mEndTrackingButtonLeftOffset && mouseX < guiLeft + mEndTrackingButtonLeftOffset + mEndTrackingButtonWidth && mouseY > guiTop + mEndTrackingButtonTopOffset && mouseY < guiTop + mEndTrackingButtonTopOffset + mEndTrackingButtonHeight)
			{
				AATickHandler.GetInstance().clearChests();
			}

			// TODO: Refresh button
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
    public void drawSlot(Slot slot) {
		super.drawSlot(slot);

		ManifestEntry entry = mContainer.getEntry(slot.getSlotIndex());
		if (entry == null) return;

		if (entry.getDimension() != player.dimension) {
			GlStateManager.disableDepth();
			drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, 0x77000000);
		}
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y)
    {
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        List<String> tooltip = this.getItemToolTip(stack);

        Slot slot = this.getSlotUnderMouse();

        if (slot != null)
		{
			ManifestEntry entry = mContainer.getEntry(slot.slotNumber);
			if (entry != null && entry.getDimension() != player.dimension)
			{
				DimensionType dim = DimensionType.getById(entry.getDimension());
				String name = WordUtils.capitalize(dim.getName().replace("_", " "));
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.inanotherdim", name));
			}
		}

        this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));

        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }
}
