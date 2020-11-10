/*package com.aranaira.arcanearchives.integration.jei;

import com.aranaira.arcanearchives.ArcaneArchives;
import mezz.jei.api.*;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
  public static final String GEM_CUTTERS_TABLE = ArcaneArchives.MODID + ".gem_cutters_table";
  public static final String RADIANT_RESONATOR = ArcaneArchives.MODID + ".radiant_resonator";

  private static final int craftOutputSlot = 0;
  private static final int craftInputSlot1 = 3;
  public static IJeiHelpers jeiHelpers;
  public static ICraftingGridHelper craftingGridHelper;
  public static IRecipeRegistry recipeRegistry;
  public static IIngredientRegistry itemRegistry;
  public static IJeiRuntime runtime;

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    *//*    registry.addRecipeCategories(new GCTCategory(helper), new QuartzCategory(helper));*//*
  }

  @Override
  public void register(@Nonnull IModRegistry registry) {
    jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    itemRegistry = registry.getIngredientRegistry();

    // crafting helper used by the shaped table wrapper
    craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
*//*
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(new CraftingStationRecipeTransferInfo());
    registry.addRecipeCatalyst(new ItemStack(BlockRegistry.RADIANT_CRAFTING_TABLE, 1, 0), VanillaRecipeCategoryUid.CRAFTING);

    registry.handleRecipes(GCTRecipe.class, GCTWrapper::new, GEM_CUTTERS_TABLE);
    registry.addRecipes(GCTRecipeList.instance.getRecipeList(), GEM_CUTTERS_TABLE);
    registry.addRecipeCatalyst(new ItemStack(BlockRegistry.GEMCUTTERS_TABLE), GEM_CUTTERS_TABLE);

    registry.handleRecipes(FakeQuartzRecipe.class, QuartzWrapper::new, RADIANT_RESONATOR);
    registry.addRecipes(Collections.singletonList(new FakeQuartzRecipe()), RADIANT_RESONATOR);
    registry.addRecipeCatalyst(new ItemStack(BlockRegistry.RADIANT_RESONATOR), RADIANT_RESONATOR);*//*

  }

  @Override
  public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    runtime = jeiRuntime;
    recipeRegistry = jeiRuntime.getRecipeRegistry();

    NonNullList<ItemStack> ignores = NonNullList.create();
*//*    ItemRegistry.CHROMATIC_POWDER.getSubItems(CreativeTabs.SEARCH, ignores);
    ignores.add(new ItemStack(ItemRegistry.CHROMATIC_POWDER));
    ignores.add(new ItemStack(ItemRegistry.RAINBOW_CHROMATIC_POWDER));*//*

    //itemRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM, ignores);
  }
}*/
