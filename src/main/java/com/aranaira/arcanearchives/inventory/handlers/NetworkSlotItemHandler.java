package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class NetworkSlotItemHandler extends SlotItemHandler
{
	private EntityPlayer player;

	public NetworkSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, EntityPlayer player)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
	}

	public int getTotalSpace () {
		if (player.world.isRemote) {
			ArcaneArchivesClientNetwork network = NetworkHelper.getArcaneArchivesClientNetwork(player.getPersistentID());
			return network.GetTotalSpace();
		} else {
			ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(player.getPersistentID());
			return network.GetTotalSpace();
		}
	}

	//Overriding this with no references to setStackInSlot
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		if(stack.isEmpty()) return false;

		return getTotalSpace() > 0;
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack)
	{
		return getTotalSpace();
	}
}
