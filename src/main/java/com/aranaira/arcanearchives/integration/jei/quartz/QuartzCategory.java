/*package com.aranaira.arcanearchives.integration.jei.quartz;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.integration.jei.JEIPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class QuartzCategory implements IRecipeCategory<QuartzWrapper> {
	private final IDrawable background;

	public QuartzCategory (IGuiHelper helper) {
		this.background = helper.createDrawable(new ResourceLocation(ArcaneArchives.MODID, "textures/gui/jei/radiant_resonator.png"), 0, 0, 22, 62);
	}

	@Override
	public String getUid () {
		return JEIPlugin.RADIANT_RESONATOR;
	}

	@Override
	public String getTitle () {
		return I18n.format("tile.radiant_resonator.name");
	}

	@Override
	public String getModName () {
		return ArcaneArchives.NAME;
	}

	@Override
	public IDrawable getBackground () {
		return background;
	}

	@Override
	public void setRecipe (IRecipeLayout recipeLayout, QuartzWrapper recipeWrapper, IIngredients ingredients) {
	}
}*/
