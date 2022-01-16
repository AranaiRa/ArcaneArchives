package com.aranaira.arcanearchives.block.entity;

import com.aranaira.arcanearchives.api.block.entity.IDomainBlockEntity;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleNetworkIdentifiedBlock extends TileEntity implements IDomainBlockEntity {
  private UUID networkId;

  public SimpleNetworkIdentifiedBlock(TileEntityType<?> type) {
    super(type);
  }

  @Nullable
  @Override
  public UUID getEntityId() {
    return networkId;
  }

  @Override
  public UUIDNameData.Name getEntityName() {
    return UUIDNameData.Name.EMPTY;
  }

  @Override
  public boolean isBlockUnknown() {
    return false;
  }

  @Nullable
  @Override
  public UUID getDomainId() {
    return null;
  }

  @Nullable
  @Override
  public UUIDNameData.Name getDomainName() {
    return UUIDNameData.Name.EMPTY;
  }

  @Override
  public boolean isDomainUnknown() {
    return false;
  }

  @Override
  public TileEntity getBlockEntity() {
    return this;
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (networkId != null) {
      compound.putUUID(Identifiers.domainId, networkId);
    }
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.domainId)) {
      this.networkId = compound.getUUID(Identifiers.domainId);
    } else {
      // TODO: Should we be generating a unique ID here?
      this.networkId = UNKNOWN;
    }

    super.load(state, compound);
  }
}
