package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// TODO: Update this for the new three things
// TODO: Work out what the new three things are
public class NetworkReferenceData extends WorldSavedData {
  public static final String PREFIX = "ArcaneArchives-NetworkReferenceData";

  private final Set<UUID> networkIds = new HashSet<>();

  public NetworkReferenceData() {
    this(PREFIX);
  }

  public NetworkReferenceData(String name) {
    super(name);
  }

  public Set<UUID> getAllNetworks () {
    return ImmutableSet.copyOf(networkIds);
  }

  public boolean containsNetwork(Network network) {
    return containsNetwork(network.getNetworkId());
  }

  public boolean containsNetwork(UUID uuid) {
    return networkIds.contains(uuid);
  }

  public void addNetwork(Network network) {
    addNetwork(network.getNetworkId());
  }

  public void addNetwork(UUID uuid) {
    networkIds.add(uuid);
    markDirty();
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    networkIds.clear();
    NBTTagList ids = nbt.getTagList(Tags.NetworkReference.networkIds, Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < ids.tagCount(); i++) {
      networkIds.add(NBTUtil.getUUIDFromTag(ids.getCompoundTagAt(i)));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagList ids = new NBTTagList();
    for (UUID uuid : networkIds) {
      ids.appendTag(NBTUtil.createUUIDTag(uuid));
    }
    compound.setTag(Tags.NetworkReference.networkIds, ids);
    return compound;
  }
}
