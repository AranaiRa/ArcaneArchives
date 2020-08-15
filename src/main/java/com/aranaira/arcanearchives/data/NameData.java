package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.client.ClientNameData;
import com.aranaira.arcanearchives.tilenetwork.NetworkName;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

// TODO: Update this for the new three things
public class NameData extends WorldSavedData implements ISerializePacketBuffer<ClientNameData> {
  public static final String PREFIX = "ArcaneArchives-NameData";
  public static final Random random = new Random();
  public static int TOTAL_SEGMENTS = 36;

  private Map<UUID, NetworkName> map = new HashMap<>();
  private Set<int[]> usedNames = new HashSet<>();

  public NameData () {
    this(PREFIX);
  }

  public NameData(String name) {
    super(name);
  }

  public Map<UUID, NetworkName> getData () {
    return ImmutableMap.copyOf(map);
  }

  public void generateNames () {
    for (UUID uuid : DataHelper.NetworkReference.getAllNetworks()) {
      getOrGenerateName(uuid);
    }
  }

  public NetworkName getOrGenerateName(UUID id) {
    NetworkName name = map.get(id);
    if (name != null) {
      return name;
    }

    for (int tries = 100; tries > 0; tries--) {
      // TODO
      int[] triplet = new int[]{random.nextInt(TOTAL_SEGMENTS), random.nextInt(TOTAL_SEGMENTS), random.nextInt(TOTAL_SEGMENTS)};
      if (usedNames.contains(triplet)) {
        continue;
      }

      map.put(id, new NetworkName(triplet));
      markDirty();
      Objects.requireNonNull(DataHelper.getWorld().getMapStorage()).saveAllData();
      return map.get(id);
    }

    ArcaneArchives.logger.error("Unable to generate a network name for network ID " + id + " after 100 itertions");

    return null;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    map.clear();

    for (String key : nbt.getKeySet()) {
      UUID thisUuid = UUID.fromString(key);
      int[] triplet = nbt.getIntArray(key);
      usedNames.add(triplet);
      map.put(thisUuid, new NetworkName(triplet[0], triplet[1], triplet[2]));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    for (Map.Entry<UUID, NetworkName> entry : map.entrySet()) {
      compound.setIntArray(entry.getKey().toString(), entry.getValue().asArray());
    }

    return compound;
  }

  @Override
  public ClientNameData fromPacket(PacketBuffer buf) {
    throw new IllegalStateException("There should never be a NameData::fromPacket call.");
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    ClientNameData.fromServer(this).toPacket(buf);
  }
}
