package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.data.client.ClientNetworkData;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;

public class ServerNetworkData extends WorldSavedData implements ISerializePacketBuffer<ClientNetworkData> {
  public static final String PREFIX = "ArcaneArchives-ServerNetworkData-";

  private UUID networkId;

  public static String ID(Network network) {
    return PREFIX + network.getCachedNetworkId();
  }

  public static String ID (UUID uuid) {
    return PREFIX + uuid;
  }

  public ServerNetworkData(String string) {
    super(string);
    String[] pieces = string.split("-");
    if (pieces.length != 3) {
      throw new IllegalStateException("Attempted to create ServerNetworkData without a valid identifier: " + string);
    }
    try {
      this.networkId = UUID.fromString(pieces[2]);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Attempted to create ServerNetworkData without a valid identifier: " + string, e);
    }
  }

  public ServerNetworkData(Network network) {
    super(ID(network));
    this.networkId = network.getNetworkId();
  }

  public ServerNetworkData (UUID uuid) {
    super (ID(uuid));
    this.networkId = uuid;
  }

  public ServerNetworkData() {
    super("");
    throw new IllegalStateException("Attempted to create ServerNetworkData without a Network reference.");
  }

  public UUID getNetworkId() {
    return networkId;
  }

  public void setNetworkId(UUID networkId) {
    this.networkId = networkId;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    this.networkId = nbt.getUniqueId(Tags.networkId);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setUniqueId(Tags.networkId, this.networkId);
    return compound;
  }

  @Override
  public ClientNetworkData fromPacket(PacketBuffer buf) {
    return ClientNetworkData.fromServer(this).fromPacket(buf);
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    ClientNetworkData.fromServer(this).toPacket(buf);
  }
}
