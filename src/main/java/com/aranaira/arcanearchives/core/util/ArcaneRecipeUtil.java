package com.aranaira.arcanearchives.core.util;

import com.aranaira.arcanearchives.api.crafting.ingredients.CollatedInfoPair;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArcaneRecipeUtil {
  /**
   * @param recipe   The CrystalWorkbenchRecipe.
   * @param crafting The CrystalWorkbenchCrafting instance.
   * @return A list of IngredientInfos containing information about each ingredient and slot.
   */
  public static List<IngredientInfo> getIngredientInfo(CrystalWorkbenchRecipe recipe, CrystalWorkbenchCrafting crafting) {
    List<IngredientStack> ingredients = recipe.getIngredientStacks();
    List<IngredientInfo> result = new ArrayList<>();
    Set<Slot> parsedSlots = new HashSet<>();
    IntOpenHashSet ingredientsChecked = new IntOpenHashSet();
    List<Slot> playerSlots = crafting.getContainer().getPlayerSlots();

    for (int i = 0; i < ingredients.size(); i++) {
      IngredientStack stack = ingredients.get(i);

      for (Slot slot : crafting.getContainer().getIngredientSlots()) {
        if (parsedSlots.contains(slot)) {
          continue;
        }

        if (stack.apply(slot.getItem())) {
          result.add(new IngredientInfo(slot.getSlotIndex(), i, slot.getItem().getCount(), stack.getCount(), IngredientInfo.SlotType.CONTAINER));
          parsedSlots.add(slot);
          ingredientsChecked.add(i);
        }
      }

      for (int o = 0; o < playerSlots.size(); o++) {
        Slot slot = playerSlots.get(o);
        if (parsedSlots.contains(slot) || !slot.hasItem()) {
          continue;
        }

        ItemStack inSlot = slot.getItem();

        if (stack.apply(inSlot)) {
          result.add(new IngredientInfo(o, i, stack.getCount(), inSlot.getCount(), IngredientInfo.SlotType.PLAYER_INVENTORY));
          parsedSlots.add(slot);
          ingredientsChecked.add(i);
        }
      }
    }

    for (int i = 0; i < ingredients.size(); i++) {
      if (ingredientsChecked.contains(i)) {
        continue;
      }
      IngredientStack stack = ingredients.get(i);

      result.add(new IngredientInfo(-1, -1, 0, stack.getCount(), IngredientInfo.SlotType.NOT_FOUND));
    }

    return result;
  }

  public static List<IngredientInfo> getCollatedIngredientInfo (CrystalWorkbenchRecipe recipe, List<IngredientInfo> infoList) {
    NonNullList<IngredientStack> ingredients = recipe.getIngredientStacks();
    List<IngredientInfo> result = new ArrayList<>();
    for (int i = 0; i < ingredients.size(); i++) {
      IngredientStack stack = ingredients.get(i);
      int foundTotal = 0;
      for (IngredientInfo info : infoList) {
        if (info.getIndex() == i) {
          foundTotal += info.getFound();
        }
      }
      result.add(new IngredientInfo(-1, i, stack.getCount(), foundTotal, foundTotal == 0 ? IngredientInfo.SlotType.NOT_FOUND : IngredientInfo.SlotType.COLLATED));
    }
    return result;
  }

  public static List<IngredientInfo> getCollatedIngredientInfo (CrystalWorkbenchRecipe recipe, CrystalWorkbenchCrafting crafting) {
    return getCollatedIngredientInfo(recipe, getIngredientInfo(recipe, crafting));
  }

  public static List<CollatedInfoPair> getCollatedIngredientInfoPairs (CrystalWorkbenchRecipe recipe, CrystalWorkbenchCrafting crafting) {
    List<IngredientInfo> info = getCollatedIngredientInfo(recipe, crafting);
    List<IngredientStack> ingredients = recipe.getIngredientStacks();
    List<CollatedInfoPair> result = new ArrayList<>();
    for (IngredientInfo thisInfo : info) {
      result.add(new CollatedInfoPair(ingredients.get(thisInfo.getIndex()), thisInfo));
    }
    return result;
  }

}
