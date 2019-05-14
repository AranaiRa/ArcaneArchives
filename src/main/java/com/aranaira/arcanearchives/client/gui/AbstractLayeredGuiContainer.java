package com.aranaira.arcanearchives.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * There are "fun" things about the way {@link net.minecraft.client.gui.inventory.GuiContainer}
 * and {@link net.minecraft.client.renderer.RenderItem} do things where they render items with
 * a crazy high z level GL transform matrix. So we have to one up the crazy in order to have
 * a real foreground that is on top of rendered {@link net.minecraft.item.ItemStack}s. This
 * provides a utility class around allowing truly layered GUIs, hiding away the dirty hacks
 * that have to be done to make this work right.
 * <br><br>
 * The end effect is that what shows up on screen from back to front is
 * <ol>
 * <li> {@link #drawBackgroundContents(int, int)} </li>
 * <li> {@link #inventorySlots} which are populated via {@link Container#addSlotToContainer(Slot)} </li>
 * <li> {@link #drawForegroundContents(int, int)} </li>
 * <li> {@link #drawTopLevelElements(int, int)} </li>
 * </ol>
 */
public abstract class AbstractLayeredGuiContainer extends GuiContainer {
	/**
	 * @param inventorySlotsIn a {@link Container} that contains the {@link net.minecraft.inventory.Slot}s
	 *                         that this GUI needs to render
	 */
	public AbstractLayeredGuiContainer (Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	/**
	 * Stuff drawn here will be drawn behind the {@link net.minecraft.inventory.Slot}s
	 *
	 * @param mouseX current mouse X position
	 * @param mouseY current mouse Y position
	 */
	protected void drawBackgroundContents (int mouseX, int mouseY) {
	}

	/**
	 * Stuff drawn here will be drawn in front of the {@link net.minecraft.inventory.Slot}s
	 *
	 * @param mouseX current mouse X position
	 * @param mouseY current mouse Y position
	 */
	protected void drawForegroundContents (int mouseX, int mouseY) {
	}

	/**
	 * Stuff drawn here will be drawn in front of everything else, at the Top of the world
	 *
	 * @param mouseX current mouse X position
	 * @param mouseY current mouse Y position
	 */
	protected void drawTopLevelElements (int mouseX, int mouseY) {
	}

	// ================ start of internal gubbins that make this class do its job =============================
	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		drawBackgroundContents(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer (int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		// we do this so that the slots will "slide" behind this foreground picture as they move out of view
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0f, 500f);

		drawForegroundContents(mouseX, mouseY);

		GlStateManager.popMatrix();
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		// this draws the "world" behind the gui
		this.drawDefaultBackground();
		// this draws the texture behind the slots via #drawGuiContainerBackgroundLayer
		// then draws all the slots in this.inventorySlots
		// then draws the texture in front of the slots via #drawGuiContainerForegroundLayer
		super.drawScreen(mouseX, mouseY, partialTicks);

		// we do this so that the all the final GUI elements are on top of everything else
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, 510.0f);
		zLevel = 510f;

		drawTopLevelElements(mouseX, mouseY);

		// clean up GL state
		zLevel = 0f;
		GlStateManager.popMatrix();
	}
}
