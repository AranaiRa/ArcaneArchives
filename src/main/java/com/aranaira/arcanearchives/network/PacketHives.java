package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.network.NetworkHandler.ServerHandler;
import hellfirepvp.astralsorcery.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketHives {
	public static class AddMember implements IMessage {
		private UUID ownerId;
		private UUID newMemberId;

		public AddMember () {
		}

		public AddMember (UUID ownerId, UUID newMemberId) {
			this.ownerId = ownerId;
			this.newMemberId = newMemberId;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			this.ownerId = ByteBufUtils.readUUID(buf);
			this.newMemberId = ByteBufUtils.readUUID(buf);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteBufUtils.writeUUID(buf, ownerId);
			ByteBufUtils.writeUUID(buf, newMemberId);
		}

		public static class Handler extends ServerHandler<AddMember> {
			@Override
			public void processMessage (AddMember message, MessageContext ctx) {
				EntityPlayerMP player = ctx.getServerHandler().player;
				if (NetworkHelper.addToNetwork(message.ownerId, message.newMemberId, player.world)) {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.successfully_added"), true);
				} else {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.failed_add"), true);
				}
			}
		}
	}

	public static class RemoveMember implements IMessage {
		private UUID ownerId;
		private UUID newMemberId;

		public RemoveMember () {
		}

		public RemoveMember (UUID ownerId, UUID newMemberId) {
			this.ownerId = ownerId;
			this.newMemberId = newMemberId;
		}

		@Override
		public void fromBytes (ByteBuf buf) {
			this.ownerId = ByteBufUtils.readUUID(buf);
			this.newMemberId = ByteBufUtils.readUUID(buf);
		}

		@Override
		public void toBytes (ByteBuf buf) {
			ByteBufUtils.writeUUID(buf, ownerId);
			ByteBufUtils.writeUUID(buf, newMemberId);
		}

		public static class Handler extends ServerHandler<RemoveMember> {
			@Override
			public void processMessage (RemoveMember message, MessageContext ctx) {
				EntityPlayerMP player = ctx.getServerHandler().player;
				if (NetworkHelper.removeFromNetwork(message.ownerId, message.newMemberId, player.world)) {
					if (message.ownerId.equals(message.newMemberId)) {
						player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.successfully_removed_self"), true);
					} else {
						player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.successfully_remove_player"), true);
					}
				} else {
					if (message.ownerId.equals(message.newMemberId)) {
						player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.failed_remove_self"), true);
					} else {
						player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.failed_remove_player"), true);
					}
				}
			}
		}
	}
}
