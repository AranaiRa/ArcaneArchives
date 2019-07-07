package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.client.render.RenderHelper.Color;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.util.ColorHelper;
import com.aranaira.arcanearchives.util.ManifestTracking;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GUIRadiantCraftingTable extends GuiContainer {

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/radiantcraftingtable.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/radiantcraftingtable.png");

	private List<Ingredient> tracked;

	public GUIRadiantCraftingTable (EntityPlayer player, ContainerRadiantCraftingTable container) {
		super(container);
		this.xSize = 206;
		this.ySize = 203;

		tracked = ManifestTracking.get(player.dimension, container.pos);
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
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
	public void drawSlot (Slot slot) {
		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) {
			if (tracked != null && !tracked.isEmpty() && ManifestTracking.matches(stack, tracked)) {
				GlStateManager.disableDepth();
				long worldTime = this.mc.player.world.getWorldTime();
				Color c = ColorHelper.getColorFromTime(worldTime);
				GuiContainer.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, c.toInteger());
				GlStateManager.enableDepth();
			}
		}

		super.drawSlot(slot);
	}
}
