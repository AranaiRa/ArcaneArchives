package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.inventory.ContainerManifest;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

public class GUIManifest extends AbstractLayeredGuiContainer implements GuiPageButtonList.GuiResponder {
	private static final ResourceLocation GUIBaseTextures = new ResourceLocation("arcanearchives:textures/gui/manifest_base.png");
	private static final int mGUIBaseTexturesSize = 256;
	private static final ResourceLocation GUIForegroundTextures = new ResourceLocation("arcanearchives:textures/gui/manifest_overlay.png");
	private static final float mGUIForegroundTexturesSize = 256;
	private final EntityPlayer player;
	private ContainerManifest container;
	// offset and size of search box
	private static int mTextTopOffset = 13;
	private static int mTextLeftOffset = 13;
	private static int mTextWidth = 89;
	private static int mTextHeight = 11;
	// offset and size of "End Tracking" button
	private static int mEndTrackingLeftOffset = 67;
	private static int mEndTrackingTopOffset = 202;
	private static int mEndTrackingButtonLeftOffset = 65;
	private static int mEndTrackingButtonTopOffset = 200;
	private static int mEndTrackingButtonWidth = 54;
	private static int mEndTrackingButtonHeight = 14;
	// offset and size of refresh button
	private static int mRefreshButtonTopOffset = 199;
	private static int mRefreshButtonLeftOffset = 155;
	private static int mRefreshButtonWidth = 17;
	private static int mRefreshButtonHeight = 14;
	// initial offset of scroll nub
	private static int mScrollNubTopOffset = 67;
	private static int mScrollNubLeftOffset = 155;
	// offset and size of slot texture in #GUIBaseTextures
	private static int mSlotTextureLeftOffset = 224;
	private static int mSlotTextureSize = 18;


	/**
	 * Color to overlay items in another minecraft dimension than the one that the player is currently in
	 */
	private static int OTHER_DIMENSION = 0x77000000;

	private RightClickTextField searchBox;
	private TexturedButton mScrollNub;

	public GUIManifest (EntityPlayer player, ContainerManifest container) {
		super(container);

		this.container = container;

		ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
		network.manifestItems.setListener(container);

		this.xSize = 200;
		this.ySize = 224;

		this.player = player;
	}

	@Override
	public void initGui () {
		super.initGui();

		String searchText = this.container.getSearchString();
		if (searchText == null) {
			searchText = "";
		}

		searchBox = new RightClickTextField(1, fontRenderer, guiLeft + mTextLeftOffset, guiTop + mTextTopOffset, mTextWidth, mTextHeight);
		searchBox.setText(searchText);
		searchBox.setGuiResponder(this);
		searchBox.setEnableBackgroundDrawing(false);

		mScrollNub = new TexturedButton(0, 0, guiLeft + mScrollNubLeftOffset, guiTop + mScrollNubTopOffset);
	}

	@Override
	protected void drawTopLevelElements (int mouseX, int mouseY) {
		searchBox.drawTextBox();

		// TODO scroll bar utility class
		//mScrollNub.drawButton(mc, mouseX, mouseY, 0);

		this.renderHoveredToolTip(mouseX, mouseY);

		fontRenderer.drawString("End Track", guiLeft + mEndTrackingLeftOffset, mEndTrackingTopOffset + guiTop, 0x000000);
	}

	@Override
	protected void drawForegroundContents (int mouseX, int mouseY) {
		super.drawForegroundContents(mouseX, mouseY);

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUIForegroundTextures);

