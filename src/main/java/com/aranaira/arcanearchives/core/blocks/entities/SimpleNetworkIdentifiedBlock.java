package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.block.entity.INetworkedBlockEntity;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;
import java.util.UUID;

public class SimpleNetworkIdentifiedBlock extends TileEntity implements INetworkedBlockEntity {
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
  public UUID getNetworkId() {
    return null;
  }

  @Nullable
  @Override
  public UUIDNameData.Name getNetworkName() {
    return UUIDNameData.Name.EMPTY;
  }

  @Override
  public boolean isNetworkUnknown() {
    return false;
  }

  @Override
  public TileEntity getBlockEntity() {
    return this;
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (networkId != null) {
      compound.putUUID(Identifiers.networkId, networkId);
    }
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.networkId)) {
      this.networkId = compound.getUUID(Identifiers.networkId);
    } else {
      // TODO: Should we be generating a unique ID here?
      this.networkId = UNKNOWN;
    }

    super.load(state, compound);
  }
}
