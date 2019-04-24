package com.aranaira.arcanearchives.tileentities;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;
import java.util.List;

public class RadiantCraftingTableTileEntity extends AATileEntity
{
	private ItemStackHandler persistentMatrix = new ItemStackHandler(9);
	private List<IRecipe> recipeList = Arrays.asList(new IRecipe[3]);

	public RadiantCraftingTableTileEntity() {
		super();
		setName("radiantcraftingtable");
	}

	public ItemStackHandler getInventory() {
		return persistentMatrix;
	}

	public IRecipe getRecipe(int index) {
		return recipeList.get(index);
	}

	public void setRecipe(int index, IRecipe recipe) {
		recipeList.set(index, recipe);
		markDirty();
	}


	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if(compound.hasKey(AATileEntity.Tags.INVENTORY)) {
			persistentMatrix.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		}

		for(int i = 0; i < 3; i++) {
			recipeList.set(i, null);
		}

		readRecipe(compound, Tags.RECIPE1, 0);
		readRecipe(compound, Tags.RECIPE2, 1);
		readRecipe(compound, Tags.RECIPE3, 2);
	}

	public void readRecipe(NBTTagCompound compound, String key, int index) {
		if(compound.hasKey(key)) {
			String rec = compound.getString(key);
			if(!rec.isEmpty()) {
				ResourceLocation loc = new ResourceLocation(rec);
				if(ForgeRegistries.RECIPES.containsKey(loc)) {
					recipeList.set(index, ForgeRegistries.RECIPES.getValue(loc));
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);

		compound.setTag(AATileEntity.Tags.INVENTORY, persistentMatrix.serializeNBT());

		storeRecipe(compound, Tags.RECIPE1, 0);
		storeRecipe(compound, Tags.RECIPE2, 1);
		storeRecipe(compound, Tags.RECIPE3, 2);

		return compound;
	}

	public void storeRecipe(NBTTagCompound compound, String tag, int index) {
		IRecipe recipe = recipeList.get(index);
		String rec = "";
		if(recipe != null && recipe.getRegistryName() != null) {
			rec = recipe.getRegistryName().toString();
		}

		compound.setString(tag, rec);
	}

	@SuppressWarnings("WeakerAccess")
	public static class Tags {
		public static final String RECIPE1 = "recipe1";
		public static final String RECIPE2 = "recipe2";
		public static final String RECIPE3 = "recipe3";
	}
}
