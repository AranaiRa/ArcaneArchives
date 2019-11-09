package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.*;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Handlers.ServerHandler;
import com.aranaira.arcanearchives.types.lists.ManifestList;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNetworks {
  public enum SynchroniseType {
    INVALID("invalid"), DATA("data"), MANIFEST("manifest"), HIVE_STATUS("hive_status");

    private String key;

    SynchroniseType(String key) {
      this.key = key;
    }

    public static SynchroniseType fromOrdinal(int i) {
      for (SynchroniseType type : values()) {
        if (type.ordinal() == i) {
          return type;
        }
      }

      return INVALID;
    }

    public String key() {
      return this.key;
    }
  }

  public static class Request implements IMessage {
    protected SynchroniseType type;

    public Request() {
      this.type = SynchroniseType.DATA;
    }

    public Request(SynchroniseType type) {
      this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.type = SynchroniseType.fromOrdinal(buf.readShort());
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeShort(this.type.ordinal());
    }

    public static class Handler implements ServerHandler<Request> {
      @Override
      public void processMessage(Request message, MessageContext context) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) {
          ArcaneArchives.logger.error("Server was null when processing sync packet");
          return;
        }

        ServerPlayerEntity player = context.getServerHandler().player;

        ServerNetwork network = DataHelper.getServerNetwork(player.getUniqueID());
        if (network == null) {
          ArcaneArchives.logger.error("Network was null when processing sync packet for " + player.getUniqueID());
          return;
        }

        IMessage response = null;

        switch (message.type) {
          case DATA:
            response = new DataResponse(network.buildSynchroniseData());
            break;
          case HIVE_STATUS:
            response = new HiveResponse(network.buildHiveMembershipData());
            break;
          case MANIFEST:
            response = new ManifestResponse(network.buildSynchroniseManifest());
            break;
        }

        if (response != null) {
          Networking.CHANNEL.sendTo(response, player);
        }
      }
    }
  }

  public static class ManifestResponse implements IMessage {
    private ManifestList data;

    public ManifestResponse() {
    }

    public ManifestResponse(ManifestList data) {
      this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.data = ManifestList.deserialize(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      this.data.toBytes(buf);
    }

    public static class Handler extends ResponseHandler<ManifestResponse> {
      @Override
      public void processMessage(ManifestResponse message, MessageContext ctx, PlayerEntity player, ClientNetwork network) {
        network.deserializeManifest(message.data);
      }
    }
  }

  public static class DataResponse implements IMessage {
    private SynchroniseInfo data;

    public DataResponse() {
    }

    public DataResponse(SynchroniseInfo data) {
      this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.data = SynchroniseInfo.deserialize(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      this.data.toBytes(buf);
    }

    public static class Handler extends ResponseHandler<DataResponse> {
      @Override
      public void processMessage(DataResponse message, MessageContext ctx, PlayerEntity player, ClientNetwork network) {
        network.deserializeData(message.data);
      }
    }
  }

  public static class HiveResponse implements IMessage {
    private HiveMembershipInfo data;

    public HiveResponse() {
    }

    public HiveResponse(HiveMembershipInfo data) {
      this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.data = HiveMembershipInfo.deserialize(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      this.data.toBytes(buf);
    }

    public static class Handler extends ResponseHandler<HiveResponse> {
      @Override
      public void processMessage(HiveResponse message, MessageContext ctx, PlayerEntity player, ClientNetwork network) {
        network.deserializeHive(message.data);
      }
    }
  }

  public static abstract class ResponseHandler<T extends IMessage> implements ClientHandler<T> {
    @Override
    @OnlyIn(Dist.CLIENT)
    public void processMessage(T message, MessageContext context) {
      PlayerEntity player;
      try {
        player = Minecraft.getMinecraft().player;
      } catch (NullPointerException e) {
        System.out.println("Exception: missing player or Minecraft when handling packet: " + this.getClass().toString());
        return;
      }

      if (player == null) {
        return;
      }

      ClientNetwork network = DataHelper.getClientNetwork(player.getUniqueID());
      processMessage(message, context, player, network);
    }

    public abstract void processMessage(T message, MessageContext ctx, PlayerEntity player, ClientNetwork network);
  }
}
