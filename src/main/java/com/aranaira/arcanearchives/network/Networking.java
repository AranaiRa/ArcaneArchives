package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.network.IPacket;
import com.aranaira.arcanearchives.network.packets.client.RequestDomainNamesPacket;
import com.aranaira.arcanearchives.network.packets.server.ExtendedSlotContentsPacket;
import com.aranaira.arcanearchives.network.packets.server.LightningRenderPacket;
import com.aranaira.arcanearchives.network.packets.server.DomainNamesSyncPacket;
import com.aranaira.arcanearchives.network.packets.server.RecipeSyncPacket;
import com.aranaira.arcanearchives.network.packets.client.RequestSyncPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import noobanidus.libs.noobutil.network.PacketHandler;

import java.util.function.Supplier;

public class Networking extends PacketHandler {
  public static Networking INSTANCE = new Networking();

  public Networking() {
    super(ArcaneArchivesAPI.MODID);
  }

  @Override
  public void registerMessages() {
    registerMessage(ExtendedSlotContentsPacket.class, ExtendedSlotContentsPacket::encode, ExtendedSlotContentsPacket::new, Networking::handlePacket);
    registerMessage(LightningRenderPacket.class, LightningRenderPacket::encode, LightningRenderPacket::new, Networking::handlePacket);
    registerMessage(RecipeSyncPacket.class, RecipeSyncPacket::encode, RecipeSyncPacket::new, Networking::handlePacket);
    registerMessage(RequestSyncPacket.class,  RequestSyncPacket::encode, RequestSyncPacket::new, Networking::handlePacket);
    registerMessage(RequestDomainNamesPacket.class, RequestDomainNamesPacket::encode, RequestDomainNamesPacket::new, Networking::handlePacket);
    registerMessage(DomainNamesSyncPacket.class, DomainNamesSyncPacket::encode, DomainNamesSyncPacket::new, Networking::handlePacket);
  }

  public static <P extends IPacket> void handlePacket (P packet, Supplier<NetworkEvent.Context> context) {
    if (packet != null) {
      NetworkEvent.Context ctx = context.get();
      ctx.enqueueWork(() -> packet.handle(ctx));
      ctx.setPacketHandled(true);
    }
  }

  public static void register () {
    INSTANCE.registerMessages();
  }

  public static <MSG> void sendTo(MSG msg, ServerPlayerEntity player) {
    INSTANCE.sendToInternal(msg, player);
  }

  public static <MSG> void sendToServer(MSG msg) {
    INSTANCE.sendToServerInternal(msg);
  }

  public static <MSG> void sendToAllTracking (MSG msg, Entity entity) {
    sendToAllTracking(msg, entity, false);
  }

  public static <MSG> void sendToAllTracking (MSG msg, Entity entity, boolean self) {
    if (self) {
      send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    } else {
      send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }
  }

  public static <MSG> void sendToAllTracking (MSG msg, TileEntity tile) {
    sendToAllTracking(msg, tile.getLevel(), tile.getBlockPos());
  }

  public static <MSG> void sendToAllTracking (MSG msg, World level, BlockPos pos) {
    if (level instanceof ServerWorld) {
      ((ServerWorld) level).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(msg, p));
    } else {
      send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), msg);
    }
  }

  public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
    INSTANCE.sendInternal(target, message);
  }
}
