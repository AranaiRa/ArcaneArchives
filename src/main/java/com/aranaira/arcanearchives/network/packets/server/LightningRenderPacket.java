package com.aranaira.arcanearchives.network.packets.server;

import com.aranaira.arcanearchives.api.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import noobanidus.libs.particleslib.client.effects.BoltEffect;
import noobanidus.libs.particleslib.client.events.RenderTickHandler;

import java.util.function.Supplier;

public class LightningRenderPacket implements IPacket {
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

  public LightningRenderPacket(PacketBuffer buf) {
    this.preset = buf.readEnum(LightningPreset.class);
    this.renderer = buf.readVarInt();
    this.start = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    this.end = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    this.segments = buf.readVarInt();
  }

  public void handle(NetworkEvent.Context context) {
    RenderTickHandler.renderBolt(this.renderer, this.preset.boltCreator.create(this.start, this.end, this.segments));
  }

  public void encode(PacketBuffer buf) {
    buf.writeEnum(this.preset);
    buf.writeVarInt(this.renderer);
    buf.writeDouble(this.start.x);
    buf.writeDouble(this.start.y);
    buf.writeDouble(this.start.z);
    buf.writeDouble(this.end.x);
    buf.writeDouble(this.end.y);
    buf.writeDouble(this.end.z);
    buf.writeVarInt(this.segments);
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
