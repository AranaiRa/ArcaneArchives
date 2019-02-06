package com.aranaira.arcanearchives.packets;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.util.NonNullList;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RadiantChestListener implements IContainerListener
{
	private EntityPlayerMP player;

	public RadiantChestListener(EntityPlayerMP player)
	{
		this.player = player;
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
	{
		AAPacketHandler.CHANNEL.sendTo(new PacketsRadiantChest.WindowItemsPacket(containerToSend.windowId, itemsList), player);
		AAPacketHandler.CHANNEL.sendTo(new PacketsRadiantChest.SetSlotPacket(-1, -1, player.inventory.getItemStack()), player);
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
	{
		if(!(containerToSend.getSlot(slotInd) instanceof SlotCrafting))
		{
			if(!player.isChangingQuantityOnly)
			{
				AAPacketHandler.CHANNEL.sendTo(new PacketsRadiantChest.SetSlotPacket(containerToSend.windowId, slotInd, stack), player);
			}
		}
	}


	/* These just function as normal, passing back to the player */
	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
	{
		player.sendWindowProperty(containerIn, varToUpdate, newValue);
	}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory)
	{
		player.sendAllWindowProperties(containerIn, inventory);
	}
}
