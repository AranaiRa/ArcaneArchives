/*package com.aranaira.arcanearchives.containers.slots;

import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.tileentities.CrystalWorkbenchTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotRecipeHandler extends Slot {
  private static IInventory emptyInventory = new Inventory(0);
  private final int index;
  private final CrystalWorkbenchTile tile;

  public SlotRecipeHandler(int index, int xPosition, int yPosition, CrystalWorkbenchTile tile) {
    super(emptyInventory, index, xPosition, yPosition);
    this.index = index;
    this.tile = tile;
  }

  public CrystalWorkbenchRecipe getRecipe() {
    return CrystalWorkbenchRegistry.getRegistry().getValueByIndex(getRelativeIndex());
  }

  public int getRelativeIndex() {
    return index + getPage() * 7;
  }

  public int getPage() {
    return tile.getPage();
  }

  @Override
  public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
    onSlotChanged();
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  @Override
  public ItemStack getStack() {
    CrystalWorkbenchRecipe recipe = getRecipe();
    if (recipe == null) {
      return ItemStack.EMPTY;
    } else {
      return recipe.getResult();
    }
  }

  @Override
  public boolean getHasStack() {
    return !getStack().isEmpty();
  }

  @Override
  public void putStack(ItemStack stack) {
  }

  @Override
  public void onSlotChanged() {
  }

  @Override
  public int getSlotStackLimit() {
    return 1;
  }

  @Override
  public int getItemStackLimit(ItemStack stack) {
    return 1;
  }

  // Handle this TODO ???
  @Nullable
  @Override
  public String getSlotTexture() {
    return super.getSlotTexture();
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean isHere(IInventory inv, int slotIn) {
    return false;
  }

  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    return false;
  }

  @Override
  public int getSlotIndex() {
    return super.getSlotIndex();
  }

  @Override
  public boolean isSameInventory(net.minecraft.inventory.container.Slot other) {
    return false;
  }
}*/
