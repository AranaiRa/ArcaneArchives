package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGemCuttersTable extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/gemcutterstable.png");

	private static final int OVERLAY = 0xaa1e3340;

	ContainerGemCuttersTable container;
	GCTRecipe curRecipe = null;
	private InvisibleButton PrevPageButton;
	private InvisibleButton NextPageButton;
	private EntityPlayer player;
	private GemCuttersTableTileEntity tile;
	private Map<GCTRecipe, Boolean> RECIPE_STATUS = new HashMap<>();
	private int timesChanged;

	public GUIGemCuttersTable(EntityPlayer player, ContainerGemCuttersTable container)
	{
		super(container);
		this.container = container;
		this.tile = container.getTile();
		container.setUpdateRecipeGUI(this::updateRecipeStatus);
		this.xSize = 206;
		this.ySize = 254;
		this.player = player;
		updateRecipeStatus();
		this.timesChanged = this.player.inventory.getTimesChanged();
	}

	@Override
	public void drawSlot(Slot slot)
	{
		super.drawSlot(slot);

		boolean wasEnabled = false;

		if(slot instanceof SlotRecipeHandler)
		{
			GCTRecipe recipe = ((SlotRecipeHandler) slot).getRecipe();
			if(recipe == null) return;

			if(recipe == curRecipe)
			{
				wasEnabled = true;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableColorMaterial();
				this.mc.getTextureManager().bindTexture(GUITextures);
				this.drawTexturedModalRect(slot.xPos - 2, slot.yPos - 2, 206, 0, 20, 20);
			}

			if(!RECIPE_STATUS.get(recipe))
			{
				dimSlot(slot, wasEnabled);
			}
		}
	}

	private void dimSlot(Slot slot, boolean wasEnabled)
	{
		if(wasEnabled) this.mc.getTextureManager().deleteTexture(GUITextures);
		GlStateManager.disableDepth();
		drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OVERLAY);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.clear();

		this.PrevPageButton = new InvisibleButton(0, guiLeft + 26, guiTop + 69, 10, 18, "");
		this.NextPageButton = new InvisibleButton(1, guiLeft + 170, guiTop + 69, 10, 18, "");

		PrevPageButton.visible = false;
		NextPageButton.visible = false;

		this.buttonList.add(PrevPageButton);
		this.buttonList.add(NextPageButton);

		updateRecipeStatus();
	}

	public void updateRecipeStatus()
	{
		curRecipe = container.getTile().getRecipe();
		RECIPE_STATUS.clear();
		RECIPE_STATUS.putAll(container.updateRecipeStatus());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		int nextChanged = this.player.inventory.getTimesChanged();
		if(nextChanged != this.timesChanged)
		{
			updateRecipeStatus();
			this.timesChanged = nextChanged;
		}
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y)
	{
		Slot slot = this.getSlotUnderMouse();

		if(slot instanceof SlotRecipeHandler)
		{
			FontRenderer font = stack.getItem().getFontRenderer(stack);
			List<String> tooltip = new ArrayList<>();
			GCTRecipe recipe = ((SlotRecipeHandler) slot).getRecipe();
			if(recipe != null)
			{
				if(RECIPE_STATUS.getOrDefault(recipe, false))
				{// Valid
					tooltip.add(TextFormatting.GREEN + stack.getDisplayName());
				} else
				{
					// Invalid
					tooltip.add(TextFormatting.RED + stack.getDisplayName());
				}

				if(recipe.TOOLTIP_CACHE != null)
				{
					tooltip.addAll(recipe.TOOLTIP_CACHE);
				} else
				{
					Map<Integer, ItemStack> ingredients = new HashMap<>();
					for(Ingredient ing : recipe.getIngredients())
					{
						ItemStack[] stacks = ing.getMatchingStacks();
						assert stacks.length != 0; // TODO?
						ItemStack item = stacks[0];
						int packed = RecipeItemHelper.pack(item);
						if(ingredients.containsKey(packed))
						{
							ingredients.get(packed).grow(1);
						} else
						{
							ingredients.put(packed, item);
						}
					}

					List<String> cache = new ArrayList<>();

					for(ItemStack item : ingredients.values())
					{
						cache.add(TextFormatting.BOLD + item.getDisplayName() + " : " + item.getCount());
					}

					recipe.TOOLTIP_CACHE = cache;
					tooltip.addAll(cache);
				}
			}

			this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));
		} else
		{
			super.renderToolTip(stack, x, y);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 206, 256, 256, 256);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id == 0)
		{
			container.getTile().previousPage();
		}
		if(button.id == 1)
		{
			container.getTile().nextPage();
		}
		super.actionPerformed(button);
	}
}

