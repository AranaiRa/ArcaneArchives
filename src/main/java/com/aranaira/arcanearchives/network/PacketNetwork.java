package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.data.client.ClientNameData;
import com.aranaira.arcanearchives.data.storage.ClientDataStorage;
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
}
