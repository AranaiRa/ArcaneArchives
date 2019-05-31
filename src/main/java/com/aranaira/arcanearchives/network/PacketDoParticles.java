package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.particles.ParticleGenerator;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.RadiantAmphoraItem.AmphoraUtil;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDoParticles implements IMessage {

	private ArcaneGemItem.GemCut cut;
	private ArcaneGemItem.GemColor color;
	private Vec3d pos1, pos2;

	public PacketDoParticles() {
	}

	public PacketDoParticles(ArcaneGemItem.GemCut cut, ArcaneGemItem.GemColor color, Vec3d pos1, Vec3d pos2) {
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

	public static class Handler extends ServerHandler<PacketDoParticles> {
		@Override
		public void processMessage (PacketDoParticles packet, MessageContext context) {
			ArcaneArchives.logger.info("Received particles packet\n    Gem is "+packet.color.name()+" "+packet.cut.name()+"\n    pos1="+packet.pos1+"    pos2="+packet.pos2);

			ParticleGenerator.makeDefaultLine(Minecraft.getMinecraft().player.world, packet.pos1, packet.pos2, 40, 2.0);
			ParticleGenerator.makeDefaultBurst(Minecraft.getMinecraft().player.world, packet.pos2, 36, 1,0.6, 0.01, 0.03);
		}
	}
}
