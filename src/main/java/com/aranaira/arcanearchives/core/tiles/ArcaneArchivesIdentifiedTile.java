package com.aranaira.arcanearchives.core.tiles;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.UUID;

public abstract class ArcaneArchivesIdentifiedTile extends TileEntity implements IArcaneArchivesTile {
  protected UUID uuid = null;

  public ArcaneArchivesIdentifiedTile(TileEntityType<?> tileEntityTypeIn) {
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
  public UUID getTileId() {
    if (this.uuid == null) {
      this.uuid = UUID.randomUUID();
    }
    return uuid;
  }
}
