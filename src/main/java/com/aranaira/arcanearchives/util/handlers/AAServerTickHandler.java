package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class AAServerTickHandler
{
	private static final List<ImmanenceTileEntity> incomingITEs = new ArrayList<>();

	public static void incomingITE(ImmanenceTileEntity entity)
	{
		incomingITEs.add(entity);
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END) return;

		if(incomingITEs.size() == 0) return;

		List<ImmanenceTileEntity> consumed = new ArrayList<>();

		for(ImmanenceTileEntity ite : incomingITEs)
		{
			UUID networkId = ite.GetNetworkID();
			if(networkId == null) continue;

			ArcaneArchivesNetwork network = NetworkHelper.getArcaneArchivesNetwork(networkId);

			if(!network.NetworkContainsTile(ite))
			{
				network.AddTileToNetwork(ite);
			}

			consumed.add(ite);
		}

		incomingITEs.removeAll(consumed);
	}
}
