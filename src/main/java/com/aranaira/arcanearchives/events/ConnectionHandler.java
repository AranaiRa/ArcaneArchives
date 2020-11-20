/*package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.events.ticks.ClientTickHandler;
import com.aranaira.arcanearchives.events.ticks.ServerTickHandler;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketNetwork;
import com.aranaira.arcanearchives.tilenetwork.PlayerNetworkConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ConnectionHandler {
  @SubscribeEvent
  public static void onClientConnectedClient(FMLNetworkEvent.ClientConnectedToServerEvent event) {
    if (event.isLocal()) {
      ClientTickHandler.addRunnable(() -> {
        PacketNetwork.ConfigMessage message = new PacketNetwork.ConfigMessage(PlayerNetworkConfig.fromClient());
        Networking.CHANNEL.sendToServer(message);
      }, 40);
    }
  }

  @SubscribeEvent
  public static void onClientConnectedServer(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
    ServerTickHandler.addRunnable(PacketNetwork.NameMessage::sendToAll, 60);
  }
}*/
