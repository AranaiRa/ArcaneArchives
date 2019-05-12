package com.aranaira.arcanearchives.integration.jei.quartz;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import gigaherz.common.ItemRegistered;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Arrays;

public class QuartzWrapper implements IRecipeWrapper
{
	private FakeQuartzRecipe recipe;
	public QuartzWrapper (FakeQuartzRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(ItemRegistry.RAW_RADIANT_QUARTZ));
	}

	@Override
	public void drawInfo (Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		double minutes = (double) ConfigHandler.ResonatorTickTime / 20.0 / 60.0;
		String info = I18n.format("jei.gui.resonator", minutes);
		int x = -(minecraft.fontRenderer.getStringWidth(info) / 3);

		minecraft.fontRenderer.drawString(info, x, 75, Color.black.getRGB());
	}

	public static class FakeQuartzRecipe {
	}
}
