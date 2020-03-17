package com.aranaira.arcanearchives.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Objects;

public class DataHelper {
  public static WorldServer getWorld() {
    return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
  }

  public static PlayerSaveData getPlayerData(EntityPlayer player) {
    WorldServer world = getWorld();
    PlayerSaveData saveData = (PlayerSaveData) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(PlayerSaveData.class, PlayerSaveData.ID(player));
    if (saveData == null) {
      saveData = new PlayerSaveData(player);
      world.getMapStorage().setData(saveData.getId(), saveData);
    }

    return saveData;
  }
}
