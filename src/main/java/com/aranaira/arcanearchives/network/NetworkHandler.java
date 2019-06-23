package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import sun.plugin2.message.Message;

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
		registerPacks(PacketRadiantAmphora.Handler.class, PacketRadiantAmphora.class, Side.SERVER);
		registerPacks(PacketConfig.MaxDistance.Handler.class, PacketConfig.MaxDistance.class, Side.SERVER);
		registerPacks(PacketConfig.RequestMaxDistance.Handler.class, PacketConfig.RequestMaxDistance.class, Side.CLIENT);
		registerPacks(PacketRadiantAmphora.Handler.class, PacketRadiantAmphora.class, Side.SERVER);
		registerPacks(PacketArcaneGem.Handler.class, PacketArcaneGem.class, Side.CLIENT);
		registerPacks(PacketArcaneGemToggle.Handler.class, PacketArcaneGemToggle.class, Side.SERVER);
		registerPacks(PacketGemSocket.Handler.class, PacketGemSocket.class, Side.SERVER);
		registerPacks(MessageClickWindowExtended.Handler.class, MessageClickWindowExtended.class, Side.CLIENT);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacks (Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}

	public static abstract class BaseHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
		@Override
		public abstract IMessage onMessage (T message, MessageContext ctx);

		public abstract void processMessage (T message, MessageContext ctx);
	}

	public static abstract class ServerHandler<T extends IMessage> extends BaseHandler<T> {
		@Override
		public IMessage onMessage (T message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}
	}

	public static abstract class ClientHandler<T extends IMessage> extends BaseHandler<T> {
		@Override
		public IMessage onMessage (T message, MessageContext ctx) {
			ArcaneArchives.proxy.scheduleTask(() -> processMessage(message, ctx), Side.CLIENT);

			return null;
		}
	}
}
