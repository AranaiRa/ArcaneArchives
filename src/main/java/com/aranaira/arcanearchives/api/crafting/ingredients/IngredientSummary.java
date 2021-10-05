package com.aranaira.arcanearchives.api.crafting.ingredients;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.*;

public class IngredientSummary {
  private final IntArrayList slots;
  private final Int2IntOpenHashMap slotCountMap;
  private final IngredientStack ingredient;
  private final int index;
  private final int foundTotal;

  protected IngredientSummary(IntArrayList slots, Int2IntOpenHashMap slotCountMap, IngredientStack ingredient, int index, int foundTotal) {
    this.slots = slots;
    this.slotCountMap = slotCountMap;
    this.ingredient = ingredient;
    this.index = index;
    this.foundTotal = foundTotal;
  }

  public IntArrayList getSlots() {
    return slots;
  }

  public Int2IntOpenHashMap getSlotCountMap() {
    return slotCountMap;
  }

  public IngredientStack getIngredient() {
    return ingredient;
  }

  public int getIndex() {
    return index;
  }

  public int getFoundTotal() {
    return foundTotal;
  }

  public static IngredientSummary of(IngredientStack stack, IngredientInfo... infos) {
    return of(stack, Arrays.asList(infos));
  }

  public static IngredientSummary of(IngredientStack stack, List<IngredientInfo> infos) {
    IntArrayList slots = new IntArrayList();
    Int2IntOpenHashMap slotToCount = new Int2IntOpenHashMap();
    int total = 0;
    int index = infos.get(0).getIndex();
    for (IngredientInfo info : infos) {
      slots.add(info.getSlot());
      slotToCount.put(info.getSlot(), info.getFound());
      total += info.getFound();
    }
    return new IngredientSummary(slots, slotToCount, stack, total, index);
  }

  public static Map<IngredientStack, IngredientSummary> of(List<IngredientStack> stacks, List<IngredientInfo> infos) {
    Map<IngredientStack, IngredientSummary> result = new HashMap<>();
    for (int i = 0; i < stacks.size(); i++) {
      List<IngredientInfo> thisInfos = new ArrayList<>();
      IngredientStack stack = stacks.get(i);
      for (IngredientInfo info : infos) {
        if (info.getIndex() == i) {
          thisInfos.add(info);
        }
      }
      if (!thisInfos.isEmpty()) {
        if (result.containsKey(stack)) {
          // TODO: ???
        }
        result.put(stack, of(stack, thisInfos));
      }
    }
    return result;
  }
}
