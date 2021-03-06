package com.aranaira.arcanearchives.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.UUID;

public class NBTUtils {
	public static void setUUID (NBTTagCompound tag, String key, UUID uuid) {
		tag.setLong(key + "_most", uuid.getMostSignificantBits());
		tag.setLong(key + "_least", uuid.getLeastSignificantBits());
	}

	public static UUID getUUID (NBTTagCompound tag, String key) {
		long most = tag.getLong(key + "_most");
		long least = tag.getLong(key + "_least");
		return new UUID(most, least);
	}

	public static void setRecipe (NBTTagCompound tag, String key, IRecipe recipe) {
		if (recipe != null) {
			tag.setInteger(key, CraftingManager.REGISTRY.getIDForObject(recipe));
		}
	}

	@Nullable
	public static IRecipe getRecipe (NBTTagCompound tag, String key) {
		if (!tag.hasKey(key)) {
			return null;
		}

		return CraftingManager.REGISTRY.getObjectById(tag.getInteger(key));
	}

	public static int defaultInt (ItemStack stack, String key, int defaultInt) {
		if (!stack.hasTagCompound()) {
			return defaultInt;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if (!tag.hasKey(key)) {
			return defaultInt;
		}

		return defaultInt(tag, key, defaultInt);
	}

	public static int defaultInt (NBTTagCompound tag, String key, int defaultInt) {
		if (!tag.hasKey(key)) {
			return defaultInt;
		}

		return tag.getInteger(key);
	}
}
