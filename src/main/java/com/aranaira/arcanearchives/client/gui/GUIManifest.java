package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.inventory.ContainerManifest;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class GUIManifest extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/manifest.png");
	private final EntityPlayer player;
	private String searchText = "";
	private boolean isEnteringText = false;
	private ContainerManifest container;
	private int mTextTopOffset = 14;
	private int mTextLeftOffset = 13;
	private int mEndTrackingLeftOffset = 67;
	private int mEndTrackingTopOffset = 202;
	private int mEndTrackingButtonLeftOffset = 65;
	private int mEndTrackingButtonTopOffset = 200;
	private int mEndTrackingButtonWidth = 54;
	private int mEndTrackingButtonHeight = 14;
	private int OTHER_DIMENSION = 0x77000000;

	private int mRefreshButtonTopOffset = 199;
	private int mRefreshButtonLeftOffset = 155;
	private int mRefreshButtonWidth = 17;
	private int mRefreshButtonHeight = 14;

	public GUIManifest(EntityPlayer player, ContainerManifest container)
	{
		super(container);

		this.container = container;

		this.xSize = 184;
		this.ySize = 224;

		this.player = player;

		String text = this.container.getSearchString();
		if(text != null && !text.isEmpty())
		{
			searchText = text;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		String temp = fontRenderer.trimStringToWidth(searchText, 6 * 15, true);

		if(searchText.equals(""))
			fontRenderer.drawString("Search", guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x000000);
		else if(isEnteringText)
			fontRenderer.drawString(temp, guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x4363ff);
		else fontRenderer.drawString(temp, guiLeft + mTextLeftOffset, mTextTopOffset + guiTop, 0x000000);

		fontRenderer.drawString("End Track", guiLeft + mEndTrackingLeftOffset, mEndTrackingTopOffset + guiTop, 0x000000);
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
		if(isEnteringText)
		{
			//Backspace
			if(keyCode == 14)
			{
				if(searchText.length() > 0) searchText = searchText.substring(0, searchText.length() - 1);
			}
			//Escape and Enter
			else if(keyCode == 1 || keyCode == 28)
			{
				isEnteringText = false;
			}
			//Anything else.
			else
			{
				if(Character.isLetterOrDigit(typedChar)) searchText += typedChar;
				else if(typedChar == ' ') searchText += typedChar;
			}
			container.SetSearchString(searchText);
		} else if(keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			Minecraft.getMinecraft().player.closeScreen();
		}
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
	{
		container.slotClick(slotId, mouseButton, type, player);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		//Checks Text Box Bounds
		isEnteringText = mouseX > guiLeft + mTextLeftOffset && mouseX < guiLeft + mTextLeftOffset + 88 && mouseY > guiTop + mTextTopOffset && mouseY < guiTop + mTextTopOffset + 10;

		if (isEnteringText) {
			// This is a right click!
			searchText = "";
			container.SetSearchString(searchText);
		}

		if(mouseX > guiLeft + mEndTrackingButtonLeftOffset && mouseX < guiLeft + mEndTrackingButtonLeftOffset + mEndTrackingButtonWidth && mouseY > guiTop + mEndTrackingButtonTopOffset && mouseY < guiTop + mEndTrackingButtonTopOffset + mEndTrackingButtonHeight)
		{
			LineHandler.clearChests(player.dimension);
		}

		if(mouseX > guiLeft + mRefreshButtonLeftOffset && mouseX < guiLeft + mRefreshButtonLeftOffset + mRefreshButtonWidth && mouseY > guiTop + mRefreshButtonTopOffset && mouseY < guiTop + mRefreshButtonTopOffset + mRefreshButtonHeight)
		{
			ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
			network.synchroniseManifest();
		}

		// TODO: Refresh button


		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawSlot(Slot slot)
	{
		super.drawSlot(slot);

		ManifestEntry entry = container.getEntry(slot.getSlotIndex());
		if(entry == null) return;

		if(entry.getDimension() != player.dimension)
		{
			GlStateManager.disableDepth();
			drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OTHER_DIMENSION);
		}
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y)
	{
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
		List<String> tooltip = this.getItemToolTip(stack);

		Slot slot = this.getSlotUnderMouse();

		if(slot != null)
		{
			ManifestEntry entry = container.getEntry(slot.slotNumber);
			if(entry != null && entry.getDimension() != player.dimension)
			{
				DimensionType dim = DimensionType.getById(entry.getDimension());
				String name = WordUtils.capitalize(dim.getName().replace("_", " "));
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.manifest.inanotherdim", name));
			} else if(entry != null)
			{
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.manifest.clicktoshow", I18n.format("arcanearchives.text.manifest.endtrackingbutton")));
			}
			if(entry != null)
			{
				if(GuiScreen.isShiftKeyDown())
				{
					tooltip.add("");
					List<ManifestEntry.ItemEntry> positions = entry.consolidateEntries(false);
					int unnamed_count = 1;
					int limit = Math.min(10, positions.size());
					int diff = Math.max(0, positions.size() - limit);
					for(int i = 0; i < limit; i++)
					{
						ManifestEntry.ItemEntry thisEntry = positions.get(i);
						String chestName = thisEntry.getChestName();
						BlockPos pos = thisEntry.getPosition();
						if(chestName.isEmpty())
							chestName = String.format("%s %d", I18n.format("arcanearchives.text.radiantchest.unnamed_chest"), unnamed_count++);
						tooltip.add(TextFormatting.GRAY + I18n.format("arcanearchives.tooltip.manifest.item_entry", chestName, pos.getX(), pos.getY(), pos.getZ(), thisEntry.getItemCount()));
					}
					if(diff > 0)
					{
						tooltip.add(I18n.format("arcanearchives.tooltip.manifest.andmore", diff));
					}
				} else
				{
					tooltip.add("" + TextFormatting.DARK_GRAY + I18n.format("arcanearchives.tooltip.manifest.chestsneak"));
				}
			}
		}

		this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));

		net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
	}
}
