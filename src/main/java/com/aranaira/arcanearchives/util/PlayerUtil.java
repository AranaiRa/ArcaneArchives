package com.aranaira.arcanearchives.util;

import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerUtil {
	public static class Server {
		public static void syncInventory (EntityPlayerMP mpPlayer) {
			mpPlayer.sendAllContents(mpPlayer.inventoryContainer, mpPlayer.inventoryContainer.getInventory());
		}

		public static void syncContainer (EntityPlayerMP mpPlayer) {
			mpPlayer.sendAllContents(mpPlayer.openContainer, mpPlayer.openContainer.getInventory());
		}
	}
}
