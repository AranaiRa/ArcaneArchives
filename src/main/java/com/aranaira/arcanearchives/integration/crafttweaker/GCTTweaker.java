package com.aranaira.arcanearchives.integration.crafttweaker;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.IGCTRecipe;
import com.aranaira.arcanearchives.recipe.IngredientStack;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.util.zen.ZenDocAppend;
import com.aranaira.arcanearchives.util.zen.ZenDocArg;
import com.aranaira.arcanearchives.util.zen.ZenDocClass;
import com.aranaira.arcanearchives.util.zen.ZenDocMethod;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenDocClass("mods.arcanearchives.GCT")
@ZenDocAppend({"docs/include/gct.example.md"})
@ZenRegister
@ZenClass("mods." + ArcaneArchives.MODID + ".GCT")
public class GCTTweaker {

	@ZenDocMethod(order = 1, args = {@ZenDocArg(arg = "name", info = "the recipe name"), @ZenDocArg(arg = "output", info = "the output as an itemstack"), @ZenDocArg(arg = "inputs", info = "the inputs as an array of ingredients")})
	@ZenMethod
	public static void addRecipe (String name, IItemStack output, IIngredient[] inputs) {
		CraftTweaker.LATE_ACTIONS.add(new Add(name, InputHelper.toStack(output), inputs, false));
	}

	@ZenDocMethod(order = 2, args = {@ZenDocArg(arg = "output", info = "the output itemstack to be removed (quantity must match)")})
	@ZenMethod
	public static void removeRecipe (IItemStack output) {
		CraftTweaker.LATE_ACTIONS.add(new Remove(InputHelper.toStack(output)));
	}

	@ZenDocMethod(order = 3, args = {@ZenDocArg(arg = "name", info = "the recipe name (must already exist)"), @ZenDocArg(arg = "output", info = "the output as an itemstack"), @ZenDocArg(arg = "inputs", info = "the inputs as an array of ingredients")})
	@ZenMethod
	public static void replaceRecipe (String name, IItemStack output, IIngredient[] inputs) {
		CraftTweaker.LATE_ACTIONS.add(new Add(name, InputHelper.toStack(output), inputs, true));
	}

	private static class Remove extends BaseAction {
		private ItemStack output;

		private Remove (ItemStack stack) {
			super("GCT Recipe removal");
			this.output = stack;
		}

		@Override
		public String describe () {
			return "Removing " + output.getItem().getRegistryName().toString();
		}

		@Override
		public void apply () {
			IGCTRecipe recipe = GCTRecipeList.instance.getRecipeByOutput(output);
			if (recipe == null) {
				CraftTweakerAPI.logError("Invalid recipe for " + output.getItem().getRegistryName().toString());
			} else {
				GCTRecipeList.instance.removeRecipe(recipe);
			}
		}
	}

	private static class Add extends BaseAction {
		private final ResourceLocation name;
		private final ItemStack output;
		private final IIngredient[] ingredients;
		private final boolean replace;

		private Add (String name, ItemStack output, IIngredient[] ingredients, boolean replace) {
			super("GCT Recipe addition");
			this.name = new ResourceLocation(ArcaneArchives.MODID, name);
			this.output = output;
			this.ingredients = ingredients;
			this.replace = replace;
		}

		@Override
		public void apply () {
			if (this.replace) {
				if (GCTRecipeList.instance.getRecipe(name) == null) {
					CraftTweakerAPI.logError("Attempting to replace recipe " + name.toString() + " when it doesn't exist. Use addRecipe instead.");
					return;
				}
			}
			List<IngredientStack> stacks = new ArrayList<>();
			for (IIngredient ingredient : ingredients) {
				stacks.add(new IngredientStack(CraftTweakerMC.getIngredient(ingredient), ingredient.getAmount()));
			}
			GCTRecipe recipe = new GCTRecipe(name, output, stacks);
			GCTRecipeList.instance.addRecipe(recipe);
		}

		@Override
		public String describe () {
			return "Adding GCT recipe for " + output.toString();
		}
	}
}
