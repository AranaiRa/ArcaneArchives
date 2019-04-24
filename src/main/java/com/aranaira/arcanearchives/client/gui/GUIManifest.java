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
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class GUIManifest extends GuiContainer implements GuiPageButtonList.GuiResponder
{
	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/manifest.png");
	private final EntityPlayer player;
	private ContainerManifest container;
	private int mTextTopOffset = 13;
	private int mTextLeftOffset = 13;
	private int mTextWidth = 89;
	private int mTextHeight = 11;
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

	private RightClickTextField searchBox;

	public GUIManifest(EntityPlayer player, ContainerManifest container) {
		super(container);

		this.container = container;

		this.xSize = 184;
		this.ySize = 224;

		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();

		String searchText = this.container.getSearchString();
		if (searchText == null) {
			searchText = "";
		}

		searchBox = new RightClickTextField(1, fontRenderer, guiLeft + mTextLeftOffset, guiTop + mTextTopOffset, mTextWidth, mTextHeight);
		searchBox.setText(searchText);
		searchBox.setGuiResponder(this);
		searchBox.setEnableBackgroundDrawing(false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		searchBox.drawTextBox();

		this.renderHoveredToolTip(mouseX, mouseY);

		fontRenderer.drawString("End Track", guiLeft + mEndTrackingLeftOffset, mEndTrackingTopOffset + guiTop, 0x000000);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256, 256, 256, 256);
	}

	@Override
	public void drawSlot(Slot slot) {
		super.drawSlot(slot);

		ManifestEntry entry = container.getEntry(slot.getSlotIndex());
		if(entry == null) return;

		if(entry.getDimension() != player.dimension) {
			GlStateManager.disableDepth();
			drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OTHER_DIMENSION);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(searchBox.mouseClicked(mouseX, mouseY, mouseButton)) return;

		if(mouseX > guiLeft + mEndTrackingButtonLeftOffset && mouseX < guiLeft + mEndTrackingButtonLeftOffset + mEndTrackingButtonWidth && mouseY > guiTop + mEndTrackingButtonTopOffset && mouseY < guiTop + mEndTrackingButtonTopOffset + mEndTrackingButtonHeight) {
			LineHandler.clearChests(player.dimension);
		}

		if(mouseX > guiLeft + mRefreshButtonLeftOffset && mouseX < guiLeft + mRefreshButtonLeftOffset + mRefreshButtonWidth && mouseY > guiTop + mRefreshButtonTopOffset && mouseY < guiTop + mRefreshButtonTopOffset + mRefreshButtonHeight) {
			ClientNetwork network = NetworkHelper.getClientNetwork(player.getUniqueID());
			network.synchroniseManifest();
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		container.slotClick(slotId, mouseButton, type, player);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_ESCAPE) Minecraft.getMinecraft().displayGuiScreen(null);

		if(searchBox.textboxKeyTyped(typedChar, keyCode)) return;

		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y) {
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
		List<String> tooltip = this.getItemToolTip(stack);

		Slot slot = this.getSlotUnderMouse();

		if(slot != null) {
			ManifestEntry entry = container.getEntry(slot.slotNumber);
			if(entry != null && entry.getDimension() != player.dimension) {
				DimensionType dim = DimensionType.getById(entry.getDimension());
				String name = WordUtils.capitalize(dim.getName().replace("_", " "));
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.manifest.inanotherdim", name));
			} else if (entry != null) {
				tooltip.add("");
				tooltip.add("" + TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.manifest.clicktoshow", I18n.format("arcanearchives.text.manifest.endtrackingbutton")));
			}
			if(entry != null) {
				if(GuiScreen.isShiftKeyDown()) {
					tooltip.add("");
					List<ManifestEntry.ItemEntry> positions = entry.consolidateEntries(false);
					int unnamed_count = 1;
					int limit = Math.min(10, positions.size());
					int diff = Math.max(0, positions.size() - limit);
					for(int i = 0; i < limit; i++) {
						ManifestEntry.ItemEntry thisEntry = positions.get(i);
						String chestName = thisEntry.getChestName();
						BlockPos pos = thisEntry.getPosition();
						if(chestName.isEmpty())
							chestName = String.format("%s %d", I18n.format("arcanearchives.text.radiantchest.unnamed_chest"), unnamed_count++);
						tooltip.add(TextFormatting.GRAY + I18n.format("arcanearchives.tooltip.manifest.item_entry", chestName, pos.getX(), pos.getY(), pos.getZ(), thisEntry.getItemCount()));
					}
					if(diff > 0) {
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
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}

	@Override
	public void setEntryValue(int id, boolean value) {
	}

	@Override
	public void setEntryValue(int id, float value) {
	}

	@Override
	public void setEntryValue(int id, String value) {
		container.SetSearchString(value);
	}
}
