package com.aranaira.arcanearchives.integration.jei;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;

public class JEIUnderMouse
{
	@Nullable
	public static ItemStack underMouse()
	{
		if(Loader.isModLoaded("jei"))
		{
			Object stack = JEIPlugin.runtime.getIngredientListOverlay().getIngredientUnderMouse();
			if(stack instanceof ItemStack)
			{
				return (ItemStack) stack;
			}

			stack = JEIPlugin.runtime.getRecipesGui().getIngredientUnderMouse();
			if(stack instanceof ItemStack)
			{
				return (ItemStack) stack;
			}
		}

		return null;
	}
}
