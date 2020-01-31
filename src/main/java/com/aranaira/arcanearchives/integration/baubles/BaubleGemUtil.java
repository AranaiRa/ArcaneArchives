/*package com.aranaira.arcanearchives.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class BaubleGemUtil {
	public static ItemStack findSocket (EntityPlayer player) {
		IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
		for (int i : BaubleType.BODY.getValidSlots()) {
			ItemStack stack = handler.getStackInSlot(i);
			if (stack.getItem() == ItemRegistry.BAUBLE_GEMSOCKET) {
				return stack;
			}
		}

		return ItemStack.EMPTY;
	}

	public static void markDirty (EntityPlayer player, int baubleSlot) {
		if (!player.world.isRemote && baubleSlot != -1) {
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			PacketSync packet = new PacketSync(player, baubleSlot, baubles.getStackInSlot(baubleSlot));
			PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
		}
	}

	public static GemSocketHandler getSocket (EntityPlayer player) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int i : BaubleType.BODY.getValidSlots()) {
			ItemStack stack = baubles.getStackInSlot(i);
			if (stack.getItem() == ItemRegistry.BAUBLE_GEMSOCKET) {
				GemSocketHandler handler = GemSocketHandler.getHandler(stack);
				handler.setBaubleSlot(i);
				return handler;
			}
		}

		return null;
	}
}*/
