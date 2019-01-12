package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.aranaira.arcanearchives.util.ItemComparison;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

//Referenced Immersive Engineering
//https://github.com/BluSunrize/ImmersiveEngineering/blob/e3e8cf65dadb2762cb343cc5f31d9bf9a29f2188/src/main/java/blusunrize/immersiveengineering/api/crafting/BlueprintCraftingRecipe.java#L3
public class GemCuttersTableRecipe 
{
	protected List<ItemStack> mInput;
	protected ItemStack mOutput;
	
	protected static Dictionary<ItemStack, GemCuttersTableRecipe> RecipeList = new Hashtable();
	
	//protected static List<GemCuttersTableRecipe> RecipeList = new ArrayList();
	
	//For use of shift clicking items in.
	public static Set<ItemStack> ValidInputs = new HashSet();
	
	public GemCuttersTableRecipe(List<ItemStack> input, ItemStack output)
	{
		mInput = input;
		mOutput = output;
	}
	
	public static void addRecipe(GemCuttersTableRecipe recipe)
	{
		RecipeList.put(recipe.mOutput, recipe);
		//for (ItemStack is : recipe.mInput)
		//{
		//	ValidInputs.add
		//}
		ValidInputs.addAll(recipe.mInput);
	}
	
	public static GemCuttersTableRecipe GetRecipe(ItemStack item)
	{
		return RecipeList.get(item);
	}
	
	public boolean matchesRecipe(NonNullList<ItemStack> query)
	{
		for (ItemStack is : mInput)
		{
			boolean hasFailed = true;
			int count = is.getCount();
			for (int i = 0; i < query.size(); i++)
			{
				if (ItemComparison.AreItemsEqual(query.get(i), is))
				{
					//GONNA HAVE TO REWORK THIS
					if (query.get(i).getCount() >= count)
						hasFailed = false;
				}
			}
			if (hasFailed)
				return false;
		}
		
		return true;
	}
	
	public void consumeInput(NonNullList<ItemStack> query)
	{
		List<ItemStack> temp = new ArrayList();
		temp.addAll(mInput);
		for (ItemStack i : query)
		{
			for (ItemStack is : temp)
			{
				if (is.isEmpty())
					continue;
				if (ItemComparison.AreItemsEqual(i, is))
				{
					if (i.getCount() >= is.getCount())
					{
						i.shrink(is.getCount());
						is.shrink(is.getCount());
					}
					else
					{
						int x = i.getCount();
						i.shrink(x);
						is.shrink(x);
					}
				}
			}
		}
	}
}
