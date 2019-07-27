package com.aranaira.arcanearchives.recipe.fastcrafting;

import com.aranaira.arcanearchives.api.IArcaneArchivesRecipe;
import com.aranaira.arcanearchives.api.RecipeIngredientHandler;
import com.aranaira.arcanearchives.recipe.IngredientStack;
import com.aranaira.arcanearchives.recipe.IngredientsMatcher;
import com.aranaira.arcanearchives.util.ItemUtils;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCraftingRecipe implements IArcaneArchivesRecipe {
	private final List<IngredientStack> ingredients = new ArrayList<>();
	private final ItemStack result;

	public FastCraftingRecipe (IRecipe input) {
		this.result = input.getRecipeOutput();
		Map<Ingredient, IngredientStack> ingredientMap = new HashMap<>();
		for (Ingredient ingredient : input.getIngredients()) {
			IngredientStack match = null;
			for (Map.Entry<Ingredient, IngredientStack> entry : ingredientMap.entrySet()) {
				if (ItemUtils.ingredientsMatch(entry.getKey(), ingredient)) {
					match = entry.getValue();
					break;
				}
			}
			if (match == null) {
				ingredientMap.put(ingredient, new IngredientStack(ingredient, 1));
			} else {
				match.grow();
			}
		}
		ingredients.addAll(ingredientMap.values());
	}

	@Override
	public boolean matches (@Nonnull IItemHandler inv) {
		return new IngredientsMatcher(ingredients).matches(inv);
	}

	// I don't know what this does
	@Override
	public boolean craftable (EntityPlayer player, IItemHandler inventory) {
		return true;
	}

	@Override
	public Int2IntMap getMatchingSlots (@Nonnull IItemHandler inv) {
		return new IngredientsMatcher(ingredients).getMatchingSlots(inv);
	}

	@Override
	public ItemStack getRecipeOutput () {
		return result.copy();
	}

	@Override
	public List<IngredientStack> getIngredients () {
		return ingredients;
	}

	public void consumeAndHandleInventory (IArcaneArchivesRecipe recipe, IItemHandler inventory, EntityPlayer player, @Nullable TileEntity tile, @Nullable Runnable callback, @Nullable RecipeIngredientHandler handler, @Nullable Runnable finalCallback) {
		for (Entry entry : recipe.getMatchingSlots(inventory).int2IntEntrySet()) {
			ItemStack result = inventory.extractItem(entry.getIntKey(), entry.getIntValue(), false);
			if (!player.world.isRemote) {
				if (handler != null && handler.test(player.world, player, tile, result)) {
					if (callback != null) {
						callback.run();
					}
				}
			}
		}
		if (finalCallback != null) {
			finalCallback.run();
		}
	}
}
