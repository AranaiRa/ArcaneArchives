package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGemCuttersTable extends GuiContainer {
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("arcanearchives:textures/gui/gemcutterstable.png");

	private static final int OVERLAY = 0xaa1e3340;

	private final ContainerGemCuttersTable container;
	private final EntityPlayer player;
	private final Object2BooleanMap<GCTRecipe> recipeStatus = new Object2BooleanOpenHashMap<>();
	private final GemCuttersTableTileEntity tile;
	private InvisibleButton prevPageButton;
	private InvisibleButton nextPageButton;
	private int timesChanged;

	public GUIGemCuttersTable (EntityPlayer player, ContainerGemCuttersTable container) {
		super(container);
		this.container = container;
		container.setUpdateRecipeGUI(this::updateRecipeStatus);
		this.tile = container.getTile();
		this.xSize = 206;
		this.ySize = 254;
		this.player = player;
		updateRecipeStatus();
		this.timesChanged = this.player.inventory.getTimesChanged();
	}

	public void updateRecipeStatus () {
		recipeStatus.clear();
		recipeStatus.putAll(container.updateRecipeStatus());
	}

	@Override
	public void initGui () {
		super.initGui();

		this.buttonList.clear();

		this.prevPageButton = new InvisibleButton(0, guiLeft + 26, guiTop + 69, 10, 18, "");
		this.nextPageButton = new InvisibleButton(1, guiLeft + 170, guiTop + 69, 10, 18, "");

		this.buttonList.add(prevPageButton);
		this.buttonList.add(nextPageButton);

		updateRecipeStatus();
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		int nextChanged = this.player.inventory.getTimesChanged();
		if (nextChanged != this.timesChanged) {
			updateRecipeStatus();
			this.timesChanged = nextChanged;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUI_TEXTURES);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 206, 256, 256, 256);
	}

	@Override
	public void drawSlot (Slot slot) {
		super.drawSlot(slot);

		boolean wasEnabled = false;

		if (slot instanceof SlotRecipeHandler) {
			GCTRecipe recipe = ((SlotRecipeHandler) slot).getRecipe();
			if (recipe == null) {
				return;
			}

			if (recipe == tile.getCurrentRecipe()) {
				wasEnabled = true;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableColorMaterial();
				this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
				this.drawTexturedModalRect(slot.xPos - 2, slot.yPos - 2, 206, 0, 20, 20);
			}

			if (!recipeStatus.getBoolean(recipe)) {
				dimSlot(slot, wasEnabled);
			}
		}
	}

	private void dimSlot (Slot slot, boolean wasEnabled) {
		if (wasEnabled) {
			this.mc.getTextureManager().deleteTexture(GUI_TEXTURES);
		}
		GlStateManager.disableDepth();
		drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OVERLAY);
	}

	@Override
	public boolean doesGuiPauseGame () {
		return false;
	}

	@Override
	protected void renderToolTip (ItemStack stack, int x, int y) {
		Slot slot = this.getSlotUnderMouse();

		if (slot instanceof SlotRecipeHandler) {
			FontRenderer font = stack.getItem().getFontRenderer(stack);
			List<String> tooltip = new ArrayList<>();
			GCTRecipe recipe = ((SlotRecipeHandler) slot).getRecipe();
			if (recipe != null) {
				if (recipeStatus.getBoolean(recipe)) {// Valid
					tooltip.add(TextFormatting.GREEN + stack.getDisplayName());
				} else {
					// Invalid
					tooltip.add(TextFormatting.RED + stack.getDisplayName());
				}

				if (recipe.TOOLTIP_CACHE != null) {
					tooltip.addAll(recipe.TOOLTIP_CACHE);
				} else {
					Map<Integer, ItemStack> ingredients = new HashMap<>();
					for (IngredientStack ing : recipe.getIngredients()) {
						ItemStack[] stacks = ing.getMatchingStacks();
						assert stacks.length != 0; // TODO?
						ItemStack item = stacks[0];
						item.setCount(ing.getCount());
						int packed = RecipeItemHelper.pack(item);
						if (ingredients.containsKey(packed)) {
							ingredients.get(packed).grow(ing.getCount());
						} else {
							ingredients.put(packed, item);
						}
					}

					List<String> cache = new ArrayList<>();

					for (ItemStack item : ingredients.values()) {
						cache.add(TextFormatting.BOLD + item.getDisplayName() + " : " + item.getCount());
					}

					recipe.TOOLTIP_CACHE = cache;
					tooltip.addAll(cache);
				}
			}

			this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));
		} else {
			super.renderToolTip(stack, x, y);
		}
	}

	@Override
	protected void actionPerformed (GuiButton button) throws IOException {
		if (button.id == 0) {
			tile.previousPage();
		}
		if (button.id == 1) {
			tile.nextPage();
		}
		super.actionPerformed(button);
	}
}

