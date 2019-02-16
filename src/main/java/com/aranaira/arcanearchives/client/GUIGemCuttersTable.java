package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GUIGemCuttersTable extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/gemcutterstable.png");

	private static final int OVERLAY = 0xaa1e3340;

	ContainerGemCuttersTable mCGCT;
	GemCuttersTableRecipe curRecipe = null;
	private InvisibleButton PrevPageButton;
	private InvisibleButton NextPageButton;
	private EntityPlayer player;
	private Map<GemCuttersTableRecipe, Boolean> RECIPE_STATUS = new HashMap<>();
	private int timesChanged;

	public GUIGemCuttersTable(EntityPlayer player, ContainerGemCuttersTable container)
	{
		super(container);
		mCGCT = container;
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

		if(slot instanceof SlotRecipeHandler)
		{
			GemCuttersTableRecipe recipe = ((SlotRecipeHandler) slot).getRecipe();
			if(recipe == null) return;

			boolean wasEnabled = false;

			if(recipe == curRecipe)
			{
				wasEnabled = true;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableColorMaterial();
				this.mc.getTextureManager().bindTexture(GUITextures);
				this.drawTexturedModalRect(slot.xPos - 2, slot.yPos - 2, 206, 0, 20, 20);
			}

			if(!RECIPE_STATUS.get(recipe))
			{
				if(wasEnabled) this.mc.getTextureManager().deleteTexture(GUITextures);
				GlStateManager.disableDepth();
				drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, OVERLAY);
			}
		}
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
		curRecipe = mCGCT.getTile().getRecipe();
		RECIPE_STATUS.clear();
		RECIPE_STATUS.putAll(mCGCT.updateRecipeStatus());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		int nextChanged = this.player.inventory.getTimesChanged();
		if (nextChanged != this.timesChanged) {
			updateRecipeStatus();
			this.timesChanged = nextChanged;
		}
	}

	/*
			if(recipe != null)
		{
			List<String> mRecipeInput = new ArrayList<>();
			if(recipeStatus)
			{
				// Valid
				mRecipeInput.add(TextFormatting.GREEN + output.getDisplayName());
			} else
			{
				// Invalid
				mRecipeInput.add(TextFormatting.RED + output.getDisplayName());
			}

			for(ItemStack item : recipe.getInput())
			{
				mRecipeInput.add(TextFormatting.BOLD + item.getDisplayName() + " : " + item.getCount());
			}

			this.drawHoveringText(mRecipeInput, guiLeft + 206, guiTop);
		}
	 */

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
			mCGCT.getTile().previousPage();
		}
		if(button.id == 1)
		{
			mCGCT.getTile().nextPage();
		}
		super.actionPerformed(button);
	}
}

