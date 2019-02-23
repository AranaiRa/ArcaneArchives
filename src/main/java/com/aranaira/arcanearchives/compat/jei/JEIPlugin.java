package com.aranaira.arcanearchives.compat.jei;

import com.aranaira.arcanearchives.init.BlockRegistry;
import mezz.jei.api.*;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin
{
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 3;
	public static IJeiHelpers jeiHelpers;
	public static ICraftingGridHelper craftingGridHelper;
	public static IRecipeRegistry recipeRegistry;

	@Override
	public void register(@Nonnull IModRegistry registry)
	{
		jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		// crafting helper used by the shaped table wrapper
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);

		registry.getRecipeTransferRegistry().addRecipeTransferHandler(new CraftingStationRecipeTransferInfo());
		registry.addRecipeCatalyst(new ItemStack(BlockRegistry.RADIANT_CRAFTING_TABLE, 1, 0), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}
}
