package com.aranaira.arcanearchives.api.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket {
  void handle (NetworkEvent.Context context);
  void encode (PacketBuffer buffer);

  static <P extends IPacket> void handle (P packet, Supplier<NetworkEvent.Context> context) {
    if (packet != null) {
      NetworkEvent.Context ctx = context.get();
      ctx.enqueueWork(() -> packet.handle(ctx));
      ctx.setPacketHandled(true);
    }
  }
}
