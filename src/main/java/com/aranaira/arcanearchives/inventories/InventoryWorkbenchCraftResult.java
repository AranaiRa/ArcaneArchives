/*
package com.aranaira.arcanearchives.inventories;


import com.aranaira.arcanearchives.api.cwb.ICrystalWorkbenchRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class InventoryWorkbenchCraftResult implements IInventory {
  private final NonNullList<ItemStack> stackResult = NonNullList.withSize(1, ItemStack.EMPTY);
  private ICrystalWorkbenchRecipe recipeUsed;

  @Override
  public int getSizeInventory() {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return this.stackResult.get(0).isEmpty();
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return this.stackResult.get(0);
  }

  @Override
  public String getName() {
    return "Result";
  }


  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent(this.getName());
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    return ItemStackHelper.getAndRemove(this.stackResult, 0);
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    return ItemStackHelper.getAndRemove(this.stackResult, 0);
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.stackResult.set(0, stack);
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public void markDirty() {
  }

  @Override
  public boolean isUsableByPlayer(PlayerEntity player) {
    return true;
  }

  @Override
  public void openInventory(PlayerEntity player) {
  }

  @Override
  public void closeInventory(PlayerEntity player) {
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }

  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {
  }

  @Override
  public int getFieldCount() {
    return 0;
  }

  @Override
  public void clear() {
    this.stackResult.clear();
  }

  public void setRecipeUsed(@Nullable ICrystalWorkbenchRecipe recipe) {
    this.recipeUsed = recipe;
  }

  @Nullable
  public ICrystalWorkbenchRecipe getRecipeUsed() {
    return this.recipeUsed;
  }
}
*/

