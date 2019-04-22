package com.aranaira.arcanearchives.integration.jei.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.integration.jei.JEIPlugin;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GCTCategory implements IRecipeCategory<GCTWrapper>
{
	private final IDrawable background;

	public GCTCategory(IGuiHelper helper)
	{
		this.background = helper.createDrawable(new ResourceLocation(ArcaneArchives.MODID, "textures/gui/jei/gem_cutters_table.png"), 0, 0, 120, 72);
	}

	@Override
	public String getUid()
	{
		return JEIPlugin.GEM_CUTTERS_TABLE;
	}

	@Override
	public String getTitle()
	{
		return I18n.format("tile.gemcutters_table.name");
	}

	@Override
	public String getModName()
	{
		return ArcaneArchives.NAME;
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GCTWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup group = recipeLayout.getItemStacks();
		if(recipeWrapper.recipe != null)
		{
			GCTRecipe recipe = recipeWrapper.recipe;
			for(int i = 0; i < recipe.getIngredients().size(); i++)
			{
				group.init(i, true, i % 2 == 0 ? 0 : 18, (i / 2) * 18);
				group.set(i, Arrays.asList(recipe.getIngredients().get(i).getMatchingStacks()));
			}
			group.init(8, false, 98, 27);
			group.set(8, recipe.getRecipeOutput());
		}
	}
}
