package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.tilenetwork.Network;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Objects;
import java.util.UUID;

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

  public static NameData getNameData () {
    WorldServer world = getWorld();
    NameData saveData = (NameData) Objects.requireNonNull(world.getMapStorage()).getOrLoadData(NameData.class, NameData.DATA);
    if (saveData == null) {
      saveData = new NameData();
      world.getMapStorage().setData(NameData.DATA, saveData);
    }

    return saveData;
  }

  public static NameData.NetworkName getNetworkName (Network network) {
    return getNetworkName(network.getNetworkId());
  }

  public static NameData.NetworkName getNetworkName (UUID uuid) {
    return getNameData().getOrGenerateName(uuid);
  }
}
