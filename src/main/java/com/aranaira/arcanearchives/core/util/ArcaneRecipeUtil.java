package com.aranaira.arcanearchives.core.util;

import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.NonNullList;

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
    PlayerEntity player = crafting.getPlayer();
    Set<Slot> parsedSlots = new HashSet<>();
    IntOpenHashSet parsedPlayerSlots = new IntOpenHashSet();
    IntOpenHashSet ingredientsChecked = new IntOpenHashSet();

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

      for (int o = 0; i < player.inventory.items.size(); i++) {
        if (parsedPlayerSlots.contains(o)) {
          continue;
        }

        if (stack.apply(player.inventory.items.get(o))) {
          result.add(new IngredientInfo(o, i, player.inventory.items.get(o).getCount(), stack.getCount(), IngredientInfo.SlotType.PLAYER_INVENTORY));
          parsedPlayerSlots.add(o);
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
      result.add(new IngredientInfo(-1, i, stack.getCount(), foundTotal, foundTotal == 0 ? IngredientInfo.SlotType.NOT_FOUND : IngredientInfo.SlotType.GENERIC));
    }
    return result;
  }

  public static List<IngredientInfo> getCollatedIngredientInfo (CrystalWorkbenchRecipe recipe, CrystalWorkbenchCrafting crafting) {
    return getCollatedIngredientInfo(recipe, getIngredientInfo(recipe, crafting));
  }
}
