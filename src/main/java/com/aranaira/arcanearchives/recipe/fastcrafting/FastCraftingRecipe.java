package com.aranaira.arcanearchives.recipe.fastcrafting;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.IArcaneArchivesRecipe;
import com.aranaira.arcanearchives.api.RecipeIngredientHandler;
import com.aranaira.arcanearchives.recipe.IngredientStack;
import com.aranaira.arcanearchives.recipe.IngredientsMatcher;
import com.aranaira.arcanearchives.util.ItemUtils;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FastCraftingRecipe implements IArcaneArchivesRecipe {
	private final List<IngredientStack> ingredients = new ArrayList<>();
	private ItemStack result;
	private final IRecipe originalRecipe;
	private final World world;
	private final FakeContainer fakeContainer = new FakeContainer();
	private List<ItemStack> returned = new ArrayList<>();

	public FastCraftingRecipe (IRecipe input, World world) {
		this.originalRecipe = input;
		this.world = world;
		Map<Ingredient, IngredientStack> ingredientMap = new HashMap<>();
		for (Ingredient ingredient : input.getIngredients()) {
			if (ingredient == Ingredient.EMPTY) continue;

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

	private InventoryCrafting createMatrix (@Nonnull IItemHandler inv, boolean simulate) {
		IngredientsMatcher matcher = new IngredientsMatcher(ingredients);
		Int2IntMap matchingSlots = matcher.getMatchingSlots(inv);

		InventoryCrafting matrix = new InventoryCrafting(fakeContainer, 3, 3);

		List<ItemStack> temp = new ArrayList<>();
		for (Entry slot : matchingSlots.int2IntEntrySet()) {
			for (int i = 0; i < slot.getIntValue(); i++) {
				temp.add(inv.extractItem(slot.getIntKey(), 1, simulate));
			}
		}

		int slot = 0;
		outer: for (Ingredient ingredient : originalRecipe.getIngredients()) {
			Iterator<ItemStack> iter = temp.iterator();
			while (iter.hasNext()) {
				ItemStack next = iter.next();
				if (ingredient.apply(next)) {
					iter.remove();
					matrix.setInventorySlotContents(slot, next);
					slot++;
					continue outer;
				}
			}
		}

		if (!temp.isEmpty()) {
			ArcaneArchives.logger.error("wat");
		}

		return matrix;
	}

	@Override
	public boolean matches (@Nonnull IItemHandler inv) {
		IngredientsMatcher matcher = new IngredientsMatcher(ingredients);
		if (!matcher.matches(inv)) return false;

		InventoryCrafting matrix = createMatrix(inv, true);

		return originalRecipe.matches(matrix, world);
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

	private void setResult (ItemStack stack) {
		result = stack;
	}

	public ItemStack getResult () {
		return result;
	}

	public List<ItemStack> getReturned () {
		return returned;
	}

	public void consumeAndHandleInventory (IArcaneArchivesRecipe recipe, IItemHandler inventory, EntityPlayer player, @Nullable TileEntity tile, @Nullable Runnable callback, @Nullable RecipeIngredientHandler handler, @Nullable Runnable finalCallback) {
		InventoryCrafting matrix = createMatrix(inventory, false);

		InventoryCraftResult craftResult = new InventoryCraftResult();
		SlotCrafting slot = new SlotCrafting(player, matrix, craftResult, 0, 0, 0);
		craftResult.setRecipeUsed(originalRecipe);
		craftResult.setInventorySlotContents(0, originalRecipe.getCraftingResult(matrix));
		ItemStack result = slot.onTake(player, craftResult.getStackInSlot(0));
		returned.clear();

		returned.add(result);

		for (int i = 0; i < matrix.getSizeInventory(); i++) {
			ItemStack inSlot = matrix.removeStackFromSlot(i);
			if (!inSlot.isEmpty()) {
				returned.add(inSlot);
			}
		}

		if (finalCallback != null) {
			finalCallback.run();
		}
	}

	public static class FakeContainer extends Container {

		@Override
		public void onCraftMatrixChanged(IInventory inventoryIn) {
		}

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return false;
		}
	}
}
