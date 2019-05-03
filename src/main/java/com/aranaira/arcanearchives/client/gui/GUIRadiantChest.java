package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ManifestTracking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;
import org.lwjgl.input.Keyboard;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

import java.io.IOException;
import java.util.List;

@Optional.InterfaceList({@Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IChestButtonCallback", striprefs = true), @Optional.Interface(modid = "quark", iface = "vazkii.quark.api.IItemSearchBar", striprefs = true)})
public class GUIRadiantChest extends GuiContainer implements IChestButtonCallback, IItemSearchBar, GuiPageButtonList.GuiResponder {
	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantchest.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/radiantchest.png");
	private final int ImageHeight = 253, ImageWidth = 192, ImageScale = 256;
	private ContainerRadiantChest mContainer;

	private int mNameTextLeftOffset = 53;
	private int mNameTextTopOffset = 238;
	private int mNameTextWidth = 88;
	private int mNameTextHeight = 10;
	private List<Ingredient> tracked;
	private int dimension;
	private BlockPos pos;
	private RightClickTextField nameBox;

	public GUIRadiantChest (ContainerRadiantChest inventorySlotsIn, EntityPlayer player) {
		super(inventorySlotsIn);

		this.mContainer = inventorySlotsIn;

		RadiantChestTileEntity tile = inventorySlotsIn.getTile();
		this.dimension = tile.dimension;
		this.pos = tile.getPos();
		tracked = ManifestTracking.get(dimension, pos);

		this.xSize = ImageWidth;
		this.ySize = ImageHeight;
	}

	@Override
	public void initGui () {
		super.initGui();

		nameBox = new RightClickTextField(1, fontRenderer, guiLeft + mNameTextLeftOffset, guiTop + mNameTextTopOffset, mNameTextWidth, mNameTextHeight);
		nameBox.setText(mContainer.getName());
		nameBox.setGuiResponder(this);
		nameBox.setEnableBackgroundDrawing(false);

		buttonList.clear();
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		nameBox.drawTextBox();

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		if(ConfigHandler.UsePrettyGUIs)
			this.mc.getTextureManager().bindTexture(GUITextures);
		else
			this.mc.getTextureManager().bindTexture(GUITexturesSimple);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, ImageScale, ImageScale, ImageScale, ImageScale);
	}

	@Override
	public void drawSlot (Slot slot) {
		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) {
			if (tracked != null && !tracked.isEmpty() && ManifestTracking.matches(stack, tracked)) {
				GlStateManager.disableDepth();
				float partialTicks = this.mc.getTickLength();
				drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, ConfigHandler.MANIFEST_HIGHLIGHT);
				GlStateManager.enableDepth();
			}
		}

		super.drawSlot(slot);
	}

	@Override
	protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		nameBox.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}

		if (nameBox.textboxKeyTyped(typedChar, keyCode)) {
			return;
		}

		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void onGuiClosed () {
		super.onGuiClosed();

		if (tracked != null) {
			ManifestTracking.remove(dimension, pos);
		}
	}

	@Override
	public void updateScreen () {
		super.updateScreen();
	}

	@Optional.Method(modid = "quark")
	@Override
	public boolean onAddChestButton (GuiButton button, int buttonType) {
		return true;
	}

	@Optional.Method(modid = "quark")
	@Override
	public void onSearchBarAdded (GuiTextField bar) {
		bar.y = (height / 2) + 2;
		bar.x = (width / 2) - bar.width / 2;
	}

	@Override
	public void setEntryValue (int id, boolean value) {
	}

	@Override
	public void setEntryValue (int id, float value) {
	}

	@Override
	public void setEntryValue (int id, String value) {
		mContainer.setName(value);
	}
}
