package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.blockentities.IIdentifiedBlockEntity;
import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.api.blockentities.IArcaneArchivesBlockEntity;
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
  public static final UUID UNKNOWN = UUID.fromString("981dd3f2-f0f7-43cf-98bb-b14d8726057b");
  protected UUID uuid = null;
  protected UUIDNameData.Name tileName = null;

  public IdentifiedBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (uuid != null) {
      compound.putUUID(Identifiers.tileId, uuid);
    }
    return super.save(compound);
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.tileId)) {
      setUuid(compound.getUUID(Identifiers.tileId));
    } else {
      // TODO: Should we be generating a unique ID here?
      setUuid(UNKNOWN);
    }

    super.load(state, compound);
  }

  @Override
  public UUID getEntityId() {
    if (this.uuid == null || this.uuid == UNKNOWN) {
      setUuid(UUID.randomUUID());
    }
    return uuid;
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
      data.putUUID(Identifiers.tileId, id);
      UUIDNameData.Name name = getEntityName();
      if (!name.isEmpty()) {
        data.put(Identifiers.tileName, name.serializeNBT());
      }
    }
    return data;
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    if (pkt.getType() == 9) {
      CompoundNBT tag = pkt.getTag();
      if (tag.hasUUID(Identifiers.tileId)) {
        setUuid(pkt.getTag().getUUID(Identifiers.tileId));
      }
      if (tag.contains(Identifiers.tileName)) {
        this.tileName = UUIDNameData.Name.fromNBT(tag.getCompound(Identifiers.tileName));
      }
    }

    super.onDataPacket(net, pkt);
  }

  @Override
  public TileEntity getTile() {
    return this;
  }

  @Override
  public UUIDNameData.Name getEntityName() {
    if (tileName == null) {
      LogicalSide side = EffectiveSide.get();
      if (side.isClient()) {
        return UUIDNameData.Name.EMPTY;
      } else {
        if (getEntityId() == null) {
          return UUIDNameData.Name.EMPTY;
        }
        tileName = DataStorage.getTileName(getEntityId());
      }
    }

    return tileName;
  }
}
