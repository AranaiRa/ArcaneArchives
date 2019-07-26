package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.controls.InvisibleButton;
import com.aranaira.arcanearchives.client.gui.controls.ManifestSearchField;
import com.aranaira.arcanearchives.client.gui.controls.ScrollBar;
import com.aranaira.arcanearchives.client.gui.framework.CustomCountSlot;
import com.aranaira.arcanearchives.client.gui.framework.LayeredGuiContainer;
import com.aranaira.arcanearchives.client.gui.framework.ScrollEventManager;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.client.render.LineHandler;
import com.aranaira.arcanearchives.integration.jei.JEIPlugin;
import com.aranaira.arcanearchives.inventory.ContainerManifest;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import com.aranaira.arcanearchives.util.ManifestUtils.EntryDescriptor;
import com.aranaira.arcanearchives.types.lists.ManifestList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class GUIManifest extends LayeredGuiContainer implements GuiPageButtonList.GuiResponder {
	private static final ResourceLocation GUIBaseTextures = new ResourceLocation("arcanearchives:textures/gui/manifest_base.png");
	private static final ResourceLocation GUIBaseTexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/manifest_base.png");
	private static final int mGUIBaseTexturesSize = 256;
	private static final ResourceLocation GUIForegroundTextures = new ResourceLocation("arcanearchives:textures/gui/manifest_overlay.png");
	private static final ResourceLocation GUIForegroundTexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/manifest_overlay.png");
	private static final float mGUIForegroundTexturesSize = 256;
	private final EntityPlayer player;
	private ContainerManifest container;
	private ScrollEventManager scrollEventManager;
	// offset and size of search box
	private static final int mTextTopOffset = 13;
	private static final int mTextLeftOffset = 13;
	private static final int mTextWidth = 89;
	private static final int mTextHeight = 11;
	// offset and size of "End Tracking" button
	private static final int mEndTrackingLeftOffset = 48;
	private static final int mEndTrackingTopOffset = 200;
	private static final int mEndTrackingButtonWidth = 88;
	private static final int mEndTrackingButtonHeight = 12;
	// offset and size of refresh button
	private static final int mRefreshButtonLeftOffset = 178;
	private static final int mRefreshButtonTopOffset = 200;
	private static final int mRefreshButtonWidth = 14;
	private static final int mRefreshButtonHeight = 14;
	// offset and side of config button
	private static final int mConfigButtonLeftOffset = 158;
	private static final int mConfigButtonTopOffset = 200;
	private static final int mConfigButtonWidth = 14;
	private static final int mConfigButtonHeight = 14;
	// scroll bar area
	private static final int mScrollBarTopOffset = 29;
	private static final int mScrollBarBottomOffset = 191;
	private static final int mScrollBarLeftOffset = 178;
	// offset and size of slot texture in #GUIBaseTextures
	private static final int mSlotTextureLeftOffset = 224;
	private static final int mSlotTextureSize = 18;
	// offset and size of alphabetical/quantity button
	private static final int mAlphaQuantButtonLeftOffset = 242;
	private static final int mAlphaQuantButtonTopOffset = 28;
	private static final int mAlphaQuantButtonSize = 14;
	// offset and size of ascending/descending button
	private static final int mAscDescButtonLeftOffset = 242;
	private static final int mAscDescButtonTopOffset = 56;
	private static final int mAscDescButtonSize = 14;
	// offset and size of JEI sync button
	private static final int mJEISyncButtonLeftOffset = 242;
	private static final int mJEISyncButtonTopOffset = 84;
	private static final int mJEISyncButtonSize = 14;

	public static boolean doJEIsync = false;

	/**
	 * Color to overlay items in another minecraft dimension than the one that the player is currently in
	 */
	private static int OTHER_DIMENSION = 0x77000000;

	private ManifestSearchField searchBox;
	private ScrollBar mScrollBar;
	private GuiButton mEndTrackButton;
	private GuiButton mRefreashButton;
	private GuiButton mConfigButton;
	private GuiButton mAlphaQuantButton;
	private GuiButton mAscDescButton;
	private GuiButton mJEIsync;
	private String storedJEI = "";

	public GUIManifest (EntityPlayer player, ContainerManifest container) {
		super(container);

		this.scrollEventManager = new ScrollEventManager();
		this.container = container;

		ClientNetwork network = DataHelper.getClientNetwork(player.getUniqueID());
		network.manifestItems.setListener(container);
		container.setScrollEventManager(scrollEventManager);

		this.xSize = 200;
		this.ySize = 224;

		this.player = player;

		if (Loader.isModLoaded("jei")) {
			doJEIsync = ConfigHandler.ManifestConfig.jeiSynchronise;
			this.storedJEI = JEIPlugin.runtime.getIngredientFilter().getFilterText();
		}
	}

	public boolean getJEISync () {
		return doJEIsync;
	}

	public void toggleJEISync () {
		doJEIsync = !doJEIsync;
	}

	@Override
	public void initGui () {
		super.initGui();

		String searchText = this.container.getSearchString();
		if (searchText == null) {
			searchText = "";
		}

		searchBox = new ManifestSearchField(this, 1, fontRenderer, guiLeft + mTextLeftOffset, guiTop + mTextTopOffset, mTextWidth, mTextHeight);
		searchBox.setText(searchText);
		searchBox.setGuiResponder(this);
		searchBox.setEnableBackgroundDrawing(false);
		searchBox.syncFromJEI();

		mScrollBar = new ScrollBar(10, guiLeft + mScrollBarLeftOffset, guiTop + mScrollBarTopOffset, guiTop + mScrollBarBottomOffset);
		scrollEventManager.registerListener(mScrollBar);
		addButton(mScrollBar);
		addButton(mScrollBar.mNub);

		mEndTrackButton = new InvisibleButton(0, guiLeft + mEndTrackingLeftOffset, guiTop + mEndTrackingTopOffset, mEndTrackingButtonWidth, mEndTrackingButtonHeight, "End Tracking");
		addButton(mEndTrackButton);

		mRefreashButton = new InvisibleButton(1, guiLeft + mRefreshButtonLeftOffset, guiTop + mRefreshButtonTopOffset, mRefreshButtonWidth, mRefreshButtonHeight, "");
		addButton(mRefreashButton);

		mConfigButton = new InvisibleButton(2, guiLeft + mConfigButtonLeftOffset, guiTop + mConfigButtonTopOffset, mConfigButtonWidth, mConfigButtonHeight, "");
		addButton(mConfigButton);

		mAlphaQuantButton = new InvisibleButton(3, guiLeft + 112, guiTop + 10, mAlphaQuantButtonSize, mAlphaQuantButtonSize, "");
		addButton(mAlphaQuantButton);

		mAscDescButton = new InvisibleButton(4, guiLeft + 130, guiTop + 10, mAscDescButtonSize, mAscDescButtonSize, "");
		addButton(mAscDescButton);

		mJEIsync = new InvisibleButton(5, guiLeft + 148, guiTop + 10, mAscDescButtonSize, mAscDescButtonSize, "");
		addButton(mJEIsync);
	}

	@Override
	protected void drawTopLevelElements (int mouseX, int mouseY) {
		if (doJEIsync) {
			searchBox.syncFromJEI();
		}
		searchBox.drawTextBox();

		// make sure tool tip is on top of everything else
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, 50f);

		renderHoveredToolTip(mouseX, mouseY);

		// clean up GL state
		GlStateManager.popMatrix();
	}

	@Override
	protected void drawForegroundContents (int mouseX, int mouseY) {
		super.drawForegroundContents(mouseX, mouseY);

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableColorMaterial();
		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(GUIForegroundTextures);
		} else {
			this.mc.getTextureManager().bindTexture(GUIForegroundTexturesSimple);
		}

		// for some reason this seems to be relative x and y position
		drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, xSize, ySize, mGUIForegroundTexturesSize, mGUIForegroundTexturesSize);

		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(GUIBaseTextures);
		} else {
			this.mc.getTextureManager().bindTexture(GUIBaseTexturesSimple);
		}

		int alphaQuantShift = 0;
		if (container.getSortingType() == ManifestList.SortingType.QUANTITY) {
			alphaQuantShift = mAlphaQuantButtonSize;
		}
		drawModalRectWithCustomSizedTexture(112, 10, mAlphaQuantButtonLeftOffset, mAlphaQuantButtonTopOffset + alphaQuantShift, mAlphaQuantButtonSize, mAlphaQuantButtonSize, mGUIForegroundTexturesSize, mGUIForegroundTexturesSize);

		int ascDescShift = 0;
		if (container.getSortingDirection() == ManifestList.SortingDirection.ASCENDING) {
			ascDescShift = mAscDescButtonSize;
		}
		drawModalRectWithCustomSizedTexture(130, 10, mAscDescButtonLeftOffset, mAscDescButtonTopOffset + ascDescShift, mAscDescButtonSize, mAscDescButtonSize, mGUIForegroundTexturesSize, mGUIForegroundTexturesSize);

		int JEISyncShift = 0;
		if (getJEISync()) {
			JEISyncShift = mJEISyncButtonSize;
		}
		drawModalRectWithCustomSizedTexture(148, 10, mJEISyncButtonLeftOffset, mJEISyncButtonTopOffset + JEISyncShift, mJEISyncButtonSize, mJEISyncButtonSize, mGUIForegroundTexturesSize, mGUIForegroundTexturesSize);
	}

	@Override
	protected void drawBackgroundContents (int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(GUIBaseTextures);
		} else {
			this.mc.getTextureManager().bindTexture(GUIBaseTexturesSimple);
		}

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, mGUIBaseTexturesSize, mGUIBaseTexturesSize);
	}

	@Override
	public void drawSlot (Slot slot) {
		if (slot.isEnabled()) {
			super.drawSlot(slot);

			if (slot instanceof CustomCountSlot) {
				((CustomCountSlot) slot).renderCount(this.fontRenderer);
			}

			CollatedEntry entry = container.getEntry(slot.getSlotIndex());
			if (entry == null) {
				return;
			}

			{
				GlStateManager.disableLighting();
				if (ConfigHandler.UsePrettyGUIs) {
					this.mc.getTextureManager().bindTexture(GUIBaseTextures);
				} else {
					this.mc.getTextureManager().bindTexture(GUIBaseTexturesSimple);
				}
				if (!ConfigHandler.ManifestConfig.DisableManifestGrid) {
					drawModalRectWithCustomSizedTexture(slot.xPos - 1, slot.yPos - 1, mSlotTextureLeftOffset, 0, mSlotTextureSize, mSlotTextureSize, mGUIBaseTexturesSize, mGUIBaseTexturesSize);
				}
				GlStateManager.enableLighting();
			}

			if (entry.getDimension() != player.dimension || entry.outOfRange) {
				GlStateManager.disableDepth();
				drawRectWithBlend(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OTHER_DIMENSION);
				GlStateManager.enableDepth();
			}
		}
	}

	public static void drawRectWithBlend (int left, int top, int right, int bottom, int color) {
		if (left < right) {
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			int j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
	}

	public void doRefresh () {
		ClientNetwork network = DataHelper.getClientNetwork(player.getUniqueID());
		network.synchroniseManifest();
	}

	@Override
	protected void actionPerformed (GuiButton button) throws IOException {
		if (button.id == mEndTrackButton.id) {
			LineHandler.clearChests(player.dimension);
		} else if (button.id == mRefreashButton.id) {
			doRefresh();
		} else if (button.id == mConfigButton.id) {
			GuiConfig config = new GuiConfig(this, ArcaneArchives.MODID, false, false, ArcaneArchives.NAME, ConfigHandler.class);
			this.mc.displayGuiScreen(config);
		} else if (button.id == mAlphaQuantButton.id) {
			if (container.getSortingType() == ManifestList.SortingType.QUANTITY) {
				container.setSortingType(ManifestList.SortingType.NAME);
			} else {
				container.setSortingType(ManifestList.SortingType.QUANTITY);
			}
		} else if (button.id == mAscDescButton.id) {
			if (container.getSortingDirection() == ManifestList.SortingDirection.ASCENDING) {
				container.setSortingDirection(ManifestList.SortingDirection.DESCENDING);
			} else {
				container.setSortingDirection(ManifestList.SortingDirection.ASCENDING);
			}
		} else if (button.id == mJEIsync.id) {
			if (!doJEIsync) {
				this.searchBox.syncToJEI(true);
			}
			this.toggleJEISync();
		}

		super.actionPerformed(button);
	}

	@Override
	protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {
		if (searchBox.mouseClicked(mouseX, mouseY, mouseButton)) {
			return;
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
			scrollEventManager.arrowUp();
		} else if (wheelState < 0) {
			scrollEventManager.arrowDown();
		}
	}

	public void maybeRestoreJEI () {
		if (Loader.isModLoaded("jei")) {
			if (!storedJEI.isEmpty()) {
				JEIPlugin.runtime.getIngredientFilter().setFilterText(storedJEI);
			}
		}
	}

	@Override
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		if (searchBox.isFocused() && searchBox.textboxKeyTyped(typedChar, keyCode)) {
			return;
		}

		switch (keyCode) {
			case Keyboard.KEY_ESCAPE: {
				maybeRestoreJEI();
				mc.player.connection.sendPacket(new CPacketCloseWindow(mc.player.openContainer.windowId));
				mc.player.openContainer = mc.player.inventoryContainer;
				mc.player.inventoryContainer.windowId = 0; // DON'T ASK ME I STOLE THIS FROM HELLFIRE				break;
			}
			case Keyboard.KEY_UP: {
				scrollEventManager.arrowUp();
				break;
			}
			case Keyboard.KEY_DOWN: {
				scrollEventManager.arrowDown();
				break;
			}
			case Keyboard.KEY_PRIOR: {
				scrollEventManager.pageUp();
				break;
			}
			case Keyboard.KEY_NEXT: {
				scrollEventManager.pageDown();
				break;
			}
			default: {
				// no-op
				break;
			}
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
			CollatedEntry entry = container.getEntry(slot.slotNumber);
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
					List<EntryDescriptor> descriptors = entry.descriptions;
					int unnamed_count = 1;
					int limit = Math.min(10, descriptors.size());
					int diff = Math.max(0, descriptors.size() - limit);
					for (int i = 0; i < limit; i++) {
						EntryDescriptor thisEntry = descriptors.get(i);
						String chestName = thisEntry.string;
						BlockPos pos = thisEntry.pos;
						if (chestName.isEmpty()) {
							chestName = String.format("%s %d", I18n.format("arcanearchives.text.radiantchest.unnamed_chest"), unnamed_count++);
						}
						tooltip.add(TextFormatting.GRAY + I18n.format("arcanearchives.tooltip.manifest.entry", chestName, pos.getX(), pos.getY(), pos.getZ(), thisEntry.getItemCount()));
					}
					if (diff > 0) {
						tooltip.add(I18n.format("arcanearchives.tooltip.manifest.andmore", diff));
					}
				} else {
					tooltip.add("" + TextFormatting.DARK_GRAY + I18n.format("arcanearchives.tooltip.manifest.chestsneak"));
				}
				if (entry.outOfRange) {
					tooltip.add("" + TextFormatting.DARK_GRAY + I18n.format("arcanearchives.tooltip.manifest.outofrange", ConfigHandler.ManifestConfig.MaxDistance));
				}
			}
		}

		this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));

		net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
	}

	@Override
	public void setEntryValue (int id, boolean value) {
	}

	@Override
	public void setEntryValue (int id, float value) {
	}

	@Override
	public void setEntryValue (int id, String value) {
		container.setSearchString(value);
	}

	@Override
	public void onGuiClosed () {
		maybeRestoreJEI();
	}
}
