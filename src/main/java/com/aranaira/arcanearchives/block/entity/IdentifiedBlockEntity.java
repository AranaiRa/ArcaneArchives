package com.aranaira.arcanearchives.block.entity;

import com.aranaira.arcanearchives.api.block.entity.IIdentifiedBlockEntity;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class IdentifiedBlockEntity extends TileEntity implements IIdentifiedBlockEntity {
  protected UUID entityId = null;
  protected UUIDNameData.Name entityName = null;

  public IdentifiedBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (entityId != null) {
      compound.putUUID(Identifiers.entityId, entityId);
    }
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.entityId)) {
      this.entityId = compound.getUUID(Identifiers.entityId);
    } else {
      // TODO: Should we be generating a unique ID here?
      this.entityId = UNKNOWN;
    }

    super.load(state, compound);
  }

  @Override
  public UUID getEntityId() {
    if (isBlockUnknown()) {
      this.entityId = UUID.randomUUID();
    }
    return entityId;
  }

  @Nullable
  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getBlockPos(), 9, getUpdateTag());
  }

  @Override
  public CompoundNBT getUpdateTag() {
    CompoundNBT data = super.getUpdateTag();
    UUID id = getEntityId();
    if (id != null) {
      data.putUUID(Identifiers.entityId, id);
      UUIDNameData.Name name = getEntityName();
      if (!name.isEmpty()) {
        data.put(Identifiers.entityName, name.serializeNBT());
      }
    }
    return data;
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    if (pkt.getType() == 9) {
      CompoundNBT tag = pkt.getTag();
      if (tag.hasUUID(Identifiers.entityId)) {
        entityId = pkt.getTag().getUUID(Identifiers.entityId);
      }
      if (tag.contains(Identifiers.entityName)) {
        this.entityName = UUIDNameData.Name.fromNBT(tag.getCompound(Identifiers.entityName));
      }
    }

    super.onDataPacket(net, pkt);
  }

  @Override
  public TileEntity getBlockEntity() {
    return this;
  }

  @Override
  public UUIDNameData.Name getEntityName() {
    if (entityName == null) {
      LogicalSide side = EffectiveSide.get();
      if (side.isClient()) {
        return UUIDNameData.Name.EMPTY;
      } else {
        if (getEntityId() == null) {
          return UUIDNameData.Name.EMPTY;
        }
        entityName = DataStorage.getEntityName(getEntityId());
      }
    }

    return entityName;
  }

  @Override
  public boolean isBlockUnknown() {
    return entityId == null || entityId == UNKNOWN;
  }
}
