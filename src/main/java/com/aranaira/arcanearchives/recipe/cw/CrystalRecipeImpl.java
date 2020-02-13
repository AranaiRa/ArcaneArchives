package com.aranaira.arcanearchives.recipe.cw;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class CrystalRecipeImpl extends CrystalWorkbenchRecipe {
  public CrystalRecipeImpl(List<IngredientStack> inputs, ItemStack output) {
    super(inputs, output);
  }

  public CrystalRecipeImpl(ItemStack output, List<Object> inputs) {
    super(calculateInputs(inputs), output);
  }

  private static List<IngredientStack> calculateInputs(List<Object> inputs) {
    List<IngredientStack> result = new ArrayList<>();
    int i = 0;
    for (Object stack : inputs) {
      if (stack instanceof ItemStack) {
        result.add(new IngredientStack((ItemStack) stack));
      } else if (stack instanceof Item) {
        result.add(new IngredientStack((Item) stack));
      } else if (stack instanceof Ingredient) {
        result.add(new IngredientStack((Ingredient) stack));
      } else if (stack instanceof String) {
        result.add(new IngredientStack((String) stack));
      } else if (stack instanceof IngredientStack) {
        result.add((IngredientStack) stack);
      } else if (stack instanceof Block) {
        result.add(new IngredientStack((Block) stack));
      } else {
        ArcaneArchives.logger.warn("Unknown ingredient #" + i + ", skipping type: " + stack.getClass().getSimpleName());
      }
      i++;
    }
    return result;
  }
}
