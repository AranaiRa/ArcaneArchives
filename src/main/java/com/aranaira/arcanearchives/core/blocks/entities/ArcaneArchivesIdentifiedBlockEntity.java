package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.api.blockentities.IArcaneArchivesBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ArcaneArchivesIdentifiedBlockEntity extends TileEntity implements IArcaneArchivesBlockEntity {
  protected UUID uuid = null;

  public ArcaneArchivesIdentifiedBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    if (uuid != null) {
      compound.putUUID(Identifiers.tileId, uuid);
    }
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    if (compound.hasUUID(Identifiers.tileId)) {
      this.uuid = compound.getUUID(Identifiers.tileId);
    } else {
      // TODO: Should we be generating a unique ID here?
      this.uuid = UUID.randomUUID();
    }

    super.load(state, compound);
  }

  @Override
  public UUID getEntityId() {
    if (this.uuid == null) {
      this.uuid = UUID.randomUUID();
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
    }
    return data;
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    if (pkt.getType() == 9 && pkt.getTag().hasUUID(Identifiers.tileId)) {
      this.uuid = pkt.getTag().getUUID(Identifiers.tileId);
    }
  }
}
