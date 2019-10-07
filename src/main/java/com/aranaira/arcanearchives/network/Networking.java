package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.network.Handlers.BaseHandler;
import com.aranaira.arcanearchives.network.Messages.EmptyMessage;
import com.aranaira.arcanearchives.network.PacketRadiantChest.SyncChestDisplay;
import com.aranaira.arcanearchives.network.PacketRadiantChest.SyncChestName;
import com.aranaira.arcanearchives.network.PacketRadiantChest.UnsetItem;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

//Used Precision Crafting as a reference. https://github.com/Daomephsta/Precision-Crafting/blob/master/src/main/java/leviathan143/precisioncrafting/common/packets/PacketHandler.java
public class Networking {
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ArcaneArchives.NAME);
	private static int packetID = 0;

	public static void registerPackets () {
		registerPacks(PacketRadiantChest.SetName.Handler.class, PacketRadiantChest.SetName.class, Side.SERVER);
		registerPacks(PacketRadiantChest.UnsetName.Handler.class, PacketRadiantChest.UnsetName.class, Side.SERVER);
		registerPacks(PacketRadiantChest.SetItemAndFacing.Handler.class, PacketRadiantChest.SetItemAndFacing.class, Side.SERVER);
		registerPacks(PacketRadiantChest.SyncChestDisplay.Handler.class, SyncChestDisplay.class, Side.CLIENT);
		registerPacks(SyncChestName.Handler.class, SyncChestName.class, Side.CLIENT);
		registerPacks(UnsetItem.Handler.class, UnsetItem.class, Side.SERVER);
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
		registerPacks(PacketConfig.TrovesDispense.Handler.class, PacketConfig.TrovesDispense.class, Side.SERVER);
		registerPacks(PacketConfig.RequestTrovesDispense.class, Side.CLIENT);
		registerPacks(PacketBrazier.SetRadius.Handler.class, PacketBrazier.SetRadius.class, Side.SERVER);
		registerPacks(PacketBrazier.SetSubnetworkMode.Handler.class, PacketBrazier.SetSubnetworkMode.class, Side.SERVER);
		registerPacks(PacketBrazier.IncrementRadius.class, Side.SERVER);
		registerPacks(PacketBrazier.DecrementRadius.class, Side.SERVER);
		registerPacks(PacketDebug.TrackPositions.Handler.class, PacketDebug.TrackPositions.class, Side.CLIENT);
		registerPacks(PacketRadiantCrafting.SetRecipe.Handler.class, PacketRadiantCrafting.SetRecipe.class, Side.SERVER);
		registerPacks(PacketRadiantCrafting.UnsetRecipe.Handler.class, PacketRadiantCrafting.UnsetRecipe.class, Side.SERVER);
		registerPacks(PacketRadiantCrafting.TryCraftRecipe.Handler.class, PacketRadiantCrafting.TryCraftRecipe.class, Side.SERVER);
		registerPacks(PacketGemCutters.SetRecipeIndex.Handler.class, PacketGemCutters.SetRecipeIndex.class, Side.SERVER);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacks (Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}

	private static <T extends EmptyMessage<T> & BaseHandler<T>> void registerPacks (Class<T> handlerAndMessage, Side side) {
		CHANNEL.registerMessage(handlerAndMessage, handlerAndMessage, packetID, side);
		packetID++;
	}

	public static void sendToAllTracking (IMessage message, BlockPos pos, int dimension) {
		TargetPoint tp = new TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), 0);
		CHANNEL.sendToAllTracking(message, tp);
	}

	public static void sendToAllTracking (IMessage message, ImmanenceTileEntity tile) {
		sendToAllTracking(message, tile.getPos(), tile.dimension);
	}

	public static void sendToAllTracking (IMessage message, Entity entity) {
		CHANNEL.sendToAllTracking(message, entity);
	}
}
