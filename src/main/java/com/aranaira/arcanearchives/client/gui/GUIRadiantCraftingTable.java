package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable.SlotIRecipe;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketRadiantCrafting.SetRecipe;
import com.aranaira.arcanearchives.network.PacketRadiantCrafting.TryCraftRecipe;
import com.aranaira.arcanearchives.network.PacketRadiantCrafting.UnsetRecipe;
import com.aranaira.arcanearchives.util.ColorUtils;
import com.aranaira.arcanearchives.util.ColorUtils.Color;
import com.aranaira.arcanearchives.util.KeyboardUtil;
import com.aranaira.arcanearchives.util.ManifestTrackingUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class GUIRadiantCraftingTable extends GuiContainer {

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantcraftingtable.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/radiantcraftingtable.png");
	private ContainerRadiantCraftingTable container;

	private List<ItemStack> tracked;

	private int mouseOverX = 0;
	private int mouseOverY = 0;

	public GUIRadiantCraftingTable (EntityPlayer player, ContainerRadiantCraftingTable container) {
		super(container);
		this.xSize = 206;
		this.ySize = 203;

		tracked = ManifestTrackingUtils.get(player.dimension, container.pos);
		this.container = container;
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.mouseOverX = mouseX;
		this.mouseOverY = mouseY;
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		if (ConfigHandler.UsePrettyGUIs) {
			this.mc.getTextureManager().bindTexture(GUITextures);
		} else {
			this.mc.getTextureManager().bindTexture(GUITexturesSimple);
		}

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256, 256, 256, 256);
	}

	@Override
	public boolean doesGuiPauseGame () {
		return false;
	}

	@Override
	protected void handleMouseClick (Slot slotIn, int slotId, int mouseButton, ClickType type) {
		if (slotIn instanceof SlotIRecipe) {
			SlotIRecipe slot = (SlotIRecipe) slotIn;
			boolean shiftDown = KeyboardUtil.isShiftDown();
			if (slot.getHasStack() && shiftDown) {
				UnsetRecipe packet = new UnsetRecipe(container.tile.getPos(), container.tile.dimension, slot.getRecipe());
				Networking.CHANNEL.sendToServer(packet);
				return;
			} else if (!slot.getHasStack() && shiftDown) {
				IRecipe toUse = container.getLastCraftedRecipe();
				SetRecipe packet = new SetRecipe(container.tile.getPos(), container.tile.dimension, slot.getRecipe(), toUse);
				Networking.CHANNEL.sendToServer(packet);
				return;
			} else if (!slot.getHasStack() && !shiftDown) {
				IRecipe toUse = container.getCurrentRecipe();
				SetRecipe packet = new SetRecipe(container.tile.getPos(), container.tile.dimension, slot.getRecipe(), toUse);
				Networking.CHANNEL.sendToServer(packet);
				return;
			} else if (slot.getHasStack() && !shiftDown) {
				TryCraftRecipe packet = new TryCraftRecipe(container.tile.getPos(), container.tile.dimension, slot.getRecipe());
				Networking.CHANNEL.sendToServer(packet);
			}
		}

		super.handleMouseClick(slotIn, slotId, mouseButton, type);
	}

	@Override
	public void drawSlot (Slot slot) {
		ItemStack stack = slot.getStack();
		if (!stack.isEmpty() && !(slot instanceof SlotIRecipe)) {
			if (tracked != null && !tracked.isEmpty() && ManifestTrackingUtils.matches(stack, tracked)) {
				GlStateManager.disableDepth();
				long worldTime = this.mc.player.world.getWorldTime();
				Color c = ColorUtils.getColorFromTime(worldTime);
				GuiContainer.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, c.toInteger());
				GlStateManager.enableDepth();
			}
		}

		if (slot instanceof SlotIRecipe && isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseOverX, mouseOverY)) {
			if (!slot.getHasStack()) {
				if (stack.isEmpty()) {
					int i = slot.xPos;
					int j = slot.yPos;
					IRecipe recipe = KeyboardUtil.isShiftDown() ? container.getLastCraftedRecipe() : container.getCurrentRecipe();
					if (recipe != null) {
						ItemStack result = recipe.getRecipeOutput();
						drawRect(i, j, i + 16, j + 16, -2130706433);
						GlStateManager.enableDepth();
						this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, result, i, j);
						this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, result, i, j, null);
					}
				}
			} else {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					return;
				} else {
					if (!container.tile.canCraftRecipe(this.mc.player, ((SlotIRecipe) slot).getRecipe())) {
						GlStateManager.disableDepth();
						Color c = new ColorUtils.Color(189 / 255f, 28 / 255f, 28 / 255f, 1f);
						GuiContainer.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, c.toInteger());
						GlStateManager.enableDepth();
					}
				}
			}
		}

		super.drawSlot(slot);
	}
}
