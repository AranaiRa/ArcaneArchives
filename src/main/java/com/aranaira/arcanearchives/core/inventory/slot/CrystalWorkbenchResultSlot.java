package com.aranaira.arcanearchives.core.inventory.slot;

import com.aranaira.arcanearchives.api.reference.Constants;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public class CrystalWorkbenchResultSlot extends Slot {
  private final CrystalWorkbenchCrafting craftSlots;
  private final PlayerEntity player;
  @Nullable
  private CrystalWorkbenchRecipe recipe;
  private int removeCount;

  public CrystalWorkbenchResultSlot(PlayerEntity pPlayer, CrystalWorkbenchCrafting pCraftSlots, IInventory pContainer, int pSlot, int pXPosition, int pYPosition) {
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
      net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, pStack, this.craftSlots);
    }
  }

  @Override
  public boolean mayPickup(PlayerEntity player) {
    if (recipe == null) {
      return false;
    }

    return recipe.matches(this.craftSlots, player.level);
  }

  @Override
  public ItemStack onTake(PlayerEntity pPlayer, ItemStack pStack) {
    // Can this ever happen?
    if (recipe == null) {
      return ItemStack.EMPTY;
    }
    this.checkTakeAchievements(pStack);
/*    net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
    NonNullList<ItemStack> nonnulllist = pPlayer.level.getRecipeManager().getRemainingItemsFor(IRecipeType.CRAFTING, this.craftSlots, pPlayer.level);
    net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
    for (int i = 0; i < nonnulllist.size(); ++i) {
      ItemStack itemstack = this.craftSlots.getItem(i);
      ItemStack itemstack1 = nonnulllist.get(i);
      if (!itemstack.isEmpty()) {
        this.craftSlots.removeItem(i, 1);
        itemstack = this.craftSlots.getItem(i);
      }

      if (!itemstack1.isEmpty()) {
        if (itemstack.isEmpty()) {
          this.craftSlots.setItem(i, itemstack1);
        } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
          itemstack1.grow(itemstack.getCount());
          this.craftSlots.setItem(i, itemstack1);
        } else if (!this.player.inventory.add(itemstack1)) {
          this.player.drop(itemstack1, false);
        }
      }
    }

    return pStack;*/
    return ItemStack.EMPTY;
  }
}
