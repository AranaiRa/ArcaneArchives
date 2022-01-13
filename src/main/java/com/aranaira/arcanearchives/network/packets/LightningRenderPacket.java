package com.aranaira.arcanearchives.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.particleslib.client.effects.BoltEffect;
import noobanidus.libs.particleslib.client.events.RenderTickHandler;

import java.util.function.Supplier;

public class LightningRenderPacket {
  private final LightningPreset preset;
  private final Vector3d start;
  private final Vector3d end;
  private final int renderer;
  private final int segments;

  public LightningRenderPacket(LightningPreset preset, int renderer, Vector3d start, Vector3d end, int segments) {
    this.preset = preset;
    this.renderer = renderer;
    this.start = start;
    this.end = end;
    this.segments = segments;
  }

  public static void handle(LightningRenderPacket message, Supplier<NetworkEvent.Context> context) {
    NetworkEvent.Context ctx = context.get();
    ctx.enqueueWork(() -> RenderTickHandler.renderBolt(message.renderer, message.preset.boltCreator.create(message.start, message.end, message.segments)));
    ctx.setPacketHandled(true);
  }

  public static void encode(LightningRenderPacket pkt, PacketBuffer buf) {
    buf.writeEnum(pkt.preset);
    buf.writeVarInt(pkt.renderer);
    buf.writeDouble(pkt.start.x);
    buf.writeDouble(pkt.start.y);
    buf.writeDouble(pkt.start.z);
    buf.writeDouble(pkt.end.x);
    buf.writeDouble(pkt.end.y);
    buf.writeDouble(pkt.end.z);
    buf.writeVarInt(pkt.segments);
  }

  public static LightningRenderPacket decode(PacketBuffer buf) {
    LightningPreset preset = buf.readEnum(LightningPreset.class);
    int renderer = buf.readVarInt();

    Vector3d start = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    Vector3d end = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    int segments = buf.readVarInt();
    return new LightningRenderPacket(preset, renderer, start, end, segments);
  }

  @FunctionalInterface
  public interface BoltCreator {
    BoltEffect create(Vector3d start, Vector3d end, int segments);
  }

  public enum LightningPreset {
    MAGNETIC_ATTRACTION((start, end, segments) -> new BoltEffect(BoltEffect.BoltRenderInfo.ELECTRICITY, start, end, segments).size(0.04F).lifespan(8).spawn(BoltEffect.SpawnFunction.noise(8, 4))),
    TOOL_AOE((start, end, segments) -> new BoltEffect(BoltEffect.BoltRenderInfo.ELECTRICITY, start, end, segments).size(0.015F).lifespan(12).spawn(BoltEffect.SpawnFunction.NO_DELAY));

    private final BoltCreator boltCreator;

    LightningPreset(BoltCreator boltCreator) {
      this.boltCreator = boltCreator;
    }
  }
}