		// for some reason this seems to be relative x and y position
		drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, xSize, ySize, mGUIForegroundTexturesSize, mGUIForegroundTexturesSize);
	}

	@Override
	protected void drawBackgroundContents (int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUIBaseTextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, mGUIBaseTexturesSize, mGUIBaseTexturesSize);
	}

	@Override
	public void drawSlot (Slot slot) {
		super.drawSlot(slot);

		ManifestEntry entry = container.getEntry(slot.getSlotIndex());
		if (entry == null) {
			return;
		}

		{
			GlStateManager.disableLighting();
			this.mc.getTextureManager().bindTexture(GUIBaseTextures);
			drawModalRectWithCustomSizedTexture(slot.xPos-1, slot.yPos-1, mSlotTextureLeftOffset, 0, mSlotTextureSize, mSlotTextureSize, mGUIBaseTexturesSize, mGUIBaseTexturesSize);
			GlStateManager.enableLighting();
		}

		if (entry.getDimension() != player.dimension) {
			GlStateManager.disableDepth();
			drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OTHER_DIMENSION);
		}
	}

	@Override
	protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {
		if (searchBox.mouseClicked(mouseX, mouseY, mouseButton)) {
			return;
		}

		if (mouseX > guiLeft + mEndTrackingButtonLeftOffset && mouseX < guiLeft + mEndTrackingButtonLeftOffset + mEndTrackingButtonWidth && mouseY > guiTop + mEndTrackingButtonTopOffset && mouseY < guiTop + mEndTrackingButtonTopOffset + mEndTrackingButtonHeight) {
			LineHandler.clearChests(player.dimension);
		}

		if (mouseX > guiLeft + mRefreshButtonLeftOffset && mouseX < guiLeft + mRefreshButtonLeftOffset + mRefreshButtonWidth && mouseY > guiTop + mRefreshButtonTopOffset && mouseY < guiTop + mRefreshButtonTopOffset + mRefreshButtonHeight) {
			ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
			network.synchroniseManifest();
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void handleMouseClick (Slot slotIn, int slotId, int mouseButton, ClickType type) {
		container.slotClick(slotId, mouseButton, type, player);
	}

	@Override
	public void handleMouseInput () throws IOException {
		super.handleMouseInput();

		// this returns the delta z since last poll
		int wheelState = Mouse.getEventDWheel();
		if (wheelState > 0) {
			container.stepPositionDown();
		} else if (wheelState < 0) {
			container.stepPositionUp();
		}
	}

	@Override
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		switch (keyCode) {
			case Keyboard.KEY_ESCAPE: {
				Minecraft.getMinecraft().displayGuiScreen(null);
				break;
			}
			default: {
				// no-op
				break;
			}
		}

		if (searchBox.textboxKeyTyped(typedChar, keyCode)) {
			return;
		}

		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean doesGuiPauseGame () {
		return false;
	}

	@Override
	protected void renderToolTip (ItemStack stack, int x, int y) {
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
		List<String> tooltip = this.getItemToolTip(stack);

		Slot slot = this.getSlotUnderMouse();

		if (slot != null) {
			ManifestEntry entry = container.getEntry(slot.slotNumber);
			if (entry != null && entry.getDimension() != player.dimension) {
				DimensionType dim = DimensionType.getById(entry.getDimension());
				String name = WordUtils.capitalize(dim.getName().replace("_", " "));
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.manifest.inanotherdim", name));
			} else if (entry != null) {
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.manifest.clicktoshow", I18n.format("arcanearchives.text.manifest.endtrackingbutton")));
			}
			if (entry != null) {
				if (GuiScreen.isShiftKeyDown()) {
					tooltip.add("");
					List<ManifestEntry.ItemEntry> positions = entry.consolidateEntries(false);
					int unnamed_count = 1;
					int limit = Math.min(10, positions.size());
					int diff = Math.max(0, positions.size() - limit);
					for (int i = 0; i < limit; i++) {
						ManifestEntry.ItemEntry thisEntry = positions.get(i);
						String chestName = thisEntry.getChestName();
						BlockPos pos = thisEntry.getPosition();
						if (chestName.isEmpty()) {
							chestName = String.format("%s %d", I18n.format("arcanearchives.text.radiantchest.unnamed_chest"), unnamed_count++);
						}
						tooltip.add(TextFormatting.GRAY + I18n.format("arcanearchives.tooltip.manifest.item_entry", chestName, pos.getX(), pos.getY(), pos.getZ(), thisEntry.getItemCount()));
					}
					if (diff > 0) {
						tooltip.add(I18n.format("arcanearchives.tooltip.manifest.andmore", diff));
					}
				} else {
					tooltip.add("" + TextFormatting.DARK_GRAY + I18n.format("arcanearchives.tooltip.manifest.chestsneak"));
				}
			}
		}

		this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));

		net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
	}

	@Override
	protected void actionPerformed (GuiButton button) throws IOException {
		super.actionPerformed(button);
	}

	@Override
	public void setEntryValue (int id, boolean value) {
	}

	@Override
	public void setEntryValue (int id, float value) {
	}

	@Override
	public void setEntryValue (int id, String value) {
		container.SetSearchString(value);
	}
}
