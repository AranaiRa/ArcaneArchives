package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import com.aranaira.arcanearchives.util.types.IngredientsMatcher;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GCTRecipe {
	private final List<IngredientStack> ingredients = new ArrayList<>();
	private final ItemStack result;
	private final String name;
	public List<String> TOOLTIP_CACHE = null;

	public GCTRecipe (String name, @Nonnull ItemStack result, Object... recipe) {
		this.result = result.copy();
		this.name = name;
		for (Object stack : recipe) {
			if (stack instanceof ItemStack) {
				ingredients.add(new IngredientStack((ItemStack) stack));
			} else if (stack instanceof Item) {
				ingredients.add(new IngredientStack((Item) stack));
			} else if (stack instanceof Ingredient) {
				ingredients.add(new IngredientStack((Ingredient) stack));
			} else if (stack instanceof String) {
				ingredients.add(new IngredientStack((String) stack));
			} else if (stack instanceof IngredientStack) {
				ingredients.add((IngredientStack) stack);
			} else if (stack instanceof Block) {
				ingredients.add(new IngredientStack((Block) stack));
			} else {
				ArcaneArchives.logger.warn(String.format("Unknown ingredient type for recipe, skipped: %s", stack.toString()));
			}
		}
	}

	public int getIndex () {
		return GCTRecipeList.indexOf(this);
	}

	public String getName () {
		return name;
	}

	public boolean matches (@Nonnull IItemHandler inv) {
		return new IngredientsMatcher(ingredients).matches(inv);
	}

	public Int2IntMap getMatchingSlots (@Nonnull IItemHandler inv) {
		return new IngredientsMatcher(ingredients).getMatchingSlots(inv);
	}

	public ItemStack getRecipeOutput () {
		return result.copy();
	}

	public List<IngredientStack> getIngredients () {
		return ingredients;
	}
}
