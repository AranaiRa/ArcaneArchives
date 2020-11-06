package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ManifestConfig;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import com.aranaira.arcanearchives.util.WorldUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerNetworkConfig implements ISerializePacketBuffer<PlayerNetworkConfig> {
  private static int defaultMaxDistance = 100;
  private static boolean defaultDefaultRouting = false;
  private static boolean defaultTrovesDispense = true;

  private UUID playerId;
  private int maxDistance;
  private boolean defaultRouting;
  private boolean trovesDispense;

  public PlayerNetworkConfig () {
  }

  public PlayerNetworkConfig(UUID playerId, int maxDistance, boolean defaultRouting, boolean trovesDispense) {
    this.playerId = playerId;
    this.maxDistance = maxDistance;
    this.defaultRouting = defaultRouting;
    this.trovesDispense = trovesDispense;
  }

  public static PlayerNetworkConfig defaultConfig (UUID playerId) {
    return new PlayerNetworkConfig(playerId, defaultMaxDistance, defaultDefaultRouting, defaultTrovesDispense);
  }

  public static PlayerNetworkConfig fromPlayerPacket (ByteBuf buf) {
    PlayerNetworkConfig conf = new PlayerNetworkConfig();
    PacketBuffer buffer = new PacketBuffer(buf);
    conf.playerId = buffer.readUniqueId();
    conf.maxDistance = buffer.readInt();
    conf.defaultRouting = buffer.readBoolean();
    conf.trovesDispense = buffer.readBoolean();
    return conf;
  }

  @SideOnly(Side.CLIENT)
  @Nullable
  public static PlayerNetworkConfig fromClient () {
    PlayerEntity player = Minecraft.getMinecraft().player;
    if (player == null) {
      return null;
    }
    return new PlayerNetworkConfig(player.getUniqueID(), ManifestConfig.MaxDistance, ConfigHandler.defaultRoutingNoNewItems, ConfigHandler.trovesDispense);
  }

  @Nullable
  public PlayerEntity getPlayer () {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    if (server == null) {
      return null;
    }
    PlayerList list = server.getPlayerList();
    return list.getPlayerByUUID(playerId);
  }

  public boolean inRange (PlayerEntity player, BlockPos pos) {
    return WorldUtil.distanceSq(player.getPosition(), pos) <= maxDistance;
  }

  public boolean inRange (BlockPos pos) {
    PlayerEntity player = getPlayer();
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

  public boolean doTrovesDispense() {
    return trovesDispense;
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    buf.writeUniqueId(playerId);
    buf.writeInt(maxDistance);
    buf.writeBoolean(defaultRouting);
    buf.writeBoolean(trovesDispense);
  }
}
