package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.google.common.collect.Lists;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class GCTRecipe extends ShapelessOreRecipe implements IRecipe
{
	public List<String> TOOLTIP_CACHE = null;

	public GCTRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe)
	{
		super(group, result, recipe);
	}

	public static GCTRecipe buildAndAdd(String name, ItemStack result, Object... recipe)
	{
		ResourceLocation group = new ResourceLocation(ArcaneArchives.MODID, name);
		GCTRecipe gctRecipe = new GCTRecipe(group, result, recipe);

		GCTRecipeList.addRecipe(gctRecipe);

		return gctRecipe;
	}

	public int getIndex () {
		return GCTRecipeList.indexOf(this);
	}

	@Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world)
    {
        RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
        List<ItemStack> items = Lists.newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                if (this.isSimple)
                    recipeItemHelper.accountStack(itemstack);
                else
                    items.add(itemstack);
            }
        }

        if (this.isSimple)
            return recipeItemHelper.canCraft(this, null);

        return RecipeMatcher.findMatches(items, this.input) != null;
    }
}
