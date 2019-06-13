package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.gui.framework.CustomCountSlot;
import com.aranaira.arcanearchives.client.gui.framework.LayeredGuiContainer;
import com.aranaira.arcanearchives.client.gui.framework.ScrollEventManager;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ConfigHandler.ManifestConfig;
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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.client.config.GuiConfig;
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
	private static int mTextTopOffset = 13;
	private static int mTextLeftOffset = 13;
	private static int mTextWidth = 89;
	private static int mTextHeight = 11;
	// offset and size of "End Tracking" button
	private static int mEndTrackingLeftOffset = 48;
	private static int mEndTrackingTopOffset = 200;
	private static int mEndTrackingButtonWidth = 88;
	private static int mEndTrackingButtonHeight = 12;
	// offset and size of refresh button
	private static int mRefreshButtonLeftOffset = 178;
	private static int mRefreshButtonTopOffset = 200;
	private static int mRefreshButtonWidth = 14;
	private static int mRefreshButtonHeight = 14;
	// offset and side of config button
	private static int mConfigButtonLeftOffset = 158;
	private static int mConfigButtonTopOffset = 200;
	private static int mConfigButtonWidth = 14;
	private static int mConfigButtonHeight = 14;
	// scroll bar area
	private static int mScrollBarTopOffset = 29;
	private static int mScrollBarBottomOffset = 191;
	private static int mScrollBarLeftOffset = 178;
	// offset and size of slot texture in #GUIBaseTextures
	private static int mSlotTextureLeftOffset = 224;
	private static int mSlotTextureSize = 18;


	/**
	 * Color to overlay items in another minecraft dimension than the one that the player is currently in
	 */
	private static int OTHER_DIMENSION = 0x77000000;

	private RightClickTextField searchBox;
	private ScrollBar mScrollBar;
	private GuiButton mEndTrackButton;
	private GuiButton mRefreashButton;
	private GuiButton mConfigButton;

	public GUIManifest (EntityPlayer player, ContainerManifest container) {
		super(container);

		this.scrollEventManager = new ScrollEventManager();
		this.container = container;

		ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
		network.manifestItems.setListener(container);
		container.setScrollEventManager(scrollEventManager);

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
	}

	@Override
	protected void drawTopLevelElements (int mouseX, int mouseY) {
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

			ManifestEntry entry = container.getEntry(slot.getSlotIndex());
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
		ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
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

	@Override
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		switch (keyCode) {
			case Keyboard.KEY_ESCAPE: {
				Minecraft.getMinecraft().displayGuiScreen(null);
				break;
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
		container.SetSearchString(value);
	}
}
