package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

//Used Precision Crafting as a reference. https://github.com/Daomephsta/Precision-Crafting/blob/master/src/main/java/leviathan143/precisioncrafting/common/packets/PacketHandler.java
public class NetworkHandler {
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ArcaneArchives.NAME);
	private static int packetID = 0;

	public static void registerPackets () {
		registerPacks(PacketRadiantChest.SetName.Handler.class, PacketRadiantChest.SetName.class, Side.SERVER);
		registerPacks(PacketGemCutters.ChangeRecipe.Handler.class, PacketGemCutters.ChangeRecipe.class, Side.SERVER);
		registerPacks(PacketGemCutters.LastRecipe.Handler.class, PacketGemCutters.LastRecipe.class, Side.CLIENT);
		registerPacks(PacketNetworks.Response.Handler.class, PacketNetworks.Response.class, Side.CLIENT);
		registerPacks(PacketNetworks.Request.Handler.class, PacketNetworks.Request.class, Side.SERVER);
		registerPacks(PacketRadiantCrafting.LastRecipe.Handler.class, PacketRadiantCrafting.LastRecipe.class, Side.CLIENT);
		registerPacks(PacketConfig.MaxDistance.Handler.class, PacketConfig.MaxDistance.class, Side.SERVER);
		registerPacks(PacketConfig.RequestMaxDistance.class, PacketConfig.RequestMaxDistance.class, Side.CLIENT);
		registerPacks(PacketRadiantAmphora.Toggle.class, Side.SERVER);
		registerPacks(PacketArcaneGems.GemParticle.Handler.class, PacketArcaneGems.GemParticle.class, Side.CLIENT);
		registerPacks(PacketArcaneGems.Toggle.class, Side.SERVER);
		registerPacks(PacketArcaneGems.OpenSocket.class, PacketArcaneGems.OpenSocket.class, Side.SERVER);
		registerPacks(PacketRadiantChest.MessageClickWindowExtended.Handler.class, PacketRadiantChest.MessageClickWindowExtended.class, Side.CLIENT);
		registerPacks(PacketRadiantChest.MessageSyncExtendedSlotContents.Handler.class, PacketRadiantChest.MessageSyncExtendedSlotContents.class, Side.CLIENT);
		registerPacks(PacketRadiantChest.ToggleBrazier.Handler.class, PacketRadiantChest.ToggleBrazier.class, Side.SERVER);
		registerPacks(PacketArcaneGems.RequestRecharge.class, Side.SERVER);
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
		public abstract void processMessage (T message, MessageContext ctx);
	}

	public interface ServerHandler<T extends IMessage> extends BaseHandler<T> {
		default IMessage onMessage (T message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}
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
}
