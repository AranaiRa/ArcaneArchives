package com.aranaira.arcanearchives.common;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

//Referenced Immersive Engineering
//https://github.com/BluSunrize/ImmersiveEngineering/blob/e3e8cf65dadb2762cb343cc5f31d9bf9a29f2188/src/main/java/blusunrize/immersiveengineering/api/crafting/BlueprintCraftingRecipe.java#L3
public class GemCuttersTableRecipe 
{
	private List<ItemStack> mInput;
	private ItemStack mOutput;
	
	protected static LinkedHashMap<ItemStack, GemCuttersTableRecipe> RecipeList = new LinkedHashMap();
	
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
		//LORE TAG STUFF DOES NOT CURRENTLY WORK : REVISIT IT
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound lore = new NBTTagCompound();
		lore.setString("Lore", "TEST");
		NBTTagCompound lore2 = new NBTTagCompound();
		lore.setString("Lore2", "TEST2");
		NBTTagList Lore = new NBTTagList();
		NBTTagCompound Display = new NBTTagCompound();
		Lore.appendTag(lore);
		Lore.appendTag(lore2);
		Display.setTag("Lore", Lore);
		nbt.setTag("display", Display);
		recipe.mOutput.setTagCompound(nbt);
		RecipeList.put(recipe.mOutput, recipe);
		//recipe.mOutput.getTagCompound().getTagList("Lore", 8);
		

		ValidInputs.addAll(recipe.mInput);
	}
	
	public static GemCuttersTableRecipe GetRecipe(ItemStack item)
	{
		return RecipeList.get(item);
	}
	
	public boolean matchesRecipe(NonNullList<ItemStack> raw)
	{
		for (ItemStack is : mInput)
		{
			boolean hasFailed = true;
			int count = is.getCount();
			NonNullList<ItemStack> query = ItemStackConsolidator.ConsolidateItems(raw);
			for (int i = 0; i < query.size(); i++)
			{
				if (ItemComparison.AreItemsEqual(query.get(i), is))
				{
					if (query.get(i).getCount() >= count)
						hasFailed = false;
				}
			}
			if (hasFailed)
				return false;
		}
		
		return true;
	}
	
	public ItemStack getOutput()
	{
		return mOutput.copy();
	}
	
	public List<ItemStack> getInput()
	{
		return new ArrayList(mInput);
	}
	
	public void consumeInput(NonNullList<ItemStack> query)
	{
		List<ItemStack> temp = new ArrayList();
		for (ItemStack s : mInput)
		{
			temp.add(s.copy());
		}
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
