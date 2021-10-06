package com.aranaira.arcanearchives.core.util;

import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;

import java.util.*;

public class ArcaneRecipeUtil {
  /**
   * @param recipe The CrystalWorkbenchRecipe.
   * @param crafting The CrystalWorkbenchCrafting instance.
   * @return A list of IngredientInfos containing information about each ingredient and slot.
   */
  public static List<IngredientInfo> getIngredientInfo(CrystalWorkbenchRecipe recipe, CrystalWorkbenchCrafting crafting) {
    List<IngredientStack> ingredients = recipe.getIngredientStacks();
    List<IngredientInfo> result = new ArrayList<>();
    PlayerEntity player = crafting.getPlayer();
    Set<Slot> parsedSlots = new HashSet<>();
    IntOpenHashSet parsedPlayerSlots = new IntOpenHashSet();

    for (int i = 0; i < ingredients.size(); i++) {
      IngredientStack stack = ingredients.get(i);

      for (Slot slot : crafting.getContainer().getIngredientSlots()) {
        if (parsedSlots.contains(slot)) {
          continue;
        }

        if (stack.apply(slot.getItem())) {
          result.add(new IngredientInfo(slot.getSlotIndex(), i, slot.getItem().getCount(), IngredientInfo.SlotType.CONTAINER));
          parsedSlots.add(slot);
        }
      }

      for (int o = 0; i < player.inventory.items.size(); i++) {
        if (parsedPlayerSlots.contains(o)) {
          continue;
        }

        if (stack.apply(player.inventory.items.get(o))) {
          result.add(new IngredientInfo(o, i, player.inventory.items.get(o).getCount(), IngredientInfo.SlotType.PLAYER_INVENTORY));
          parsedPlayerSlots.add(o);
        }
      }
    }

    return result;
  }
}
