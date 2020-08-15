package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.data.client.ClientNameData;
import com.aranaira.arcanearchives.data.storage.ClientDataStorage;
import com.aranaira.arcanearchives.tilenetwork.PlayerConfigAggregator;
import com.aranaira.arcanearchives.tilenetwork.PlayerNetworkConfig;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNetwork {
  public static class NameMessage implements IMessage {
    private ClientNameData data = new ClientNameData();

    public NameMessage() {
    }

    public NameMessage(ClientNameData data) {
      this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.data.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      this.data.toBytes(buf);
    }

    public static class Handler implements Handlers.ClientHandler<NameMessage> {
      @Override
      public void processMessage(NameMessage message, MessageContext ctx) {
        ClientDataStorage.setNameData(message.data);
      }
    }
  }

  public static class ConfigMessage implements IMessage {
    private PlayerNetworkConfig config = new PlayerNetworkConfig();

    public ConfigMessage() {
    }

    public ConfigMessage(PlayerNetworkConfig config) {
      this.config = config;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
      this.config.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
      this.config.toBytes(buf);
    }

    public static class Handler implements Handlers.ServerHandler<ConfigMessage> {
      @Override
      public void processMessage(ConfigMessage message, MessageContext ctx) {
        PlayerConfigAggregator.updatePlayer(ctx.getServerHandler().player, message.config);
      }
    }
  }
}
