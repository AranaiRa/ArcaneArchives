package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.blockentities.INetworkedBlockEntity;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class NetworkIdentifiedBlockEntity extends IdentifiedBlockEntity implements INetworkedBlockEntity {
  protected UUID networkId = null;
  protected UUIDNameData.Name networkName = null;

  public NetworkIdentifiedBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (getNetworkId() != null && networkId != UNKNOWN) {
      compound.putUUID(Identifiers.networkId, getNetworkId());
    }
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.networkId)) {
      this.networkId = compound.getUUID(Identifiers.networkId);
    }
    super.load(state, compound);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    CompoundNBT updateTag = super.getUpdateTag();
    if (getNetworkId() == null) {
      return updateTag;
    }

    //noinspection ConstantConditions
    UUID networkId = getNetworkId();
    updateTag.putUUID(Identifiers.networkId, networkId);
    UUIDNameData.Name name = getNetworkName();
    if (name != null && !name.isEmpty()) {
      updateTag.put(Identifiers.networkName, name.serializeNBT());
    }
    return updateTag;
  }

  @Nullable
  @Override
  public UUID getNetworkId() {
    return networkId;
  }

  public void setNetworkId(UUID id) {
    this.networkId = id;
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    if (pkt.getType() == 9) {
      CompoundNBT tag = pkt.getTag();
      if (tag.hasUUID(Identifiers.networkId)) {
        this.networkId = pkt.getTag().getUUID(Identifiers.networkId);
      }
      if (tag.contains(Identifiers.networkName)) {
        this.networkName = UUIDNameData.Name.fromNBT(tag.getCompound(Identifiers.networkName));
      }
    }

    super.onDataPacket(net, pkt);
  }

  @Override
  public UUIDNameData.Name getNetworkName() {
    if (networkName == null) {
      LogicalSide side = EffectiveSide.get();
      if (side.isClient()) {
        return UUIDNameData.Name.EMPTY;
      } else {
        if (getNetworkId() == null) {
          return UUIDNameData.Name.EMPTY;
        }
        networkName = DataStorage.getTileName(getNetworkId());
      }
    }

    return networkName;
  }

  @Override
  public boolean isNetworkUnknown() {
    return uuid == null || uuid == UNKNOWN;
  }
}
