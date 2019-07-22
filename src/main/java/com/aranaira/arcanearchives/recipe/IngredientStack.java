package com.aranaira.arcanearchives.recipe;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IngredientStack {
	private final Ingredient ingredient;
	private int count;

	@Nullable
	private final NBTTagCompound nbt;

	public IngredientStack (ItemStack stack) {
		this.ingredient = Ingredient.fromStacks(stack);
		this.count = stack.getCount();
		this.nbt = stack.getTagCompound();
	}

	public IngredientStack (Item item, int count) {
		this(item, count, null);
	}

	public IngredientStack (Item item, int count, NBTTagCompound nbt) {
		this.ingredient = Ingredient.fromItem(item);
		this.count = count;
		this.nbt = nbt;
	}

	public IngredientStack (Item item) {
		this(item, 1, null);
	}

	public IngredientStack (String item, int count) {
		this(item, count, null);
	}

	public IngredientStack (String item, int count, NBTTagCompound nbt) {
		this.ingredient = new OreIngredient(item);
		this.count = count;
		this.nbt = nbt;
	}

	public IngredientStack (String item) {
		this(item, 1, null);
	}

	public IngredientStack (Ingredient ingredient, int count) {
		this(ingredient, count, null);
	}

	public IngredientStack (Ingredient ingredient, int count, NBTTagCompound nbt) {
		this.ingredient = ingredient;
		this.count = count;
		this.nbt = nbt;
	}

	public IngredientStack (Ingredient ingredient) {
		this(ingredient, 1, null);
	}

	public IngredientStack (Block item, int count) {
		this(item, count, null);
	}

	public IngredientStack (Block block, int count, NBTTagCompound nbt) {
		this.ingredient = Ingredient.fromItem(Item.getItemFromBlock(block));
		this.count = count;
		this.nbt = nbt;
	}

	public IngredientStack (Block item) {
		this(item, 1, null);
	}

	public ItemStack[] getMatchingStacks () {
		return ingredient.getMatchingStacks();
	}

	public boolean apply (@Nullable ItemStack p_apply_1_) {
		boolean res = ingredient.apply(p_apply_1_);
		if (nbt != null && p_apply_1_ != null) {
			return res && nbt.equals(p_apply_1_.getTagCompound());
		}

		return res;
	}

	public IntList getValidItemStacksPacked () {
		return ingredient.getValidItemStacksPacked();
	}

	public boolean isSimple () {
		return ingredient.isSimple();
	}

	public int getCount () {
		return count;
	}

	public void shrink (int amount) {
		this.count -= amount;
	}

	public void grow (int amount) {
		this.count += amount;
	}

	public void shrink () {
		shrink(1);
	}

	public void grow () {
		grow(1);
	}

	public Ingredient getIngredient () {
		return ingredient;
	}

	@Nullable
	public NBTTagCompound getNBT () {
		return nbt;
	}

	public List<ItemStack> getMatchingStacksWithSizes () {
		List<ItemStack> result = new ArrayList<>();
		for (ItemStack stack : getMatchingStacks()) {
			ItemStack copy = stack.copy();
			copy.setCount(getCount());
			result.add(copy);
		}
		return result;
	}
}
