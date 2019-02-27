package com.aranaira.arcanearchives.registry.crafting.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class GCTRecipe extends ShapelessOreRecipe implements IRecipe
{
	public GCTRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe)
	{
		super(group, result, recipe);
	}

	public static GCTRecipe buildAndAdd(String name, ItemStack result, Object... recipe)
	{
		ResourceLocation group = new ResourceLocation(ArcaneArchives.MODID, name);
		GCTRecipe gctRecipe = new GCTRecipe(group, result, recipe);

		GCTRecipeList.addRecipe(gctRecipe);

		return gctRecipe;
	}
}
