package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GCTRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	public List<String> TOOLTIP_CACHE = null;

	private IntOpenHashSet ingredientsSet = null;
	private Int2IntOpenHashMap ingredientsMap = null;
	private final List<IngredientStack> ingredients = new ArrayList<>();
	private final ItemStack result;
	private NonNullList<Ingredient> ingredientList = null;

	public GCTRecipe(String name, @Nonnull ItemStack result, Object... recipe)
	{
		this(new ResourceLocation(ArcaneArchives.MODID, name), result, recipe);
	}

	public GCTRecipe(ResourceLocation name, @Nonnull ItemStack result, Object... recipe)
	{
		this.setRegistryName(name);
		this.result = result.copy();
		for(Object stack : recipe)
		{
			if(stack instanceof ItemStack)
			{
				ingredients.add(new IngredientStack((ItemStack) stack));
			} else if (stack instanceof Item) {
				ingredients.add(new IngredientStack((Item) stack));
			} else if(stack instanceof Ingredient)
			{
				ingredients.add(new IngredientStack((Ingredient) stack));
			} else if(stack instanceof String)
			{
				ingredients.add(new IngredientStack((String) stack));
			} else if(stack instanceof IngredientStack) {
				ingredients.add((IngredientStack) stack);
			} else if (stack instanceof Block) {
				ingredients.add(new IngredientStack((Block) stack));
			} else
			{
				ArcaneArchives.logger.warn(String.format("Unknown ingredient type for recipe %s, skipped: %s", name, stack.toString()));
			}
		}
	}

	private IntOpenHashSet getIngredientsSet()
	{
		if(ingredientsSet == null)
		{
			ingredientsMap = new Int2IntOpenHashMap();
			ingredientsSet = new IntOpenHashSet();

			for(int i = 0; i < ingredients.size(); i++)
			{
				IngredientStack stack = ingredients.get(i);
				IntList packs = stack.getValidItemStacksPacked();
				ingredientsSet.addAll(packs);
				for(int pack : packs)
					ingredientsMap.put(pack, i);
			}

		}

		return ingredientsSet;
	}

	private class Counter
	{
		private Int2IntOpenHashMap counter;

		private Counter()
		{
			counter = new Int2IntOpenHashMap();
			for(int i = 0; i < ingredients.size(); i++)
			{
				IngredientStack stack = ingredients.get(i);
				counter.put(i, stack.getCount());
			}
		}

		private void account(int index, int count)
		{
			if(counter.containsKey(index))
			{
				int val = counter.get(index);
				int newVal = Math.max(0, val - count);
				if(newVal == 0)
				{
					counter.remove(index);
				} else
				{
					counter.put(index, newVal);
				}
			}
		}

		private boolean matches()
		{
			return counter.size() == 0;
		}
	}

	public int getIndex()
	{
		return GCTRecipeList.indexOf(this);
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world)
	{
		IntOpenHashSet ingredients = getIngredientsSet();
		Counter counter = new Counter();

		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			if(!itemstack.isEmpty())
			{
				int stack = RecipeItemHelper.pack(itemstack);
				if(ingredients.contains(stack))
				{
					int index = ingredientsMap.get(stack);
					counter.account(index, itemstack.getCount());
				}
			}
		}

		return counter.matches();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return result.copy();
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return ingredients.size() <= width * height;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return result.copy();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return NonNullList.withSize(0, ItemStack.EMPTY); // TODO
	}

	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		if(ingredientList == null)
		{
			ingredientList = NonNullList.from(Ingredient.EMPTY, this.ingredients.stream().map(IngredientStack::getIngredient).toArray(Ingredient[]::new));
		}

		return ingredientList;
	}

	@Override
	public boolean isDynamic()
	{
		return false;
	}

	@Override
	public String getGroup()
	{
		return this.getRegistryName() == null ? "" : this.getRegistryName().toString();
	}
}
