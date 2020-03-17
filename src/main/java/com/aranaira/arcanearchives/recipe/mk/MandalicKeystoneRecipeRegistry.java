package com.aranaira.arcanearchives.recipe.mk;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import com.aranaira.arcanearchives.init.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreIngredient;

import java.util.HashMap;
import java.util.Map;

public class MandalicKeystoneRecipeRegistry {
    private static Map<ResourceLocation, MandalicKeystoneRecipe> TRANSFORMATION_RECIPES = new HashMap();

    public static void initializeRecipes(){
        addRecipe(new ResourceLocation(ArcaneArchives.MODID, "stalwart_stone"), new MandalicKeystoneRecipe(new IngredientStack(new OreIngredient("stone"), 1), new IngredientStack(ModBlocks.StalwartStone, 1)));
        addRecipe(new ResourceLocation(ArcaneArchives.MODID, "stalwart_wood"), new MandalicKeystoneRecipe(new IngredientStack(new OreIngredient("logWood"), 1), new IngredientStack(ModBlocks.StalwartWood, 1)));
        addRecipe(new ResourceLocation(ArcaneArchives.MODID, "gravel2flint"), new MandalicKeystoneRecipe(new IngredientStack(new OreIngredient("gravel"), 1), new IngredientStack(Items.FLINT, 1)));
        addRecipe(new ResourceLocation(ArcaneArchives.MODID, "cobweb2string"), new MandalicKeystoneRecipe(new IngredientStack(Blocks.WEB, 1), new IngredientStack(Items.STRING, 1)));
        addRecipe(new ResourceLocation(ArcaneArchives.MODID, "vines2string"), new MandalicKeystoneRecipe(new IngredientStack(Blocks.VINE, 6), new IngredientStack(Items.STRING, 1)));
    }

    public static boolean addRecipe(ResourceLocation loc, MandalicKeystoneRecipe rec) {
        if(TRANSFORMATION_RECIPES.containsKey(loc))
            return false;

        TRANSFORMATION_RECIPES.put(loc, rec);
        return true;
    }

    public static boolean removeRecipe(ResourceLocation loc, MandalicKeystoneRecipe rec) {
        if(!TRANSFORMATION_RECIPES.containsKey(loc))
            return false;

        TRANSFORMATION_RECIPES.remove(loc);
        return true;
    }

    public static MandalicKeystoneRecipe findRecipe(ItemStack stack) {
        MandalicKeystoneRecipe out = null;

        for(MandalicKeystoneRecipe mkr : TRANSFORMATION_RECIPES.values()) {
            if (mkr.getRecipeOutput().getIngredient().test(stack)) {
                out = mkr;
                break;
            }
        }

        return out;
    }

    public static MandalicKeystoneRecipe getRecipe(ResourceLocation loc) {
        return TRANSFORMATION_RECIPES.get(loc);
    }
}
