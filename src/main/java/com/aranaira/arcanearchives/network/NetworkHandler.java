package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.types.IteRef;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

//Used Precision Crafting as a reference. https://github.com/Daomephsta/Precision-Crafting/blob/master/src/main/java/leviathan143/precisioncrafting/common/packets/PacketHandler.java
public class NetworkHandler {
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ArcaneArchives.NAME);
	private static int packetID = 0;

	public static void registerPackets () {
		registerPacks(PacketRadiantChest.SetName.Handler.class, PacketRadiantChest.SetName.class, Side.SERVER);
		registerPacks(PacketGemCutters.ChangeRecipe.Handler.class, PacketGemCutters.ChangeRecipe.class, Side.SERVER);
		registerPacks(PacketGemCutters.LastRecipe.Handler.class, PacketGemCutters.LastRecipe.class, Side.CLIENT);
		registerPacks(PacketNetworks.HiveResponse.Handler.class, PacketNetworks.HiveResponse.class, Side.CLIENT);
		registerPacks(PacketNetworks.DataResponse.Handler.class, PacketNetworks.DataResponse.class, Side.CLIENT);
		registerPacks(PacketNetworks.ManifestResponse.Handler.class, PacketNetworks.ManifestResponse.class, Side.CLIENT);
		registerPacks(PacketNetworks.Request.Handler.class, PacketNetworks.Request.class, Side.SERVER);
		registerPacks(PacketRadiantCrafting.LastRecipe.Handler.class, PacketRadiantCrafting.LastRecipe.class, Side.CLIENT);
		registerPacks(PacketConfig.MaxDistance.Handler.class, PacketConfig.MaxDistance.class, Side.SERVER);
		registerPacks(PacketConfig.RequestMaxDistance.class, Side.CLIENT);
		registerPacks(PacketRadiantAmphora.Toggle.class, Side.SERVER);
		registerPacks(PacketArcaneGems.GemParticle.Handler.class, PacketArcaneGems.GemParticle.class, Side.CLIENT);
		registerPacks(PacketArcaneGems.Toggle.class, Side.SERVER);
		registerPacks(PacketArcaneGems.OpenSocket.class, PacketArcaneGems.OpenSocket.class, Side.SERVER);
		registerPacks(PacketRadiantChest.MessageClickWindowExtended.Handler.class, PacketRadiantChest.MessageClickWindowExtended.class, Side.CLIENT);
		registerPacks(PacketRadiantChest.MessageSyncExtendedSlotContents.Handler.class, PacketRadiantChest.MessageSyncExtendedSlotContents.class, Side.CLIENT);
		registerPacks(PacketRadiantChest.ToggleBrazier.Handler.class, PacketRadiantChest.ToggleBrazier.class, Side.SERVER);
		registerPacks(PacketArcaneGems.RequestRecharge.class, Side.SERVER);
		registerPacks(PacketClipboard.CopyToClipboard.Handler.class, PacketClipboard.CopyToClipboard.class, Side.CLIENT);
		registerPacks(PacketConfig.RequestDefaultRoutingType.class, Side.CLIENT);
		registerPacks(PacketConfig.DefaultRoutingType.Handler.class, PacketConfig.DefaultRoutingType.class, Side.SERVER);
		registerPacks(PacketBrazier.SetRadius.Handler.class, PacketBrazier.SetRadius.class, Side.SERVER);
		registerPacks(PacketBrazier.SetSubnetworkMode.Handler.class, PacketBrazier.SetSubnetworkMode.class, Side.SERVER);
		registerPacks(PacketBrazier.IncrementRadius.class, Side.SERVER);
		registerPacks(PacketBrazier.DecrementRadius.class, Side.SERVER);

	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacks (Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}

	private static <T extends EmptyMessage<T> & BaseHandler<T>> void registerPacks (Class<T> handlerAndMessage, Side side) {
		CHANNEL.registerMessage(handlerAndMessage, handlerAndMessage, packetID, side);
		packetID++;
	}

	public interface BaseHandler<T extends IMessage> extends IMessageHandler<T, IMessage> {
		void processMessage (T message, MessageContext ctx);
	}

	public interface ServerHandler<T extends IMessage> extends BaseHandler<T> {
		default IMessage onMessage (T message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}
	}

	public interface TileHandlerServer<T extends TileMessage> extends ServerHandler<T> {
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

			ServerNetwork network = NetworkHelper.getServerNetwork(networkId, player.world);

			IteRef ref = network.getTiles().getReference(message.getTileId());
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

	public interface EmptyMessage<T extends IMessage> extends IMessage, BaseHandler<T> {
		default void fromBytes (ByteBuf buf) {
		}

		@Override
		default void toBytes (ByteBuf buf) {
		}
	}

	public interface EmptyMessageServer<T extends IMessage> extends EmptyMessage<T>, ServerHandler<T> {
	}

	public interface EmptyMessageClient<T extends IMessage> extends EmptyMessage<T>, ClientHandler<T> {

	}

	public static abstract class EmptyTileMessageServer<T extends TileMessage> extends TileMessage implements TileHandlerServer<T>, EmptyMessage<T> {
		public EmptyTileMessageServer () {
		}

		public EmptyTileMessageServer (UUID tileId) {
			super(tileId);
		}
	}
}
