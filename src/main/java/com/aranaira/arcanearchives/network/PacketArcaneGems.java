/*package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.GemSocketHandler;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.network.Handlers.ClientHandler;
import com.aranaira.arcanearchives.network.Messages.EmptyMessageServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

public class PacketArcaneGems {
	public static class GemParticle implements IMessage {

		private ArcaneGemItem.GemCut cut;
		private ArcaneGemItem.GemColor color;
		private Vec3d pos1, pos2;

		public GemParticle () {
		}

		public GemParticle (ArcaneGemItem.GemCut cut, ArcaneGemItem.GemColor color, Vec3d pos1, Vec3d pos2) {
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
			this.pos1 = new Vec3d(x, y, z);

			x = buf.readDouble();
			y = buf.readDouble();
			z = buf.readDouble();
			this.pos2 = new Vec3d(x, y, z);
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

		public static class Handler implements ClientHandler<GemParticle> {
			@Override
			public void processMessage (GemParticle packet, MessageContext context) {
				//ArcaneArchives.logger.info("Received particles packet\n    Gem is "+packet.color.name()+" "+packet.cut.name()+"\n    pos1="+packet.pos1+"    pos2="+packet.pos2);

				if (packet.cut == ArcaneGemItem.GemCut.ASSCHER) {
					if (packet.color == ArcaneGemItem.GemColor.BLUE) {
					}
				} else if (packet.cut == ArcaneGemItem.GemCut.OVAL) {
					if (packet.color == ArcaneGemItem.GemColor.BLACK) {
						Minecraft.getMinecraft().player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0f, new Random().nextFloat() * 0.5f + 0.75f);
					}
				} else if (packet.cut == ArcaneGemItem.GemCut.PENDELOQUE) {
					//Do nothing right now
				} else if (packet.cut == ArcaneGemItem.GemCut.PAMPEL) {
					if (packet.color == ArcaneGemItem.GemColor.GREEN) {
						Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
					}
				} else if (packet.cut == ArcaneGemItem.GemCut.TRILLION) {
					if (packet.color == ArcaneGemItem.GemColor.ORANGE) {
						Minecraft.getMinecraft().player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
					}
				}
			}
		}
	}

	public static class Toggle implements EmptyMessageServer<Toggle> {
		public Toggle () {
		}

		@Override
		public void processMessage (Toggle packet, MessageContext context) {
			//ArcaneArchives.logger.info("Received toggle packet");
			EntityPlayerMP player = context.getServerHandler().player;
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, EnumHand.MAIN_HAND);
			GemUtil.swapToggle(handler.getHeld());
		}
	}

	public static class OpenSocket implements EmptyMessageServer<OpenSocket> {
		public OpenSocket () {
		}

		@Override
		public void processMessage (OpenSocket message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			ItemStack stack = GemSocketHandler.findSocket(player);
			if (!stack.isEmpty()) {
				player.openGui(ArcaneArchives.instance, AAGuiHandler.BAUBLE_GEMSOCKET, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
		}
	}

	public static class RequestRecharge implements EmptyMessageServer<RequestRecharge> {
		public RequestRecharge () {

		}

		@Override
		public void processMessage (RequestRecharge message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().player;
			GemUtil.rechargeGems(player);
		}
	}
}*/
