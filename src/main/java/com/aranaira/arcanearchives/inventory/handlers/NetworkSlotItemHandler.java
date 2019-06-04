package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.ServerNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deprecated
public class NetworkSlotItemHandler extends SlotItemHandler {
	private EntityPlayer player;
	@Nullable
	private ServerNetwork network = null;
	private ClientNetwork cNetwork = null;

	public NetworkSlotItemHandler (IItemHandler itemHandler, ServerNetwork network, int index, int xPosition, int yPosition, EntityPlayer player) {
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
		this.network = network;
	}

	public NetworkSlotItemHandler (IItemHandler itemHandler, ClientNetwork network, int index, int xPosition, int yPosition, EntityPlayer player) {
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
		this.cNetwork = network;
	}

	//Overriding this with no references to setStackInSlot
	@Override
	public boolean isItemValid (@Nonnull ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		return getTotalSpace() > 0;
	}

	public int getTotalSpace () {
		if (player.world.isRemote) {
			return cNetwork.GetTotalSpace();
		} else {
			return 0;
			//if(network == null) // TODO: Error
			//	return 0;
			//return network.GetTotalSpace();
		}
	}

	@Override
	public int getItemStackLimit (@Nonnull ItemStack stack) {
		return getTotalSpace();
	}
}
