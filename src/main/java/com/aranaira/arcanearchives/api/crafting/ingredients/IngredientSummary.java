package com.aranaira.arcanearchives.api.crafting.ingredients;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import java.util.*;

// TODO: I've kind of veered away from this but maybe it isn't a bad idea.
@Deprecated
public class IngredientSummary {
  private final IntArrayList slots;
  private final List<IngredientInfo.SlotType> slotTypes;
  private final Int2IntOpenHashMap slotCountMap;
  private final IngredientStack ingredient;
  private final int index;
  private final int foundTotal;

  protected IngredientSummary(IntArrayList slots, List<IngredientInfo.SlotType> slotTypes, Int2IntOpenHashMap slotCountMap, IngredientStack ingredient, int index, int foundTotal) {
    this.slots = slots;
    this.slotTypes = slotTypes;
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

  public List<IngredientInfo.SlotType> getSlotTypes() {
    return slotTypes;
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
    List<IngredientInfo.SlotType> types = new ArrayList<>();
    Int2IntOpenHashMap slotToCount = new Int2IntOpenHashMap();
    int total = 0;
    int index = infos.get(0).getIndex();
    for (IngredientInfo info : infos) {
      slots.add(info.getSlot());
      types.add(info.getType());
      slotToCount.put(info.getSlot(), info.getFound());
      total += info.getFound();
    }
    return new IngredientSummary(slots, types, slotToCount, stack, total, index);
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
