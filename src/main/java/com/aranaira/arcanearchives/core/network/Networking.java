package com.aranaira.arcanearchives.core.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import noobanidus.libs.noobutil.network.PacketHandler;

public class Networking extends PacketHandler {
  public static Networking INSTANCE = new Networking();

  public Networking() {
    super(ArcaneArchives.MODID);
  }

  @Override
  public void registerMessages() {
    registerMessage(ExtendedSlotContentsPacket.class, ExtendedSlotContentsPacket::encode, ExtendedSlotContentsPacket::new, ExtendedSlotContentsPacket::handle);
    registerMessage(LightningRenderPacket.class, LightningRenderPacket::encode, LightningRenderPacket::decode, LightningRenderPacket::handle);
  }

  public static void register () {
    INSTANCE.registerMessages();
  }

  public static void sendTo(Object msg, ServerPlayerEntity player) {
    INSTANCE.sendToInternal(msg, player);
  }

  public static void sendToServer(Object msg) {
    INSTANCE.sendToServerInternal(msg);
  }

  public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
    INSTANCE.sendInternal(target, message);
  }
}
