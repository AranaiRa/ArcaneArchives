package com.aranaira.arcanearchives.integration.guidebook;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import gigaherz.lirelent.guidebook.guidebook.drawing.VisualElement;
import gigaherz.lirelent.guidebook.guidebook.elements.ElementImage;
import gigaherz.lirelent.guidebook.guidebook.elements.ElementStack;
import gigaherz.lirelent.guidebook.guidebook.recipe.RecipeProvider;
import gigaherz.lirelent.guidebook.guidebook.util.Size;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class GCTRecipeProvider extends RecipeProvider {
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ArcaneArchives.MODID, "textures/gui/jei/gem_cutters_table.png");

	@Nullable
	@Override
	public ProvidedComponents provideRecipeComponents (@Nonnull ItemStack targetOutput, int recipeIndex) {
		return null;
	}

	@Nullable
	@Override
	public ProvidedComponents provideRecipeComponents (@Nonnull ResourceLocation recipeKey) {
		GCTRecipe recipe = GCTRecipeList.getRecipe(recipeKey);
		if (recipe == null) return null;

		ArrayList<ElementStack> stacks = new ArrayList<>();
		VisualElement additionalRenderer = new VisualElement(new Size(), 0, 0, 0) {
		};

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			IngredientStack stack = recipe.getIngredients().get(i);
			ElementStack inputSlot = new ElementStack(false, false);
			for (ItemStack s : stack.getMatchingStacks()) {
				s.setCount(stack.getCount());
				inputSlot.stacks.add(s.copy());
			}

			inputSlot.x = (i % 2 == 0) ? 0 : 18;
			inputSlot.y = (i / 2) * 18;
			stacks.add(inputSlot);
		}

		ElementStack outputSlot = new ElementStack(false, false);
		outputSlot.stacks.add(recipe.getRecipeOutput().copy());
		outputSlot.x = 98;
		outputSlot.y = 27;
		stacks.add(outputSlot);

		ElementImage background = new ElementImage(false, false);
		background.textureLocation = BACKGROUND;
		background.x = 0;
		background.y = 0;
		background.tx = 0;
		background.ty = 0;
		background.w = 120;
		background.h = 72;

		return new ProvidedComponents(72, stacks.toArray(new ElementStack[]{}), background, additionalRenderer);
	}
}
