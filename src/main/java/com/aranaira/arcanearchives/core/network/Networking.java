package com.aranaira.arcanearchives.core.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

  public static <MSG> void sendToAllTracking (MSG msg, World world, BlockPos pos) {
    if (world instanceof ServerWorld) {
      ((ServerWorld) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(msg, p));
    } else {
      send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), msg);
    }
  }

  public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
    INSTANCE.sendInternal(target, message);
  }
}
