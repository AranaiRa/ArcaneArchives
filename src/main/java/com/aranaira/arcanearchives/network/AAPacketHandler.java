package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

//Used Precision Crafting as a reference. https://github.com/Daomephsta/Precision-Crafting/blob/master/src/main/java/leviathan143/precisioncrafting/common/packets/PacketHandler.java
public class AAPacketHandler
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ArcaneArchives.NAME);
	private static int packetID = 0;

	public static void registerPackets()
	{
		registerPacks(PacketNetworkInteraction.PacketNetworkInteractionHandler.class, PacketNetworkInteraction.class, Side.SERVER);
		registerPacks(PacketRadiantChest.SetName.SetNameHandler.class, PacketRadiantChest.SetName.class, Side.SERVER);
		registerPacks(PacketGemCutters.ChangeRecipe.Handler.class, PacketGemCutters.ChangeRecipe.class, Side.SERVER);
		registerPacks(PacketGemCutters.Consume.ConsumeHandler.class, PacketGemCutters.Consume.class, Side.SERVER);
		registerPacks(PacketNetwork.PacketSynchroniseResponse.PacketSynchroniseResponseHandler.class, PacketNetwork.PacketSynchroniseResponse.class, Side.CLIENT);
		registerPacks(PacketNetwork.PacketSynchroniseRequest.PacketSynchroniseRequestHandler.class, PacketNetwork.PacketSynchroniseRequest.class, Side.SERVER);
		registerPacks(PacketRadiantCrafting.LastRecipe.Handler.class, PacketRadiantCrafting.LastRecipe.class, Side.CLIENT);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacks(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}

	public static abstract class Handler<T extends IMessage> implements IMessageHandler<T, IMessage> {
		@Override
		public IMessage onMessage(T message, MessageContext ctx)
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> processMessage(message, ctx));

			return null;
		}

		public abstract void processMessage (T message, MessageContext ctx);
	}
}
