package com.aranaira.arcanearchives.util.handlers;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.network.AAPacketHandler;
import com.aranaira.arcanearchives.network.PacketNetwork;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class AAServerTickHandler
{
	private static final List<ImmanenceTileEntity> incomingITEs = new ArrayList<>();
	private static final List<ImmanenceTileEntity> outgoingITEs = new ArrayList<>();

	public static void incomingITE(ImmanenceTileEntity entity)
	{
		incomingITEs.add(entity);
	}

	public static void outgoingITE(ImmanenceTileEntity entity)
	{
		outgoingITEs.add(entity);
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END) return;

		List<ImmanenceTileEntity> consumed = new ArrayList<>();

		for(ImmanenceTileEntity ite : incomingITEs)
		{
			if(ite.ticks() > 30)
			{
				outgoingITE(ite);
			} else
			{
				UUID networkId = ite.networkID;
				if(networkId == null || networkId.equals(NetworkHelper.INVALID))
				{
					ite.tick();
					continue;
				}

				ServerNetwork network = NetworkHelper.getServerNetwork(networkId, ite.getWorld());
				if(network == null) continue;

				if(!network.NetworkContainsTile(ite))
				{
					network.AddTileToNetwork(ite);
				}
			}

			consumed.add(ite);
		}

		incomingITEs.removeAll(consumed);

		for(ImmanenceTileEntity ite : outgoingITEs)
		{
			ite.breakBlock();
		}

		outgoingITEs.clear();
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
		ServerNetwork network = NetworkHelper.getServerNetwork(event.player.getUniqueID(), event.player.world);
		if (network != null)
		{
			network.rebuildTotals();
		}
	}
}
