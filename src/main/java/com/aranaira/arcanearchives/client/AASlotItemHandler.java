package com.aranaira.arcanearchives.client;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.NetworkHelper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class AASlotItemHandler extends SlotItemHandler 
{
	private UUID playerUUID;
	
	public AASlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, UUID playerID) 
	{
		super(itemHandler, index, xPosition, yPosition);
		playerUUID = playerID;
	}
	

	//Overriding this with no references to setStackInSlot
    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        if (stack.isEmpty())
            return false;

        if (NetworkHelper.getArcaneArchivesNetwork(playerUUID).GetTotalSpace() > 0)
        	return true;
        else
        	return false;
    }
    

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack)
    {
    	return NetworkHelper.getArcaneArchivesNetwork(playerUUID).GetTotalSpace();
    }
}
