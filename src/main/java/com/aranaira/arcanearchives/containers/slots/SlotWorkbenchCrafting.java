package com.aranaira.arcanearchives.containers.slots;


import com.aranaira.arcanearchives.api.cwb.ICrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.api.cwb.WorkbenchCrafting;
import com.aranaira.arcanearchives.inventories.InventoryWorkbenchCraftResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWorkbenchCrafting extends Slot {
  public final WorkbenchCrafting craftMatrix;
  public final EntityPlayer player;
  public int amountCrafted;

  public SlotWorkbenchCrafting(EntityPlayer player, WorkbenchCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
    this.player = player;
    this.craftMatrix = craftingInventory;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    if (this.getHasStack()) {
      this.amountCrafted += Math.min(amount, this.getStack().getCount());
    }

    return super.decrStackSize(amount);
  }

  @Override
  protected void onCrafting(ItemStack stack, int amount) {
    this.amountCrafted += amount;
    this.onCrafting(stack);
  }

  @Override
  protected void onSwapCraft(int amount) {
    this.amountCrafted += amount;
  }

  @Override
  protected void onCrafting(ItemStack stack) {
    if (this.amountCrafted > 0) {
      stack.onCrafting(this.player.world, this.player, this.amountCrafted);
    }

    this.amountCrafted = 0;
    InventoryWorkbenchCraftResult inventorycraftresult = (InventoryWorkbenchCraftResult) this.inventory;
    ICrystalWorkbenchRecipe irecipe = inventorycraftresult.getRecipeUsed();

    if (irecipe != null) {
      inventorycraftresult.setRecipeUsed(null);
    }
  }
}

