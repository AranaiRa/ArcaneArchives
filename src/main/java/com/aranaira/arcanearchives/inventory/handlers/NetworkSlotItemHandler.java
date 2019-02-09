package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NetworkSlotItemHandler extends SlotItemHandler
{
	private EntityPlayer player;
	@Nullable
	private ArcaneArchivesNetwork network = null;
	private ArcaneArchivesClientNetwork cNetwork = null;

	public NetworkSlotItemHandler(IItemHandler itemHandler, ArcaneArchivesNetwork network, int index, int xPosition, int yPosition, EntityPlayer player)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
		this.network = network;
	}

	public NetworkSlotItemHandler(IItemHandler itemHandler, ArcaneArchivesClientNetwork network, int index, int xPosition, int yPosition, EntityPlayer player)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
		this.cNetwork = network;
	}

	public int getTotalSpace()
	{
		if(player.world.isRemote)
		{
			return cNetwork.GetTotalSpace();
		} else
		{
			if (network == null) // TODO: Error
				return 0;
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
