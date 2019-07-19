package com.aranaira.arcanearchives.events;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.tileentities.unused.MatrixCoreTileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ServerTickHandler {
	private static final List<ImmanenceTileEntity> incomingITEs = new ArrayList<>();
	private static final List<ImmanenceTileEntity> outgoingITEs = new ArrayList<>();
	private static final List<ImmanenceTileEntity> limitedITEs = new ArrayList<>();

	public static void incomingITE (ImmanenceTileEntity entity) {
		incomingITEs.add(entity);
	}

	@SubscribeEvent
	public static void onServerTick (TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}

		List<ImmanenceTileEntity> consumed = new ArrayList<>();

		if (!limitedITEs.isEmpty()) {
			for (ImmanenceTileEntity ite : limitedITEs) {
				UUID networkId = ite.networkId;
				if (networkId == null || networkId.equals(DataHelper.INVALID)) {
					continue;
				}

				ServerNetwork network = DataHelper.getServerNetwork(networkId, ite.getWorld());
				if (network == null) {
					continue;
				}

				if (ite.uuid != null && !network.isSafe(ite.uuid)) {
					outgoingITE(ite);
				}

				consumed.add(ite);
			}
		}

		limitedITEs.removeAll(consumed);
		consumed.clear();

		for (ImmanenceTileEntity ite : incomingITEs) {
			if (ite.ticks() > 30) {
				outgoingITE(ite);
				ArcaneArchives.logger.debug(String.format("Tile entity with the class %s spent 30 ticks in the queue and is being discarded.", ite.getClass().getName()));
			} else {
				UUID networkId = ite.networkId;
				if (networkId == null || networkId.equals(DataHelper.INVALID)) {
					ite.tick();
					continue;
				}

				ServerNetwork network = DataHelper.getServerNetwork(networkId, ite.getWorld());
				if (network == null) {
					continue;
				}

				ite.tryGenerateUUID();

				if (!network.containsTile(ite)) {
					network.addTile(ite);
				}

				if (ite instanceof RadiantResonatorTileEntity || ite instanceof MatrixCoreTileEntity) {
					limitedITE(ite);
				}
			}

			consumed.add(ite);
		}

		incomingITEs.removeAll(consumed);

		for (ImmanenceTileEntity ite : outgoingITEs) {
			if (ite.isInvalid()) {
				continue;
			}

			ite.breakBlock();
		}

		outgoingITEs.clear();
	}

	private static void limitedITE (ImmanenceTileEntity entity) {
		limitedITEs.add(entity);
	}

	private static void outgoingITE (ImmanenceTileEntity entity) {
		outgoingITEs.add(entity);
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
		ServerNetwork network = DataHelper.getServerNetwork(event.player.getUniqueID(), event.player.world);
		if (network != null) {
			network.rebuildTotals();
		}
	}
}
