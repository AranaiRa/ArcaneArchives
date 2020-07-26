package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerNetworkConfig {
  public static int defaultMaxDistance = 100;
  public static boolean defaultDefaultRouting = false;
  public static boolean defaultTrovesDispense = true;

  private final UUID playerId;
  private final int maxDistance;
  private final boolean defaultRouting;
  private final boolean trovesDispense;

  public PlayerNetworkConfig(UUID playerId, int maxDistance, boolean defaultRouting, boolean trovesDispense) {
    this.playerId = playerId;
    this.maxDistance = maxDistance;
    this.defaultRouting = defaultRouting;
    this.trovesDispense = trovesDispense;
  }

  public static PlayerNetworkConfig defaultConfig (UUID playerId) {
    return new PlayerNetworkConfig(playerId, defaultMaxDistance, defaultDefaultRouting, defaultTrovesDispense);
  }

  @Nullable
  public EntityPlayer getPlayer () {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    if (server == null) {
      return null;
    }
    PlayerList list = server.getPlayerList();
    return list.getPlayerByUUID(playerId);
  }

  public boolean inRange (EntityPlayer player, BlockPos pos) {
    return WorldUtil.distanceSq(player.getPosition(), pos) <= maxDistance;
  }

  public boolean inRange (BlockPos pos) {
    EntityPlayer player = getPlayer();
    if (player == null) {
      return false;
    }

    return inRange(player, pos);
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public int getMaxDistance() {
    return maxDistance;
  }

  public boolean isDefaultRouting() {
    return defaultRouting;
  }

  public boolean isTrovesDispense() {
    return trovesDispense;
  }
}
