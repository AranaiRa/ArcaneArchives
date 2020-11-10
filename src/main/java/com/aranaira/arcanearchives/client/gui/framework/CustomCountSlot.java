package com.aranaira.arcanearchives.client.gui.framework;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CustomCountSlot extends SlotItemHandler implements ICustomCountSlot {

  public CustomCountSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public ItemStack getStack() {
    ItemStack stack = super.getStack();
    if (stack.isEmpty()) {
      return stack;
    }
    stack = stack.copy();
    stack.setCount(1);
    return stack;
  }

  @Override
  public int getX() {
    return xPos;
  }

  @Override
  public int getY() {
    return yPos;
  }

  @Override
  public ItemStack getItemStack() {
    return super.getStack();
  }
}
