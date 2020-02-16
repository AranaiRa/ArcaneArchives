package com.aranaira.arcanearchives.api.cwb;

import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

public class MatchResult {
  private boolean matches = false;
  private boolean matched = false;
  private Object2IntOpenHashMap<IngredientStack> ingredientCounts = new Object2IntOpenHashMap<>();
  private Object2IntOpenHashMap<IngredientStack> matchMap;
  private Int2IntOpenHashMap slotMap = new Int2IntOpenHashMap();
  private CrystalWorkbenchRecipe recipe;
  private WorkbenchCrafting inventory;

  public MatchResult(CrystalWorkbenchRecipe recipe, WorkbenchCrafting inv) {
    this.recipe = recipe;
    this.inventory = inv;
    for (IngredientStack ingredient : getIngredients()) {
      ingredientCounts.put(ingredient, ingredient.getCount());
    }
  }

  public Object2IntOpenHashMap<IngredientStack> getCounts () {
    return ingredientCounts.clone();
  }

  public CrystalWorkbenchRecipe getRecipe() {
    return recipe;
  }

  public boolean matches () {
    if (matched) {
      return matches;
    }

    checkMatches();
    return matches;
  }

  private void checkMatches () {
    Object2IntOpenHashMap<IngredientStack> counts = getCounts();
    Object2IntOpenHashMap<IngredientStack> discounts = getCounts();

    slotMap.clear();

    IItemHandlerModifiable inv = inventory.getInventory();
    for (int i = 0; i < inv.getSlots(); i++) {
      ItemStack inSlot = inv.getStackInSlot(i);
      if (inSlot.isEmpty()) {
        continue;
      }

      for (IngredientStack ingredient : getIngredients()) {
        if (ingredient.isEmpty()) {
          continue;
        }

        if (ingredient.apply(inSlot)) {
          accountMatch(counts, ingredient, inSlot);
          int discount = discountMatch(discounts, ingredient, inSlot);
          if (discount != -1) {
            slotMap.put(i, discount);
          }
        }
      }
    }

    matchMap = counts;
    matches = counts.isEmpty();
    matched = true;
  }

  private void accountMatch (Object2IntOpenHashMap<IngredientStack> counts, IngredientStack ingredient, ItemStack stack) {
    if (counts.containsKey(ingredient)) {
      int current = counts.getInt(ingredient);
      int incoming = stack.getCount();
      if (current - incoming <= 0) {
        counts.remove(ingredient, current);
      } else {
        counts.put(ingredient, current - incoming);
      }
    }
  }

  private int discountMatch (Object2IntOpenHashMap<IngredientStack> counts, IngredientStack ingredient, ItemStack stack) {
    int result = -1;

    if (counts.containsKey(ingredient)) {
      int incoming = stack.getCount();
      int needed = counts.getInt(ingredient);
      if (incoming >= needed) {
        result = needed;
        counts.remove(ingredient, needed);
      } else {
        counts.put(ingredient, needed - incoming);
        result = incoming;
      }
    }

    return result;
  }

  protected WorkbenchCrafting getInventory () {
    return inventory;
  }

  protected List<IngredientStack> getIngredients () {
    return recipe.getIngredients();
  }

  public Object2IntOpenHashMap<IngredientStack> getMatchMap() {
    return matchMap;
  }

  public Int2IntOpenHashMap getSlotMap () {
    return slotMap;
  }
}
