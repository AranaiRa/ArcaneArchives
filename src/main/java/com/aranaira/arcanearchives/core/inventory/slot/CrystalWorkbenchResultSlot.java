package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.api.crafting.ingredients.CountableIngredientStack;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.reference.Constants;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.aranaira.arcanearchives.api.reference.Constants.CrystalWorkbench.DataArray.Count;

public class CrystalWorkbenchResultSlot extends Slot {
  private final Supplier<CrystalWorkbenchCrafting> craftSlots;
  private final PlayerEntity player;
  @Nullable
  private CrystalWorkbenchRecipe recipe;
  private int removeCount;

  public CrystalWorkbenchResultSlot(PlayerEntity pPlayer, Supplier<CrystalWorkbenchCrafting> pCraftSlots, IInventory pContainer, int pSlot, int pXPosition, int pYPosition) {
    super(pContainer, pSlot, pXPosition, pYPosition);
    this.player = pPlayer;
    this.craftSlots = pCraftSlots;
  }

  @Override
  public boolean hasItem() {
    return recipe != null;
  }

  @Override
  public ItemStack getItem() {
    if (recipe == null) {
      return ItemStack.EMPTY;
    } else {
      return recipe.getResultItem();
    }
  }

  public void setRecipe (CrystalWorkbenchRecipe recipe) {
    this.recipe = recipe;
  }

  @Nullable
  public CrystalWorkbenchRecipe getRecipe () {
    return this.recipe;
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return false;
  }

  @Override
  public ItemStack remove(int pAmount) {
    if (this.hasItem()) {
      this.removeCount += Math.min(pAmount, this.getItem().getCount());
    }

    return super.remove(pAmount);
  }

  @Override
  protected void onQuickCraft(ItemStack pStack, int pAmount) {
    this.removeCount += pAmount;
    this.checkTakeAchievements(pStack);
  }

  @Override
  protected void onSwapCraft(int pNumItemsCrafted) {
    this.removeCount += pNumItemsCrafted;
  }

  protected void checkTakeAchievements(ItemStack pStack) {
    if (this.removeCount > 0) {
      pStack.onCraftedBy(this.player.level, this.player, this.removeCount);
      net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, pStack, this.craftSlots.get());
    }
  }

  @Override
  public boolean mayPickup(PlayerEntity player) {
    if (recipe == null) {
      return false;
    }

    return recipe.matches(this.craftSlots.get(), player.level);
  }

  @Override
  public ItemStack onTake(PlayerEntity pPlayer, ItemStack pStack) {
    // Can this ever happen?
    if (recipe == null) {
      return ItemStack.EMPTY;
    }
    this.checkTakeAchievements(pStack);
    CrystalWorkbenchCrafting crafting = craftSlots.get();
    net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
    ItemStack result = recipe.assemble(crafting);
    List<CountableIngredientStack> countableIngredientStackList = recipe.getIngredientStacks().stream().map(CountableIngredientStack::new).collect(Collectors.toList());
    NonNullList<ItemStack> processedItems = NonNullList.of(ItemStack.EMPTY);
    for (Slot ingredientSlot : crafting.getContainer().getIngredientSlots()) {
      if (!ingredientSlot.hasItem()) {
        continue;
      }

      ItemStack inSlot = ingredientSlot.getItem();
      if (inSlot.isEmpty()) {
        continue;
      }
      for (CountableIngredientStack ingredient : countableIngredientStackList) {
        if (!ingredient.apply(inSlot) || ingredient.filled()) {
          continue;
        }

        int toRemove = ingredient.subtract(inSlot.getCount());
        if (toRemove > 0) {
          ItemStack copy = inSlot.copy();
          copy.shrink(toRemove);
          for (Processor<CrystalWorkbenchCrafting> processors : recipe.getProcessors()) {
            processedItems.addAll(processors.processIngredient(result, ingredient, copy, crafting));
          }
          crafting.removeItem(ingredientSlot.getSlotIndex(), toRemove);
          break;
        }
      }
    }
    PlayerInventory inventory = crafting.getPlayerInventory();
    for (Slot playerSlot : crafting.getContainer().getPlayerSlots()) {
      if (!playerSlot.hasItem()) {
        continue;
      }

      ItemStack inSlot = playerSlot.getItem();
      if (inSlot.isEmpty()) {
        continue;
      }
      for (CountableIngredientStack ingredient : countableIngredientStackList) {
        if (!ingredient.apply(inSlot) || ingredient.filled()) {
          continue;
        }

        int toRemove = ingredient.subtract(inSlot.getCount());
        if (toRemove > 0) {
          ItemStack copy = inSlot.copy();
          copy.shrink(toRemove);
          for (Processor<CrystalWorkbenchCrafting> processors : recipe.getProcessors()) {
            processedItems.addAll(processors.processIngredient(result, ingredient, copy, crafting));
          }
          inventory.removeItem(playerSlot.getSlotIndex(), toRemove);
          // TODO: Handle post-processing here too.
          break;
        }
      }
    }
    net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
    for (CountableIngredientStack ingredient : countableIngredientStackList) {
      if (!ingredient.filled()) {
        throw new IllegalStateException("invalid recipe state: ingredient " + ingredient.toString() + " is not filled");
      }
    }
    PlayerEntity player = crafting.getPlayer();
    for (ItemStack stack : processedItems) {
      if (stack.isEmpty()) {
        continue;
      }

      if (!player.inventory.add(stack)) {
        crafting.getPlayer().drop(stack, false);
      }
    }

    return result;
  }
}
