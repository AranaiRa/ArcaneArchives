package com.aranaira.arcanearchives.registry.crafting;

import com.aranaira.arcanearchives.util.AACollectors;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

import java.util.*;

public class GemCuttersTableRecipeList
{
	//For use of shift clicking items in.
	private static Set<ItemStack> ValidInputs = new HashSet<>();
	private static List<GemCuttersTableRecipe> RecipeList = new ArrayList<>();
	// private static LinkedHashMap<ItemStack, GemCuttersTableRecipe> RecipeList = new LinkedHashMap<>();

	public static Set<ItemStack> getValidInputs () {
		return ValidInputs;
	}

	public static List<GemCuttersTableRecipe> getRecipeList()
	{
		return ImmutableList.copyOf(RecipeList);
	}

	// I'm not sure if using order is important
	public static List<ItemStack> getKeys()
	{
		return RecipeList.stream().map(GemCuttersTableRecipe::getOutput).collect(AACollectors.toImmutableList());
	}

	public static ItemStack getOutputByIndex (int index) {
		if (index < 0 || index >= getSize()) return ItemStack.EMPTY;

		return getKeys().get(index);
	}

	public static int getSize()
	{
		return RecipeList.size();
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

		RecipeList.add(recipe);
		ValidInputs.addAll(recipe.getInput());
	}

	public static GemCuttersTableRecipe GetRecipe(ItemStack item)
	{
		return RecipeList.stream().filter((recipe) -> ItemComparison.AreItemsEqual(recipe.getOutput(), item)).findFirst().orElse(null);
	}
}
