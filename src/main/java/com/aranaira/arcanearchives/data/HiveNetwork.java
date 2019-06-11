package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.ManifestTileEntity;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTileEntity;
import com.aranaira.arcanearchives.util.ItemStackConsolidator;
import com.aranaira.arcanearchives.util.LargeItemNBTUtil;
import com.aranaira.arcanearchives.util.types.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.*;

public class HiveNetwork implements IHiveBase {
	private List<ServerNetwork> memberNetworks;
	private ServerNetwork ownerNetwork;

	public HiveNetwork (ServerNetwork ownerNetwork, List<ServerNetwork> memberNetworks) {
		this.ownerNetwork = ownerNetwork;
		this.memberNetworks = memberNetworks;
	}

	// TODO: ????
	private List<ServerNetwork> getCombinedNetworks () {
		List<ServerNetwork> combined = new ArrayList<>(getContainedNetworks());
		combined.add(ownerNetwork);
		return combined;
	}

	@Nullable
	@Override
	public ServerNetwork getOwnerNetwork () {
		return ownerNetwork;
	}

	@Nullable
	@Override
	public HiveNetwork getHiveNetwork () {
		return this;
	}

	@Override
	public boolean isHiveMember () {
		return true;
	}

	@Override
	public boolean isHiveNetwork () {
		return true;
	}

	@Nullable
	@Override
	public List<ServerNetwork> getContainedNetworks () {
		return memberNetworks;
	}

	@Nullable
	public ServerNetwork getContainedNetwork (EntityPlayer player) {
		for (ServerNetwork network : getCombinedNetworks()) {
			if (network.getUuid().equals(player.getUniqueID())) return network;
		}

		return null;
	}

	@Override
	public NBTTagCompound buildHiveManifest (EntityPlayer player) {
		ManifestList manifestItems = new ManifestList(new ArrayList<>());

		ServerNetwork playerNetwork = getContainedNetwork(player);
		assert playerNetwork != null;

		// already squared
		int maxDistance = playerNetwork.getMaxDistance();

		List<ManifestItemEntry> preManifest = new ArrayList<>();
		Set<ManifestTileEntity> done = new HashSet<>();
		Set<BlockPosDimension> positions = new HashSet<>();

		for (ServerNetwork network : getCombinedNetworks()) {
			for (IteRef ref : network.getManifestTileEntities()) {
				ManifestTileEntity ite = ref.getManifestServerTile();
				if (ite == null) {
					continue;
				}

				if (done.contains(ite)) {
					continue;
				}

				int distance = ownerNetwork.distanceSq(ite.getPos(), player.getPosition());
				boolean outOfRange = distance >= maxDistance;

				int dimId = ite.getWorld().provider.getDimension();

				if (ite.isSingleStackInventory()) {
					ItemStack is = ite.getSingleStack();
					if (!is.isEmpty()) {
						preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(ite.getPos(), ite.getChestName(), is.getCount()), outOfRange));
					}
				} else {
					if (ite instanceof MonitoringCrystalTileEntity) {
						MonitoringCrystalTileEntity mte = (MonitoringCrystalTileEntity) ite;

						BlockPos tar = mte.getTarget();
						if (tar == null) {
							continue;
						}

						BlockPosDimension ttar = new BlockPosDimension(tar, mte.dimension);

						if (positions.contains(ttar)) {
							if (player != null) {
								player.sendMessage(new TextComponentTranslation("arcanearchives.error.monitoring_crystal", tar.getX(), tar.getY(), tar.getZ(), ttar.dimension));
							} else {
								ArcaneArchives.logger.error("Multiple Monitoring Crystals were found for hive network " + ownerNetwork.getUuid().toString() + " targeting " + String.format("%d/%d/%d in dimension %d", tar.getX(), tar.getY(), tar.getZ(), ttar.dimension));
							}
							continue;
						}

						positions.add(ttar);

						IItemHandler handler = mte.getInventory();
						if (handler != null) {
							for (ItemStack is : new SlotIterable(handler)) {
								if (is.isEmpty()) {
									continue;
								}

								preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(mte.getTarget(), mte.getDescriptor(), is.getCount()), outOfRange));
							}
						}
					} else {
						for (ItemStack is : new SlotIterable(ite.getInventory())) {
							if (is.isEmpty()) {
								continue;
							}

							preManifest.add(new ManifestItemEntry(is.copy(), dimId, new ManifestEntry.ItemEntry(ite.getPos(), ite.getChestName(), is.getCount()), outOfRange));
						}
					}
				}

				done.add(ite);
			}
		}

		List<ManifestEntry> consolidated = ItemStackConsolidator.ConsolidateManifest(preManifest, maxDistance);
		manifestItems.addAll(consolidated);

		NBTTagList manifest = new NBTTagList();

		for (ManifestEntry entry : manifestItems) {
			NBTTagCompound itemEntry = new NBTTagCompound();
			LargeItemNBTUtil.writeToNBT(itemEntry, entry.getStack());
			NBTTagList entries = new NBTTagList();
			for (ManifestEntry.ItemEntry iEntry : entry.getEntries()) {
				entries.appendTag(iEntry.serializeNBT());
			}
			itemEntry.setTag(NetworkTags.ENTRIES, entries);
			itemEntry.setInteger(NetworkTags.DIMENSION, entry.getDimension());
			itemEntry.setBoolean(NetworkTags.OUT_OF_RANGE, entry.outOfRange);
			manifest.appendTag(itemEntry);
		}

		NBTTagCompound result = new NBTTagCompound();
		result.setTag(NetworkTags.MANIFEST, manifest);

		return result;
	}
}
