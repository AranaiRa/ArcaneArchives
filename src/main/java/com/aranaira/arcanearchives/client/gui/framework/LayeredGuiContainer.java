package com.aranaira.arcanearchives.client.gui.framework;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
public abstract class LayeredGuiContainer extends GuiContainer {
	/**
	 * at what Z level to render {@link #drawForegroundContents(int, int)} relative to {@link @START_Z}
	 */
	public static float DELTA_FOREGROUND_Z = 300f;
	/**
	 * at what Z level to render {@link #drawBackgroundContents(int, int)} relative to {@link @START_Z}
	 */
	public static float DELTA_BACKGROUND_Z = -50f;
	/**
	 * at what Z level relative to the "fog" that is drawn in front of the world behind the GUI to start drawing
	 */
	public static float START_Z = 100f;
	/**
	 * at what Z level to render {@link #drawTopLevelElements(int, int)} relative to z level
	 * when {@link #drawScreen(int, int, float)} is called which is where the "fog" in front of the view
	 * of the world is drawn via {@link GuiScreen#drawDefaultBackground()}
	 */
	public static float TOP_Z = 400f;

	/**
	 * @param inventorySlotsIn a {@link Container} that contains the {@link net.minecraft.inventory.Slot}s
	 *                         that this GUI needs to render
	 */
	public LayeredGuiContainer (Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	/**
	 * Stuff drawn here will be drawn behind the {@link net.minecraft.inventory.Slot}s
	 *
	 * @param mouseX current mouse X position
	 * @param mouseY current mouse Y position
	 */
	protected void drawBackgroundContents (int mouseX, int mouseY) {}

	/**
	 * Stuff drawn here will be drawn in front of the {@link net.minecraft.inventory.Slot}s
	 *
	 * @param mouseX current mouse X position
	 * @param mouseY current mouse Y position
	 */
	protected void drawForegroundContents (int mouseX, int mouseY) {}

	/**
	 * Stuff drawn here will be drawn in front of everything else, at the Top of the world
	 *
	 * @param mouseX current mouse X position
	 * @param mouseY current mouse Y position
	 */
	protected void drawTopLevelElements (int mouseX, int mouseY) {}

	/**
	 * Use this method to add buttons to this {@link LayeredGuiContainer} so that they will end up
	 * on the correct layer
	 *
	 * @param buttonIn {@link GuiButton} to add
	 */
	@Override
	protected <T extends GuiButton> T addButton(T buttonIn) {
		super.addButton(new LayeredButton(buttonIn));
		return buttonIn;
	}

	// ================ start of internal gubbins that make this class do its job =============================
	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0f, DELTA_BACKGROUND_Z);

		drawBackgroundContents(mouseX, mouseY);

		// clean up GL state
		GlStateManager.popMatrix();
	}

	@Override
	protected void drawGuiContainerForegroundLayer (int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		// we do this so that the slots will "slide" behind this foreground picture as they move out of view
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0f, DELTA_FOREGROUND_Z);

		drawForegroundContents(mouseX, mouseY);

		// clean up GL state
		GlStateManager.popMatrix();
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		// this draws the "world" behind the gui
		this.drawDefaultBackground();

		// translate relative to the "fog" that is drawn in front of the world in the background in
		// the drawDefaultBackground call above
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, START_Z);

		// first calls super.super.drawScreen which will draw buttonList and labelList
		// then draws the texture behind the slots via #drawGuiContainerBackgroundLayer
		// then draws all the slots in this.inventorySlots
		// then draws the texture in front of the slots via #drawGuiContainerForegroundLayer
		super.drawScreen(mouseX, mouseY, partialTicks);

		// translate for drawTopLevelElements
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0f, 0.0f, TOP_Z);

		drawTopLevelElements(mouseX, mouseY);

		// clean up GL state
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
