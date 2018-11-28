package com.aranaira.arcanearchives.common;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class AASlot extends Slot 
{

	public AASlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		// TODO Auto-generated constructor stub
	}

	public boolean Contains(int mouseX, int mouseY, int offLeft, int topOffset)
	{
		if (mouseX > offLeft + xPos && mouseY > yPos + topOffset && mouseX < offLeft + xPos + 16 && mouseY < yPos + topOffset + 16)
		{
			return true;
		}
		return false;
	}
}
