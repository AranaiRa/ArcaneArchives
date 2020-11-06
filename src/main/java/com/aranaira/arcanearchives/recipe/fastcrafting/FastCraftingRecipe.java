package com.aranaira.arcanearchives.recipe.fastcrafting;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import com.aranaira.arcanearchives.api.cwb.IArcaneArchivesRecipe;
import com.aranaira.arcanearchives.api.cwb.IngredientTransformer;
import com.aranaira.arcanearchives.api.cwb.MatchResult;
import com.aranaira.arcanearchives.api.cwb.WorkbenchCrafting;
import com.aranaira.arcanearchives.util.ItemUtils;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FastCraftingRecipe implements IArcaneArchivesRecipe {
  private final List<IngredientStack> ingredients = new ArrayList<>();
  private ItemStack result;
  private final IRecipe originalRecipe;
  private final World world;
  private final FakeContainer fakeContainer = new FakeContainer();
  private List<ItemStack> returned = new ArrayList<>();

  public FastCraftingRecipe(IRecipe input, World world) {
    this.originalRecipe = input;
    this.world = world;
    Map<Ingredient, IngredientStack> ingredientMap = new HashMap<>();
    for (Ingredient ingredient : input.getIngredients()) {
      if (ingredient == Ingredient.EMPTY) {
        continue;
      }

      IngredientStack match = null;
      for (Map.Entry<Ingredient, IngredientStack> entry : ingredientMap.entrySet()) {
        if (ItemUtils.ingredientsMatch(entry.getKey(), ingredient)) {
          match = entry.getValue();
          break;
        }
      }
      if (match == null) {
        ingredientMap.put(ingredient, new IngredientStack(ingredient, 1));
      } else {
        match.grow();
      }
    }
    ingredients.addAll(ingredientMap.values());
  }

  private CraftingInventory createMatrix(@Nonnull WorkbenchCrafting inv, boolean simulate) {
    MatchResult matcher = new MatchResult(this, inv);
    Int2IntOpenHashMap matchingSlots = matcher.getSlotMap();

    int minSize = 1;
    for (; minSize <= 3; minSize++) {
      if (originalRecipe.canFit(minSize, minSize)) {
        break;
      }
    }

    CraftingInventory matrix = new CraftingInventory(fakeContainer, minSize, minSize);

    List<ItemStack> temp = new ArrayList<>();
    for (Int2IntMap.Entry slot : matchingSlots.int2IntEntrySet()) {
      for (int i = 0; i < slot.getIntValue(); i++) {
        temp.add(inv.extractItem(slot.getIntKey(), 1, simulate));
      }
    }

    int slot = 0;
    outer:
    for (Ingredient ingredient : originalRecipe.getIngredients()) {
      if (ingredient == Ingredient.EMPTY) {
        slot++;
        continue;
      }
      Iterator<ItemStack> iter = temp.iterator();
      while (iter.hasNext()) {
        ItemStack next = iter.next();
        if (ingredient.apply(next)) {
          iter.remove();
          matrix.setInventorySlotContents(slot, next);
          slot++;
          continue outer;
        }
      }
    }

    if (!temp.isEmpty()) {
      ArcaneArchives.logger.error("wat");
    }

    return matrix;
  }

  @Override
  public MatchResult matches(WorkbenchCrafting inv) {
    return new MatchResult(this, inv);
  }

  @Override
  public ItemStack getActualResult(WorkbenchCrafting inventory, UUID workbenchId) {
    return getResult();
  }

  @Override
  public List<IngredientStack> getIngredients() {
    return ingredients;
  }

  private void setResult(ItemStack stack) {
    result = stack;
  }

  @Override
  public ItemStack getResult() {
    return result;
  }

  @Override
  public void addIngredientTransformer(IngredientTransformer transformer) {

  }

  @Override
  public List<IngredientTransformer> getIngredientTransformers() {
    return Collections.emptyList();
  }

  @Override
  public NonNullList<ItemStack> getRemainingIngredients(WorkbenchCrafting inventory, @Nullable PlayerEntity player) {
    return NonNullList.create();
  }

  public List<ItemStack> getReturned() {
    return returned;
  }

  public void consumeAndHandleInventory(IArcaneArchivesRecipe recipe, WorkbenchCrafting inventory, PlayerEntity player, @Nullable TileEntity tile, @Nullable Runnable callback, @Nullable Runnable finalCallback) {
    CraftingInventory matrix = createMatrix(inventory, false);

    CraftResultInventory craftResult = new CraftResultInventory();
    CraftingResultSlot slot = new CraftingResultSlot(player, matrix, craftResult, 0, 0, 0);
    craftResult.setRecipeUsed(originalRecipe);
    craftResult.setInventorySlotContents(0, originalRecipe.getCraftingResult(matrix));
    ItemStack result = slot.onTake(player, craftResult.getStackInSlot(0));
    returned.clear();

    returned.add(result);

    for (int i = 0; i < matrix.getSizeInventory(); i++) {
      ItemStack inSlot = matrix.removeStackFromSlot(i);
      if (!inSlot.isEmpty()) {
        returned.add(inSlot);
      }
    }

    if (finalCallback != null) {
      finalCallback.run();
    }
  }

  public static class FakeContainer extends Container {
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
      return false;
    }
  }
}
