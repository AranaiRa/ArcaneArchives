package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.ingredient.CountableIngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

  public void setRecipe(CrystalWorkbenchRecipe recipe) {
    if (recipe != this.recipe) {
      this.recipe = recipe;
      if (recipe != null) {
        this.container.setItem(0, recipe.getResultItem());
      } else {
        this.container.setItem(0, ItemStack.EMPTY);
      }
    } else if (recipe != null) {
      if (this.container.getItem(0).isEmpty()) {
        this.container.setItem(0, recipe.getResultItem());
      }
    } else {
      this.container.setItem(0, ItemStack.EMPTY);
    }
  }

  @Nullable
  public CrystalWorkbenchRecipe getRecipe() {
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
    net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
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
          if (!player.level.isClientSide()) {
            ItemStack copy = inSlot.copy();
            for (Processor<CrystalWorkbenchCrafting> processors : recipe.getProcessors()) {
              processedItems.addAll(processors.processIngredient(result, ingredient, copy, crafting));
            }
          }
          ingredientSlot.remove(toRemove);
          break;
        } else {
          if (!player.level.isClientSide()) {
            ItemStack copy = inSlot.copy();
            for (Processor<CrystalWorkbenchCrafting> processors : recipe.getProcessors()) {
              processedItems.addAll(processors.processIngredient(result, ingredient, copy, crafting));
            }
          }
          ingredientSlot.set(ItemStack.EMPTY);
          break;
        }
      }
    }
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
          if (!player.level.isClientSide()) {
            ItemStack copy = inSlot.copy();
            for (Processor<CrystalWorkbenchCrafting> processors : recipe.getProcessors()) {
              processedItems.addAll(processors.processIngredient(result, ingredient, copy, crafting));
            }
          }
          playerSlot.remove(toRemove);
          break;
        } else {
          if (!player.level.isClientSide()) {
            ItemStack copy = inSlot.copy();
            for (Processor<CrystalWorkbenchCrafting> processors : recipe.getProcessors()) {
              processedItems.addAll(processors.processIngredient(result, ingredient, copy, crafting));
            }
          }
          playerSlot.set(ItemStack.EMPTY);
          break;
        }
      }
    }

    for (CountableIngredientStack ingredient : countableIngredientStackList) {
      if (!ingredient.filled()) {
        throw new IllegalStateException("invalid recipe state: ingredient " + ingredient.toString() + " is not filled");
      }
    }
    PlayerEntity player = crafting.getPlayer();
    if (!player.level.isClientSide()) {
      for (ItemStack stack : processedItems) {
        if (stack.isEmpty()) {
          continue;
        }

        if (!player.inventory.add(stack)) {
          crafting.getPlayer().drop(stack, false);
        }
      }
    }
    return result;
  }
}
