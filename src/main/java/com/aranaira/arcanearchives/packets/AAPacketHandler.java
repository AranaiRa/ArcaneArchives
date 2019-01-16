package com.aranaira.arcanearchives.packets;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.packets.PacketChangeRadiantChestNameClient.PacketChangeRadiantChestNameClientHandler;

import net.minecraft.entity.player.EntityPlayerMP;
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
        registerPacks(PacketRadiantChestsListResponse.PacketRadiantChestsListResponseHandler.class, PacketRadiantChestsListResponse.class, Side.CLIENT);
        registerPacks(SetRadiantChestName.SetRadiantChestNameHandler.class, SetRadiantChestName.class, Side.SERVER);
        registerPacks(PacketChangeRadiantChestNameClient.PacketChangeRadiantChestNameClientHandler.class, PacketChangeRadiantChestNameClient.class, Side.CLIENT);
        registerPacks(PacketGCTChangePage.PacketGCTChangePageHandler.class, PacketGCTChangePage.class, Side.SERVER);
	}
	
	
	
	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacks(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}
	
}
