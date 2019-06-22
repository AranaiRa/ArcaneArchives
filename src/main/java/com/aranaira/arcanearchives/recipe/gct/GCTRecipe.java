package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.util.types.IngredientStack;
import com.aranaira.arcanearchives.util.types.IngredientsMatcher;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GCTRecipe {
	private final List<IngredientStack> ingredients = new ArrayList<>();
	private final ItemStack result;
	private final ResourceLocation name;

	public GCTRecipe (String name, @Nonnull ItemStack result, Object... recipe) {
		this(new ResourceLocation(ArcaneArchives.MODID, name), result, recipe);
	}

	public GCTRecipe (ResourceLocation name, @Nonnull ItemStack result, List<IngredientStack> ingredients) {
		this.result = result;
		this.name = name;
		this.ingredients.addAll(ingredients);
	}

	public GCTRecipe (ResourceLocation name, @Nonnull ItemStack result, Object... recipe) {
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

	public ResourceLocation getName () {
		return name;
	}

	public boolean matches (@Nonnull IItemHandler inv) {
		return new IngredientsMatcher(ingredients).matches(inv);
	}

	public boolean craftable (EntityPlayer player, GemCuttersTableTileEntity tile) {
		return true;
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

	// Only called on the server side, in theory
	public ItemStack onCrafted (EntityPlayer player, ItemStack output) {
		return output;
	}

	// Also only called on the server side
	public void handleItemResult (World world, EntityPlayer player, GemCuttersTableTileEntity tile, ItemStack ingredient) {
		boolean doReturn = false;

		IFluidHandlerItem cap = ingredient.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if (cap != null && cap.getTankProperties().length > 0) {
			FluidStack toDrain = cap.getTankProperties()[0].getContents();
			cap.drain(toDrain, true);
			ingredient = cap.getContainer();
			doReturn = true;
		}

		if (ingredient.getItem() instanceof ItemFlintAndSteel) {
			ingredient.damageItem(1, player);
			doReturn = true;
		}

		if (doReturn) {
			IItemHandler drawer = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			ItemStack result = ItemHandlerHelper.insertItemStacked(drawer, ingredient, false);
			if (!result.isEmpty()) {
				IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				result = ItemHandlerHelper.insertItemStacked(inventory, result, false);
				if (!result.isEmpty()) {
					Block.spawnAsEntity(world, player.getPosition(), result);
				}
			}
		}
	}
}
