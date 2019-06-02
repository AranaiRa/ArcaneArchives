package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.util.NBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class GCTRecipeWithCreator extends GCTRecipe {
	public GCTRecipeWithCreator (String name, @Nonnull ItemStack result, Object... recipe) {
		super(name, result, recipe);
	}

	@Override
	public ItemStack onCrafted (EntityPlayer player, ItemStack output) {
		NBTTagCompound tag = NBTUtils.getOrCreateTagCompound(output);

		// TODO: This may need to become more complex to handle other members of hives
		tag.setUniqueId("creator", player.getUniqueID());
		return output;
	}
}
