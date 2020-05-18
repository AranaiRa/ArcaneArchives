package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.HiveNetwork;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.Messages.ConfigPacket;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.types.IteRef;
import com.aranaira.arcanearchives.types.lists.ITileList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class Handlers {
	public interface BaseHandler<T extends IMessage> extends IMessageHandler<T, IMessage> {
		void processMessage (T message, MessageContext ctx);
	}

	public interface ServerHandler<T extends IMessage> extends BaseHandler<T> {
		@Override
		default IMessage onMessage (T message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public interface TileHandlerServer<T extends Messages.TileMessage, V extends ImmanenceTileEntity> extends ServerHandler<T> {
		@Override
		default void processMessage (T message, MessageContext ctx) {
			V tile = getTile(message, ctx);
			if (tile == null) {
				ArcaneArchives.logger.error("Unable to resolve tile reference for message of type " + message.getClass().getSimpleName() + " targetting " + message.getPos() + " in dimension " + message.getDimension() + ". Halting execution.", new NullPointerException());
				return;
			}
			processMessage(message, ctx, tile);
		}

		@Nullable
		default V getTile (T message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			if (player == null) {
				return null;
			}

			UUID networkId = player.getUniqueID();

			ServerNetwork network = DataHelper.getServerNetwork(networkId);

			if (network == null) {
				return null;
			}

			IteRef ref;
			ITileList tiles;

			if (network.isHiveMember()) {
				HiveNetwork hive = network.getHiveNetwork();
				if (hive == null) {
					return null;
				}
				tiles = hive.getTiles();
			} else {
				tiles = network.getTiles();
			}

			if (message.getTileId() != null) {
				ref = tiles.getReference(message.getTileId());
			} else {
				ref = tiles.getReference(message.getPos(), message.getDimension());
			}

			if (ref == null) {
				return null;
			}
			try {
				return (V) ref.getTile();
			} catch (ClassCastException exception) {
				ArcaneArchives.logger.error("Attempted to cast to an invalid tile entity: " + ref.getTile().getClass());
				return null;
			}
		}

		void processMessage (T message, MessageContext ctx, @Nonnull V tile);
	}

	public interface ClientHandler<T extends IMessage> extends BaseHandler<T> {
		@Override
		default IMessage onMessage (T message, MessageContext ctx) {
			ArcaneArchives.proxy.scheduleTask(() -> processMessage(message, ctx), Side.CLIENT);

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public interface TileHandlerClient<T extends Messages.TileMessage, V extends ImmanenceTileEntity> extends ClientHandler<T> {
		@Override
		@SideOnly(Side.CLIENT)
		default void processMessage (T message, MessageContext ctx) {
			V tileEntity = getTile(message, ctx);
			if (tileEntity != null) {
				processMessage(message, ctx, tileEntity);
			} else {
				ArcaneArchives.logger.error("WARNING! Unable to handle client-side Tile packet due to invalid or missing tile entity.", new IllegalArgumentException());
			}
		}

		@Nullable
		@SideOnly(Side.CLIENT)
		default V getTile (T message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			World world = mc.world;

			BlockPos pos = message.getPos();
			int dimension = message.getDimension();
			if (message.getPos() == null || dimension == -9999) {
				return null;
			}

			if (world.provider.getDimension() != dimension) {
				return null;
			}

			TileEntity te = world.getTileEntity(pos);
			if (te == null) {
				return null;
			}

			try {
				return (V) te;
			} catch (ClassCastException exception) {
				ArcaneArchives.logger.error("Attempted to cast to an invalid tile entity: " + te.getClass(), new IllegalArgumentException());
				return null;
			}
		}

		@SideOnly(Side.CLIENT)
		void processMessage (T message, MessageContext ctx, @Nullable V tile);
	}

	public static abstract class ConfigServerHandler<T extends ConfigPacket<?>> implements ServerHandler<T> {

		@Override
		public void processMessage (T message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			if (server == null) {
				ArcaneArchives.logger.error("Server was null when processing sync packet");
				return;
			}

			EntityPlayerMP player = ctx.getServerHandler().player;

			ServerNetwork network = DataHelper.getServerNetwork(player.getUniqueID());
			if (network == null) {
				ArcaneArchives.logger.error("Network was null when processing sync packet for " + player.getUniqueID());
				return;
			}

			configValueChanged(network, message, ctx);
		}

		public abstract void configValueChanged (ServerNetwork network, T message, MessageContext ctx);
	}

}
