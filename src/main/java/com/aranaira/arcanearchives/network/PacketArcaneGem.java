package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.particles.ParticleGenerator;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler.ClientHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundList;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

public class PacketArcaneGem implements IMessage {

	private ArcaneGemItem.GemCut cut;
	private ArcaneGemItem.GemColor color;
	private Vec3d pos1, pos2;

	public PacketArcaneGem() {
	}

	public PacketArcaneGem(ArcaneGemItem.GemCut cut, ArcaneGemItem.GemColor color, Vec3d pos1, Vec3d pos2) {
		this.cut = cut;
		this.color = color;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	@Override
	public void fromBytes (ByteBuf buf) {
		this.cut = ArcaneGemItem.GemCut.fromByte(buf.readByte());
		this.color = ArcaneGemItem.GemColor.fromByte(buf.readByte());

		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		this.pos1 = new Vec3d(x,y,z);

		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		this.pos2 = new Vec3d(x,y,z);
	}

	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeByte(ArcaneGemItem.GemCut.ToByte(cut));
		buf.writeByte(ArcaneGemItem.GemColor.ToByte(color));

		buf.writeDouble(pos1.x);
		buf.writeDouble(pos1.y);
		buf.writeDouble(pos1.z);

		buf.writeDouble(pos2.x);
		buf.writeDouble(pos2.y);
		buf.writeDouble(pos2.z);
	}

	public static class Handler extends ClientHandler<PacketArcaneGem> {
		@Override
		public void processMessage (PacketArcaneGem packet, MessageContext context) {
			ArcaneArchives.logger.info("Received particles packet\n    Gem is "+packet.color.name()+" "+packet.cut.name()+"\n    pos1="+packet.pos1+"    pos2="+packet.pos2);

			if(packet.cut == ArcaneGemItem.GemCut.OVAL) {
				if(packet.color == ArcaneGemItem.GemColor.BLACK) {
					Minecraft.getMinecraft().player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0f, new Random().nextFloat() * 0.5f + 0.75f);
				}
			}
			else if(packet.cut == ArcaneGemItem.GemCut.PENDELOQUE) {
				int particleDensity = 5 * (int) Math.ceil(packet.pos1.distanceTo(packet.pos2));

				ParticleGenerator.makeDefaultLine(Minecraft.getMinecraft().player.world, packet.pos1, packet.pos2, particleDensity, 2.0);
				ParticleGenerator.makeDefaultBurst(Minecraft.getMinecraft().player.world, packet.pos2, 36, 1, 0.6, 0.01, 0.03);
			}
			else if(packet.cut == ArcaneGemItem.GemCut.PAMPEL) {
				if(packet.color == ArcaneGemItem.GemColor.GREEN) {
					Minecraft.getMinecraft().player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, 1.0F);
					ParticleGenerator.makeDefaultBurstOnEntity(Minecraft.getMinecraft().player.world, packet.pos2, 18, 5, 0.5, 0.01, 0.03);
				}
			}
			else if(packet.cut == ArcaneGemItem.GemCut.TRILLION) {
				if(packet.color == ArcaneGemItem.GemColor.ORANGE) {
					Minecraft.getMinecraft().player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
				}
			}
		}
	}
}
