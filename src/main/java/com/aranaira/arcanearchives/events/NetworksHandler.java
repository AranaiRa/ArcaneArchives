package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.NetworkHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class NetworksHandler
{
	@SubscribeEvent
	public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		NetworkHelper.clearClientCache();
	}
}
