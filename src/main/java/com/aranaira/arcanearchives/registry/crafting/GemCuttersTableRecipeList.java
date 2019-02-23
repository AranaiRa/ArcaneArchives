package com.aranaira.arcanearchives.registry.crafting;

import com.aranaira.arcanearchives.util.AACollectors;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GemCuttersTableRecipeList
{
	//For use of shift clicking items in.
	private static Set<ItemStack> VALID_INPUTS = new HashSet<>();
	private static List<GemCuttersTableRecipe> RECIPE_LIST = new ArrayList<>();
	// private static LinkedHashMap<ItemStack, GemCuttersTableRecipe> RECIPE_LIST = new LinkedHashMap<>();

	public static Set<ItemStack> getValidInputs()
	{
		return VALID_INPUTS;
	}

	public static List<GemCuttersTableRecipe> getRecipeList()
	{
		return ImmutableList.copyOf(RECIPE_LIST);
	}

	// I'm not sure if using order is important
	private static List<ItemStack> getKeys()
	{
		return RECIPE_LIST.stream().map(GemCuttersTableRecipe::getOutput).collect(AACollectors.toImmutableList());
	}

	public static ItemStack getOutputByIndex(int index)
	{
		if(index < 0 || index >= getSize()) return ItemStack.EMPTY;

		return getKeys().get(index);
	}

	public static int getSize()
	{
		return RECIPE_LIST.size();
	}

	public static void addAll(GemCuttersTableRecipe... recipes)
	{
		for(GemCuttersTableRecipe recipe : recipes)
		{
			addRecipe(recipe);
		}
	}

	public static void addRecipe(GemCuttersTableRecipe recipe)
	{
		//LORE TAG STUFF DOES NOT CURRENTLY WORK : REVISIT IT
		/*NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound lore = new NBTTagCompound();
		lore.setString("Lore", "TEST");
		NBTTagCompound lore2 = new NBTTagCompound();
		lore.setString("Lore2", "TEST2");
		NBTTagList Lore = new NBTTagList();
		NBTTagCompound Display = new NBTTagCompound();
		Lore.appendTag(lore);
		Lore.appendTag(lore2);
		Display.setTag("Lore", Lore);
		nbt.setTag("display", Display);*/
		//recipe.getOutput().setTagCompound(nbt);
		//recipe.mOutput.getTagCompound().getTagList("Lore", 8);

		RECIPE_LIST.add(recipe);
		VALID_INPUTS.addAll(recipe.getInput());
	}

	public static GemCuttersTableRecipe getRecipe(ItemStack item)
	{
		return RECIPE_LIST.stream().filter((recipe) -> ItemComparison.areStacksEqualIgnoreSize(recipe.getOutput(), item)).findFirst().orElse(null);
	}
}
