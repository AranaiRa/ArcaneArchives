/*package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class GCTRecipeWithCrafter extends GCTRecipe {
	public GCTRecipeWithCrafter (String name, @Nonnull ItemStack result, Object... recipe) {
		super(name, result, recipe);
	}

	@Override
	public ItemStack onCrafted (EntityPlayer player, ItemStack output) {
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(output);

		// TODO: This may need to become more complex to handle other members of hives
		tag.setUniqueId("creator", player.getUniqueID());
		tag.setString("creator_name", player.getDisplayNameString());
		return output;
	}
}*/
