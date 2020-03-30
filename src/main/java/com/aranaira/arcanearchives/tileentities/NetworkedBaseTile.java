package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.tilenetwork.NetworkAggregator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class NetworkedBaseTile extends BaseTile {
  private UUID networkId = null;
  private String cachedNetworkId = null; // TODO: Transition this into the properly generated ID.

  public UUID getNetworkId() {
    return networkId;
  }

  public String getCachedNetwork() {
    if (cachedNetworkId == null) {
      if (networkId == null) {
        return "";
      }

      cachedNetworkId = networkId.toString();
    }
    return cachedNetworkId;
  }

  public void setNetworkId(UUID networkId) {
    if (this.networkId != null && this.networkId != networkId) {
      ArcaneArchives.logger.debug("Just changed the network ID of tile " + getCachedUUID() + " from " + getCachedNetwork() + " to " + networkId.toString());
    }
    this.networkId = networkId;
  }

  @Nullable
  public Network getNetwork() {
    if (networkId == null) {
      return null;
    }

    return NetworkAggregator.byId(networkId);
  }

  @Override
  protected UUID generateId() {
    Network network = getNetwork();
    if (network == null) {
      return null;
    }

    UUID tile = UUID.randomUUID();
    while (network.containsTile(tile)) {
      tile = UUID.randomUUID();
    }
    return tile;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    if (compound.hasUniqueId(Tags.networkId)) {
      networkId = compound.getUniqueId(Tags.networkId);
    }
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (networkId != null) {
      compound.setUniqueId(Tags.networkId, networkId);
    }
    return super.writeToNBT(compound);
  }

  @Override
  public void invalidate() {
    NetworkAggregator.tileLeave(this);
    super.invalidate();
  }

  @Override
  public void onLoad() {
    NetworkAggregator.tileJoin(this);
    super.onLoad();
  }

  @Override
  public void onChunkUnload() {
    NetworkAggregator.tileLeave(this);
    super.onChunkUnload();
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  public abstract void onNetworkJoined (Network network);
}
