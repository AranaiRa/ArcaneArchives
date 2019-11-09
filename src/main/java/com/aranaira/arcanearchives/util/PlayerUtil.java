package com.aranaira.arcanearchives.util;

import net.minecraft.entity.player.ServerPlayerEntity;

public class PlayerUtil {
  public static class Server {
    public static void syncInventory(ServerPlayerEntity mpPlayer) {
      mpPlayer.sendAllContents(mpPlayer.inventoryContainer, mpPlayer.inventoryContainer.getInventory());
    }

    public static void syncContainer(ServerPlayerEntity mpPlayer) {
      mpPlayer.sendAllContents(mpPlayer.openContainer, mpPlayer.openContainer.getInventory());
    }
  }
}
