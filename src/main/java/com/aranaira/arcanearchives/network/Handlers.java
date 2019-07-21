package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.Handlers.BaseHandler;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class Handlers {
	public interface BaseHandler<T extends IMessage> extends IMessageHandler<T, IMessage> {
		void processMessage (T message, MessageContext ctx);
	}

	public interface ServerHandler<T extends IMessage> extends BaseHandler<T> {
		default IMessage onMessage (T message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}
	}

	public interface TileHandlerServer<T extends Messages.TileMessage> extends ServerHandler<T> {
		@Override
		default void processMessage (T message, MessageContext ctx) {
			processMessage(message, ctx, getTile(message, ctx));
		}

		default ImmanenceTileEntity getTile (T message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			if (player == null) {
				return null;
			}

			UUID networkId = player.getUniqueID();

			ServerNetwork network = DataHelper.getServerNetwork(networkId, player.world);

			IteRef ref;

			if (message.getTileId() != null) {
				ref = network.getTiles().getReference(message.getTileId());
			} else {
				ref = network.getTiles().getReference(message.getPos(), message.getDimension());
			}

			if (ref == null) {
				return null;
			}
			ref.refreshTile(player.world, player.dimension);
			return ref.getTile();
		}

		void processMessage (T message, MessageContext ctx, ImmanenceTileEntity tile);
	}

	public interface ClientHandler<T extends IMessage> extends BaseHandler<T> {
		default IMessage onMessage (T message, MessageContext ctx) {
			ArcaneArchives.proxy.scheduleTask(() -> processMessage(message, ctx), Side.CLIENT);

			return null;
		}
	}

}
